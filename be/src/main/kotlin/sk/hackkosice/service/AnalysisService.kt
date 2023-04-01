package sk.esten.uss.gbco2.service

import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.create.CreateAnalysisDto
import sk.esten.uss.gbco2.dto.request.filter.AnalysisFilter
import sk.esten.uss.gbco2.dto.request.filter.AnalysisReadAllFilter
import sk.esten.uss.gbco2.dto.request.update.UpdateAnalysisDto
import sk.esten.uss.gbco2.dto.response.AnalysisDto
import sk.esten.uss.gbco2.dto.response.detail.AnalysisDetailDto
import sk.esten.uss.gbco2.exceptions.NotFoundException
import sk.esten.uss.gbco2.exceptions.ValidationException
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.AnalysisMapper
import sk.esten.uss.gbco2.mapper.QuantityMapper
import sk.esten.uss.gbco2.model.entity.analysis.Analysis
import sk.esten.uss.gbco2.model.entity.analysis.VAnalysisTranslated
import sk.esten.uss.gbco2.model.repository.analysis.AdvAnalysisERepository
import sk.esten.uss.gbco2.model.repository.analysis.AnalysisERepository
import sk.esten.uss.gbco2.model.repository.analysis.AnalysisVRepository
import sk.esten.uss.gbco2.model.repository.quantity.QuantityVRepository
import sk.esten.uss.gbco2.model.specification.AnalysisPageSpecification
import sk.esten.uss.gbco2.model.specification.AnalysisReadAllSpecification
import sk.esten.uss.gbco2.utils.principalLangOrEn
import sk.esten.uss.gbco2.utils.principalUnitSetIdOrMetricId

@Service
class AnalysisService(
    override val entityRepository: AnalysisERepository,
    private val advEntityRepository: AdvAnalysisERepository,
    override val viewRepository: AnalysisVRepository,
    private val quantityViewRepository: QuantityVRepository,
    private val materialNodeService: MaterialNodeService,
    private val mapper: AnalysisMapper,
    private val quantityMapper: QuantityMapper,
    private val nodeService: NodeService,
    private val materialNodeTypeService: MaterialNodeTypeService,
    private val scopeMaterialNodeService: ScopeMaterialNodeService
) :
    CrudServiceView<
        Analysis,
        VAnalysisTranslated,
        AnalysisDto,
        AnalysisDetailDto,
        CreateAnalysisDto,
        UpdateAnalysisDto,
        Long,
        AnalysisFilter,
        AnalysisReadAllFilter>() {

    @Transactional(readOnly = true)
    override fun getById(id: Long, translated: Boolean): AnalysisDetailDto {
        val entity =
            viewRepository.findViewByIdLanguageAndUnitSet(
                id,
                principalLangOrEn().id,
                principalUnitSetIdOrMetricId()
            )
                ?: throw NotFoundException()
        return viewToDetailDto(entity, translated)
    }

    override fun viewToDto(entity: VAnalysisTranslated, translated: Boolean): AnalysisDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: CreateAnalysisDto): Analysis {
        val entity = mapper.map(createDto)
        entity.materialNode =
            this.materialNodeService.getByNodeAndMaterial(createDto.nodeId, createDto.materialId)
        entity.editable = true
        entity.calculated = false
        return entity
    }

    override fun updateEntity(updateDto: UpdateAnalysisDto, entity: Analysis) {

        val memo = updateDto.memo
        if (memo.isNullOrBlank()) {
            throw ValidationException(description = "errorMemoBlank")
        } else if (memo.isNotBlank() && memo == entity.memo) {
            throw ValidationException(description = "errorMemoMustBeNew")
        }
        mapper.update(updateDto, entity)
    }

    override fun viewToDetailDto(
        entity: VAnalysisTranslated,
        translated: Boolean
    ): AnalysisDetailDto {
        val languageId = principalLangOrEn().id
        val unitSetId = principalUnitSetIdOrMetricId()
        val analysisDetailDto: AnalysisDetailDto =
            if (translated) {
                mapper.mapDetail(entity)
            } else {
                mapper.mapDetailEn(entity)
            }

        if (analysisDetailDto.unitAnalysisFormatAbbr == null) {
            analysisDetailDto.unitAnalysisFormatAbbr = analysisDetailDto.unitNumenator?.name
            if (entity.analysisParam?.unitType?.compute == true) {
                analysisDetailDto.unitAnalysisFormatAbbr +=
                    " / ${analysisDetailDto.unitDenominator?.name ?: ""}"
            }
        }

        val quantities =
            quantityViewRepository.findAllViewsByAnalysisLanguageUnitSetAndAnalysisParam(
                entity.id,
                entity.analysisParam?.id,
                languageId,
                unitSetId
            )
        if (translated) {
            analysisDetailDto.quantities = quantityMapper.mapQuantitiesToQuantitiesDto(quantities)
        } else {
            analysisDetailDto.quantities = quantityMapper.mapQuantitiesToQuantitiesDtoEn(quantities)
        }
        return analysisDetailDto
    }

    override fun repositoryGetAllQuery(filter: AnalysisReadAllFilter?): List<VAnalysisTranslated> {
        return filter?.let { viewRepository.findAll(AnalysisReadAllSpecification(it), it.toSort()) }
            ?: throw IllegalStateException("AnalysisReadAllFilter can not be null")
    }

    fun getAllEligibleAnalysisForAutomaticAssignment(
        year: Int,
        materialNodeIds: List<Long>,
        remoteMaterialCodeIds: List<Long>
    ): List<Analysis> {
        return advEntityRepository.findAllEligibleAnalysisForAutomaticAssignment(
            year,
            materialNodeIds,
            remoteMaterialCodeIds
        )
    }

    override fun repositoryPageQuery(filter: AnalysisFilter): Page<VAnalysisTranslated> {
        return viewRepository.findAll(
            AnalysisPageSpecification(
                filter,
                nodeService,
                materialNodeTypeService,
                scopeMaterialNodeService
            ),
            filter.toPageable()
        )
    }
}
