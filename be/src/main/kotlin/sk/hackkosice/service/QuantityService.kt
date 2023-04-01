package sk.esten.uss.gbco2.service

import java.time.LocalDate
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateQuantityDto
import sk.esten.uss.gbco2.dto.request.filter.AnalysisParamReadAllFilter
import sk.esten.uss.gbco2.dto.request.filter.QuantityFilter
import sk.esten.uss.gbco2.dto.request.update.UpdateQuantityDto
import sk.esten.uss.gbco2.dto.response.QuantityAnalysisParamDto
import sk.esten.uss.gbco2.dto.response.detail.QuantityDetailDto
import sk.esten.uss.gbco2.dto.response.quantity.QuantityDto
import sk.esten.uss.gbco2.dto.response.quantity.QuantityPageResponseDto
import sk.esten.uss.gbco2.exceptions.DatabaseException
import sk.esten.uss.gbco2.exceptions.NotFoundException
import sk.esten.uss.gbco2.exceptions.ValidationException
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.AnalysisParamMapper
import sk.esten.uss.gbco2.mapper.QuantityAnalysisMapper
import sk.esten.uss.gbco2.mapper.QuantityMapper
import sk.esten.uss.gbco2.model.entity.quantity.Quantity
import sk.esten.uss.gbco2.model.entity.quantity.VQuantityTranslated
import sk.esten.uss.gbco2.model.entity.quantity_analysis.QuantityAnalysis
import sk.esten.uss.gbco2.model.entity.quantity_analysis.QuantityAnalysisProjection
import sk.esten.uss.gbco2.model.entity.quantity_analysis.QuantityAnalysisUpdate
import sk.esten.uss.gbco2.model.repository.analysis_param.AdvAnalysisParamVRepository
import sk.esten.uss.gbco2.model.repository.quantity.AdvQuantityRepository
import sk.esten.uss.gbco2.model.repository.quantity.QuantityERepository
import sk.esten.uss.gbco2.model.repository.quantity.QuantityVRepository
import sk.esten.uss.gbco2.model.repository.quantity_analysis.QuantityAnalysisERepository
import sk.esten.uss.gbco2.model.repository.quantity_analysis.QuantityAnalysisVRepository
import sk.esten.uss.gbco2.model.specification.QuantitySpecification
import sk.esten.uss.gbco2.utils.principalLangOrEn
import sk.esten.uss.gbco2.utils.principalUnitSetIdOrMetricId
import sk.esten.uss.gbco2.utils.safeLet

@Service
class QuantityService(
    override val entityRepository: QuantityERepository,
    override val viewRepository: QuantityVRepository,
    private val quantityAnalysisVRepository: QuantityAnalysisVRepository,
    private val quantityAnalysisERepository: QuantityAnalysisERepository,
    private val mapper: QuantityMapper,
    private val quantityAnalysisMapper: QuantityAnalysisMapper,
    private val analysisParamMapper: AnalysisParamMapper,
    private val nodeService: NodeService,
    private val materialNodeService: MaterialNodeService,
    private val advQuantityRepository: AdvQuantityRepository,
    private val advAnalysisParamVRepository: AdvAnalysisParamVRepository,
    private val analysisParamService: AnalysisParamService,
    private val analysisService: AnalysisService,
) :
    CrudServiceView<
        Quantity,
        VQuantityTranslated,
        QuantityDto,
        QuantityDetailDto,
        CreateQuantityDto,
        UpdateQuantityDto,
        Long,
        QuantityFilter,
        ReadAllParamsFilterDto>() {

    override fun viewToDto(entity: VQuantityTranslated, translated: Boolean): QuantityDto =
        if (translated) mapper.map(entity) else mapper.mapEn(entity)

    override fun createEntity(createDto: CreateQuantityDto): Quantity {
        createDto.validate()
        val entity = mapper.map(createDto)
        entity.materialNode =
            materialNodeService.getByNodeAndMaterial(createDto.nodeId, createDto.materialId)
        entity.editable = true
        return entity
    }

    @Transactional
    override fun create(createDto: CreateQuantityDto): QuantityDto {
        try {
            val quantity = createEntity(createDto)
            val analysisParamsIds =
                getAnalysisParamIdsForQuantity(createDto.nodeId, createDto.materialId)

            val qAnalyses = mutableListOf<QuantityAnalysis>()
            createDto.materialAnalysisParams?.forEach {
                if (analysisParamsIds.contains(it.analysisParamId)) {
                    safeLet(it.analysisId, it.analysisParamId) { analysisId, analysisParamId ->
                        qAnalyses.add(
                            createNewQuantityAnalysis(
                                quantity,
                                analysisParamId,
                                analysisId,
                                it.memo
                            )
                        )
                    }
                } else
                    throw ValidationException(
                        description =
                            "Selected analysis param [${it.analysisParamId}] is not present in the GBC_MATERIAL_NODE_AP table, so it cannot be assigned to the Quantity!"
                    )
            }

            entityRepository.save(quantity)
            quantityAnalysisERepository.saveAllAndFlush(qAnalyses)
            return getQuantityDto(quantity)
        } catch (e: DataIntegrityViolationException) {
            throw DatabaseException(e)
        }
    }

    override fun updateEntity(updateDto: UpdateQuantityDto, entity: Quantity) {
        updateDto.validate()
        val memo = updateDto.memo
        if (memo.isNullOrBlank()) {
            throw ValidationException(description = "errorMemoBlank")
        } else if (memo.isNotBlank() && memo == entity.memo) {
            throw ValidationException(description = "errorMemoMustBeNew")
        }

        mapper.update(updateDto, entity)
    }

    @Transactional
    override fun update(id: Long, updateDto: UpdateQuantityDto): QuantityDto {
        val quantity = getInternal(id)
        updateEntity(updateDto, quantity)

        try {
            val materialNode = quantity.materialNode
            val analysisParamsIds =
                getAnalysisParamIdsForQuantity(materialNode?.node?.id, materialNode?.material?.id)

            val qAnalysesCurrent = quantityAnalysisERepository.findAllByQuantityId(quantity.id)
            val qApIdAnalysisMap = qAnalysesCurrent.associateBy { it.analysisParam?.id }

            val qAnalysesToSave = mutableListOf<QuantityAnalysis>()
            updateDto.materialAnalysisParams?.forEach {
                if (analysisParamsIds.contains(it.analysisParamId)) {
                    safeLet(it.analysisId, it.analysisParamId) { analysisId, analysisParamId ->
                        val existingAnalysis = qApIdAnalysisMap[analysisParamId]

                        if (existingAnalysis == null) {
                            // QuantityAnalysis does not exist for the analysisParamId - create new
                            qAnalysesToSave.add(
                                createNewQuantityAnalysis(
                                    quantity,
                                    analysisParamId,
                                    analysisId,
                                    it.memo
                                )
                            )
                        } else {
                            // QuantityAnalysis for selected analysisParam exists - update it
                            existingAnalysis.analysis = analysisService.get(analysisId)
                            existingAnalysis.memo = it.memo
                            existingAnalysis.objVersion = it.objVersion
                            qAnalysesToSave.add(existingAnalysis)
                        }
                    }
                } else
                    throw ValidationException(
                        description =
                            "Selected analysis param [${it.analysisParamId}] is not present in the GBC_MATERIAL_NODE_AP table, so it cannot be assigned to the Quantity!"
                    )
            }

            val qAnalysesToDelete = qAnalysesCurrent - qAnalysesToSave.toSet()
            entityRepository.save(quantity)
            quantityAnalysisERepository.deleteAll(qAnalysesToDelete)
            quantityAnalysisERepository.saveAllAndFlush(qAnalysesToSave)
            return getQuantityDto(quantity)
        } catch (e: DataIntegrityViolationException) {
            throw DatabaseException(e)
        }
    }

    override fun repositoryPageQuery(filter: QuantityFilter): Page<VQuantityTranslated> {
        val specification = QuantitySpecification(filter, nodeService)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    override fun viewToDetailDto(
        entity: VQuantityTranslated,
        translated: Boolean,
    ): QuantityDetailDto = if (translated) mapper.mapDetail(entity) else mapper.mapDetailEn(entity)

    @Transactional(readOnly = true)
    override fun getView(id: Long): VQuantityTranslated {
        return viewRepository.findByIdAndLanguageIdAndUnitSetId(
            id,
            principalLangOrEn().id,
            principalUnitSetIdOrMetricId()
        )
            ?: throw NotFoundException("Did not find quantity with id: $id")
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long, translated: Boolean): QuantityDetailDto {
        val quantityView = getView(id)

        val quantityAnalyses =
            quantityAnalysisVRepository.findAllByQuantityIdViewAndUnitSetId(
                id,
                quantityView.unitSetId,
                principalLangOrEn().id
            )

        val detailDto = viewToDetailDto(quantityView, translated)
        detailDto.materialAnalysisParams =
            getMappedMaterialAnalysisParams(quantityView, quantityAnalyses)
        return detailDto
    }

    private fun getMappedMaterialAnalysisParams(
        quantityView: VQuantityTranslated,
        quantityAnalyses: List<QuantityAnalysisProjection>,
    ): MutableList<QuantityAnalysisParamDto> {
        val analysisParamIdsFromQA = quantityAnalyses.map { it.getAnalysisParamId() }
        val analysisParams =
            quantityView.materialNode?.materialNodeAnalysisParams
                ?.map { it.analysisParam }
                ?.filterNot { analysisParamIdsFromQA.contains(it?.id) }

        val response: MutableList<QuantityAnalysisParamDto> = mutableListOf()

        // add full quantity analyses
        response.addAll(quantityAnalyses.map { quantityAnalysisMapper.map(it) })

        // add remaining analysisParams
        if (analysisParams != null) {
            response.addAll(
                analysisParams.filterNotNull().map { analysisParamTranslated ->
                    QuantityAnalysisParamDto().apply {
                        analysisParam = analysisParamMapper.mapSimple(analysisParamTranslated)
                    }
                }
            )
        }

        return response
    }

    fun getQuantities(filter: QuantityFilter): QuantityPageResponseDto =
        QuantityPageResponseDto(
            getPaginatedWithFilter(filter),
            advQuantityRepository.getQuantitySummary(filter)
        )

    fun getQuantityAnalysesBasedOnQuantityId(quantityId: Long): List<QuantityAnalysis> {
        return quantityAnalysisERepository.findAllByQuantityId(quantityId)
    }

    fun getAllEligibleQuantitiesForAutoAssignment(
        year: Int,
        nodeIds: List<Long>?,
        materialIds: List<Long>?,
        corpCloseDate: LocalDate?
    ): List<Quantity> {
        return advQuantityRepository.findEligibleQuantityForAAA(
            year,
            nodeIds,
            materialIds,
            corpCloseDate
        )
    }

    fun syncWithDb(
        toInsert: List<QuantityAnalysis>,
        toUpdate: List<QuantityAnalysisUpdate>,
        toDelete: List<QuantityAnalysis>
    ) {
        quantityAnalysisERepository.saveAll(toInsert)

        for (qaUpdate in toUpdate) {
            val qa: QuantityAnalysis? = qaUpdate.quantityAnalysis
            qa?.analysis = qaUpdate.analysis
            qa?.analysisParam = qaUpdate.analysisParam

            if (qa != null) quantityAnalysisERepository.save(qa)
        }

        quantityAnalysisERepository.deleteAll(toDelete)
    }

    private fun getQuantityDto(entity: Quantity): QuantityDto =
        viewToDto(
            getView(
                entity.getPk()
                    ?: throw ValidationException(description = "Quantity's primary key is NULL!")
            )
        )

    private fun getAnalysisParamIdsForQuantity(nodeId: Long?, materialId: Long?): List<Long> {
        var quantityAPs = emptyList<Long>()
        safeLet(nodeId, materialId) { nId, mId ->
            quantityAPs =
                advAnalysisParamVRepository.getAllFromMaterialNodeAP(
                        AnalysisParamReadAllFilter().apply {
                            this.nodeId = nId
                            materialIds = listOf(mId)
                        }
                    )
                    .mapNotNull { it.id }
        }
            ?: throw ValidationException(description = "`nodeId` nor `materialId` cannot be null")

        return quantityAPs.ifEmpty {
            throw ValidationException(
                description =
                    "There are no Analysis Params for selected MaterialNode(materialID, nodeID) - in the table GBC_MATERIAL_NODE_AP"
            )
        }
    }

    private fun createNewQuantityAnalysis(
        quantity: Quantity,
        analysisParamId: Long,
        analysisId: Long,
        memo: String?
    ) =
        QuantityAnalysis().apply {
            this.quantity = quantity
            analysisParam = analysisParamService.get(analysisParamId)
            analysis = analysisService.get(analysisId)
            this.memo = memo
        }
}
