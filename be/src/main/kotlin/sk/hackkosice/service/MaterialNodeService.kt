package sk.esten.uss.gbco2.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateMaterialNodeDto
import sk.esten.uss.gbco2.dto.request.filter.MaterialNodeFilter
import sk.esten.uss.gbco2.dto.request.update.UpdateMaterialNodeDto
import sk.esten.uss.gbco2.dto.response.MaterialNodeDto
import sk.esten.uss.gbco2.dto.response.detail.MaterialNodeDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeDto
import sk.esten.uss.gbco2.exceptions.DatabaseException
import sk.esten.uss.gbco2.exceptions.ValidationException
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.MaterialNodeAnalysisParamMapper
import sk.esten.uss.gbco2.mapper.MaterialNodeMapper
import sk.esten.uss.gbco2.model.entity.material_node.MaterialNode
import sk.esten.uss.gbco2.model.entity.material_node.VMaterialNodeTranslated
import sk.esten.uss.gbco2.model.entity.material_node_analysis_param.MaterialNodeAnalysisParam
import sk.esten.uss.gbco2.model.entity.material_node_type.MaterialNodeType
import sk.esten.uss.gbco2.model.repository.material_node.MaterialNodeERepository
import sk.esten.uss.gbco2.model.repository.material_node.MaterialNodeVRepository
import sk.esten.uss.gbco2.model.specification.MaterialNodeSpecification
import sk.esten.uss.gbco2.utils.safeLet

@Service
class MaterialNodeService(
    override val entityRepository: MaterialNodeERepository,
    override val viewRepository: MaterialNodeVRepository,
    private val mapper: MaterialNodeMapper,
    private val nodeService: NodeService,
    private val materialService: MaterialService,
    private val materialTypeService: MaterialTypeService,
    private val analysisParamExprService: AnalysisParamExprService,
    private val scopeMaterialNodeService: ScopeMaterialNodeService,
    private val materialNodeAnalysisParamService: MaterialNodeAnalysisParamService,
    private val materialNodeTypeService: MaterialNodeTypeService,
    @Autowired private val materialNodeAnalysisParamMapper: MaterialNodeAnalysisParamMapper,
) :
    CrudServiceView<
        MaterialNode,
        VMaterialNodeTranslated,
        MaterialNodeDto,
        MaterialNodeDetailDto,
        CreateMaterialNodeDto,
        UpdateMaterialNodeDto,
        Long,
        MaterialNodeFilter,
        ReadAllParamsFilterDto>() {
    override fun createEntity(createDto: CreateMaterialNodeDto): MaterialNode {
        val materialNode = mapper.map(createDto)

        createDto.materialNodeAnalysisParams.forEach {
            materialNode.materialNodeAnalysisParams.add(
                materialNodeAnalysisParamMapper.map(it, it.processAdv).apply {
                    this.materialNode = materialNode
                }
            )
        }
        materialNode.materialNodeTypes =
            materialTypeService
                .getAllMaterialTypesByMaterialType(createDto.materialTypeIds)
                .map { materialType ->
                    MaterialNodeType().apply {
                        this.materialNode = materialNode
                        this.materialType = materialType
                    }
                }
                .toMutableSet()

        return materialNode
    }

    override fun updateEntity(updateDto: UpdateMaterialNodeDto, entity: MaterialNode) {
        val memo = updateDto.memo
        if (memo.isNullOrBlank()) {
            throw ValidationException(description = "errorMemoBlank")
        } else if (memo.isNotBlank() && memo == entity.memo) {
            throw ValidationException(description = "errorMemoMustBeNew")
        }
        mapper.update(updateDto, entity)
    }

    @Transactional
    override fun update(id: Long, updateDto: UpdateMaterialNodeDto): MaterialNodeDto {
        val materialNode = getInternal(id)
        val product = materialNode.product
        updateEntity(updateDto, materialNode)
        try {
            if (product == false && updateDto.product == true) {
                materialNode.product = false
            }
            val mnApIdAnalysisMap =
                materialNode.materialNodeAnalysisParams.associateBy { it.analysisParam?.id }
            val mnApsToSave = mutableListOf<MaterialNodeAnalysisParam>()
            val currentMaterialNodeAps = materialNode.materialNodeAnalysisParams.toList()
            updateDto.materialNodeAnalysisParams.forEach {
                if (mnApIdAnalysisMap.containsKey(it.analysisParamId)) {
                    safeLet(it.analysisParamId, it.materialNodeAdvBasisId) {
                        analysisParamId,
                        materialNodeAdvBasisId ->
                        val currentAp = mnApIdAnalysisMap.get(analysisParamId)
                        if (currentAp != null) {
                            if (it.materialNodeAdvBasisId !== materialNode.id && it.processAdv) {
                                currentAp.materialNodeAdvBasis =
                                    entityRepository.findByIdOrNull(materialNodeAdvBasisId)
                            }
                            currentAp.processAdv = it.processAdv
                            currentAp.stdDeviation = it.stdDeviation
                            currentAp.memo = it.memo
                            currentAp.analysisParamExpr =
                                analysisParamExprService.get(it.analysisParamExprId)
                            currentAp.contentDeterminationMethod = it.contentDeterminationMethod
                        }
                    }
                } else {
                    val mnApToSave =
                        materialNodeAnalysisParamMapper.map(it, it.processAdv).apply {
                            this.materialNode = materialNode
                        }
                    materialNode.materialNodeAnalysisParams.add(mnApToSave)
                    mnApsToSave.add(mnApToSave)
                }
            }
            val mnTypesIdsMap = materialNode.materialNodeTypes.associateBy { it.materialType?.id }
            val mnTypesToSave = mutableListOf<MaterialNodeType>()
            val currentMaterialNodeTypes = materialNode.materialNodeTypes.toList()
            updateDto.materialTypeIds.forEach {
                if (!mnTypesIdsMap.containsKey(it)) {
                    val mnTypeToSave =
                        MaterialNodeType().apply {
                            this.materialNode = materialNode
                            this.materialType = materialTypeService.get(it)
                        }
                    materialNode.materialNodeTypes.add(mnTypeToSave)
                    mnTypesToSave.add(mnTypeToSave)
                }
            }
            val mnApsToDelete =
                (currentMaterialNodeAps - mnApsToSave).associateBy { it.analysisParam?.id }
            val mnTypesToDelete =
                (currentMaterialNodeTypes - mnTypesToSave).associateBy { it.materialType?.id }
            materialNode.materialNodeAnalysisParams.removeIf {
                mnApsToDelete.containsKey(it.analysisParam?.id)
            }
            materialNode.materialNodeTypes.removeIf {
                mnTypesToDelete.containsKey(it.materialType?.id)
            }
            val savedEntity = entityRepository.saveAndFlush(materialNode)
            return viewToDto(
                getView(
                    savedEntity.getPk()
                        ?: throw ValidationException(
                            description = "MaterialNode's primary key is NULL!"
                        )
                )
            )
        } catch (e: DataIntegrityViolationException) {
            throw DatabaseException(e)
        }
    }

    override fun viewToDto(entity: VMaterialNodeTranslated, translated: Boolean): MaterialNodeDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun repositoryPageQuery(filter: MaterialNodeFilter): Page<VMaterialNodeTranslated> {
        val specification = MaterialNodeSpecification(filter, nodeService)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    override fun viewToDetailDto(
        entity: VMaterialNodeTranslated,
        translated: Boolean,
    ): MaterialNodeDetailDto {
        return if (translated) mapper.mapToDetail(entity) else mapper.mapToDetailEn(entity)
    }

    fun getAllByNodeAndMaterial(nodeId: Long?, materialId: Long?): List<MaterialNode> =
        entityRepository.findAllByNodeIdAndMaterialId(nodeId, materialId)

    fun getByNodeAndMaterial(nodeId: Long?, materialId: Long?): MaterialNode {
        return safeLet(nodeId, materialId) { node_id, material_id ->
            entityRepository.findByNodeIdAndMaterialId(node_id, material_id)
        }
            ?: throw ValidationException(description = "[nodeId] or [materialId] is null!")
    }

    fun getById(id: Long): MaterialNode? {
        val entity = entityRepository.findByIdOrNull(id)
        entity?.materialNodeAnalysisParams =
            materialNodeAnalysisParamService.getAllByMaterialNodeId(id)
        entity?.materialNodeTypes = materialNodeTypeService.getAllByMaterialNodeId(id)
        entity?.scopeMaterialNode = scopeMaterialNodeService.getAllByMaterialNodeId(id)
        return entity
    }

    @Transactional(readOnly = true)
    fun getNodesInMaterialNode(nodeLevel: Long?): List<SimpleNodeDto> {
        return nodeService.getNodesInMaterialNode(nodeLevel)
    }

    @Transactional(readOnly = true)
    fun getMaterialsInMaterialNode(nodeIds: List<Long>?): List<SimpleMaterialDto>? {
        return materialService.getMaterialsInMaterialNode(nodeIds)
    }
}
