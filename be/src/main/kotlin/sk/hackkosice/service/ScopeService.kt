package sk.esten.uss.gbco2.service

import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.create.CreateScopeDto
import sk.esten.uss.gbco2.dto.request.filter.ScopeFilter
import sk.esten.uss.gbco2.dto.request.filter.ScopeReadAllFilter
import sk.esten.uss.gbco2.dto.response.ScopeDto
import sk.esten.uss.gbco2.dto.response.detail.ScopeDetailDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.AnalysisParamMapper
import sk.esten.uss.gbco2.mapper.MaterialMapper
import sk.esten.uss.gbco2.mapper.ScopeMapper
import sk.esten.uss.gbco2.model.entity.quantity.IOEnum
import sk.esten.uss.gbco2.model.entity.scope.Scope
import sk.esten.uss.gbco2.model.entity.scope.VScopeTranslated
import sk.esten.uss.gbco2.model.entity.scope.VScopeTranslated_
import sk.esten.uss.gbco2.model.entity.scope_analysis_param.ScopeAnalysisParam
import sk.esten.uss.gbco2.model.entity.scope_denominator.ScopeDenominator
import sk.esten.uss.gbco2.model.entity.scope_material_node.ScopeMaterialNode
import sk.esten.uss.gbco2.model.repository.scope.AdvScopeVRepository
import sk.esten.uss.gbco2.model.repository.scope.ScopeERepository
import sk.esten.uss.gbco2.model.repository.scope.ScopeVRepository
import sk.esten.uss.gbco2.model.specification.ScopeSpecification
import sk.esten.uss.gbco2.utils.idsInSpecification
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
@Transactional
class ScopeService(
    override val entityRepository: ScopeERepository,
    override val viewRepository: ScopeVRepository,
    private val advScopeVRepository: AdvScopeVRepository,
    private val materialService: MaterialService,
    private val materialNodeService: MaterialNodeService,
    private val analysisParamService: AnalysisParamService,
    private val scopeMaterialNodeService: ScopeMaterialNodeService,
    private val scopeAnalysisParamService: ScopeAnalysisParamService,
    private val scopeDenominatorService: ScopeDenominatorService,
    private val mapper: ScopeMapper,
    private val materialMapper: MaterialMapper,
    private val analysisParamMapper: AnalysisParamMapper,
) :
    CrudServiceView<
        Scope,
        VScopeTranslated,
        ScopeDto,
        ScopeDetailDto,
        CreateScopeDto,
        CreateScopeDto,
        Long,
        ScopeFilter,
        ScopeReadAllFilter>() {

    override fun createEntity(createDto: CreateScopeDto): Scope {
        val scope = mapper.map(createDto)
        newEnTranslation(scope.nameK, createDto.name, scope.nameTranslations)

        assignScopeForeignAttributes(createDto, scope, false)

        return scope
    }

    override fun updateEntity(updateDto: CreateScopeDto, entity: Scope) {
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.nameTranslations)

        assignScopeForeignAttributes(updateDto, entity, true)
    }

    private fun assignScopeForeignAttributes(
        dto: CreateScopeDto,
        entity: Scope,
        isUpdate: Boolean
    ) {
        val savedScope = entity.id?.let { getInternal(it) }

        val scopeNodeMaterials = dto.scopeMaterialNodeData

        val scopeAnalysisParams: MutableSet<ScopeAnalysisParam> = mutableSetOf()
        val scopeMaterialNodes: MutableSet<ScopeMaterialNode> = mutableSetOf()
        val scopeDenominators: MutableSet<ScopeDenominator> = mutableSetOf()

        entity.scopeMaterialNodes.clear()
        entity.scopeDenominators.clear()
        entity.scopeAnalysisParams.clear()

        dto.nodeIds?.forEach { nodeId ->
            dto.scopeMaterialData?.forEach { smd ->
                if (smd.factorA == true || smd.factorB == true || smd.factorC == true) {
                    val savedScopeMaterialNodes =
                        savedScope?.scopeMaterialNodes?.filter {
                            it.materialNode?.material?.id == smd.materialId &&
                                it.materialNode?.node?.id == nodeId
                        }

                    val scopeMaterialNode =
                        savedScopeMaterialNodes?.find {
                            it.materialNode?.material?.id == smd.materialId &&
                                it.materialNode?.node?.id == nodeId
                        }

                    if (scopeMaterialNode != null && isUpdate) {
                        scopeMaterialNodes.add(scopeMaterialNode)
                    } else {
                        val materialNode =
                            materialNodeService.getByNodeAndMaterial(nodeId, smd.materialId)
                        val scopeNodeMaterialData =
                            scopeNodeMaterials?.firstOrNull { materialNode.id == it.materialNodeId }

                        var useCalculation = false
                        if (scopeNodeMaterialData?.useAs != null &&
                                scopeNodeMaterialData.useAs == IOEnum.INPUT
                        )
                            useCalculation = true

                        scopeMaterialNodes.add(
                            ScopeMaterialNode().apply {
                                this.scope = entity
                                this.materialNode = materialNode
                                factorA = smd.factorA
                                factorB = smd.factorB
                                factorC = smd.factorC
                                this.useCalculation = useCalculation
                            }
                        )
                    }
                }
            }
        }

        dto.scopeAnalysisParamIds?.forEach { analysisParamId ->
            val savedScopeAnalysisParams =
                savedScope?.scopeAnalysisParams?.filter { analysisParamId == it.analysisParam?.id }

            val scopeAnalysisParam =
                savedScopeAnalysisParams?.find { it.analysisParam?.id == analysisParamId }

            if (scopeAnalysisParam != null && isUpdate) {
                scopeAnalysisParams.add(scopeAnalysisParam)
            } else {
                val analysisParam = analysisParamService.get(analysisParamId)
                scopeAnalysisParams.add(
                    ScopeAnalysisParam().apply {
                        this.scope = entity
                        this.analysisParam = analysisParam
                    }
                )
            }
        }

        dto.scopeDenominatorIds?.forEach { denominatorId ->
            val savedScopeDenominators =
                savedScope?.scopeDenominators?.filter { denominatorId == it.material?.id }

            val scopeDenominator = savedScopeDenominators?.find { it.material?.id == denominatorId }

            if (scopeDenominator != null && isUpdate) {
                scopeDenominators.add(scopeDenominator)
            } else {
                val material = materialService.get(denominatorId)
                scopeDenominators.add(
                    ScopeDenominator().apply {
                        this.scope = entity
                        this.material = material
                    }
                )
            }
        }

        if (isUpdate) {
            val toDeleteScopeMaterialNodes =
                savedScope?.scopeMaterialNodes?.minus(scopeMaterialNodes)
            scopeMaterialNodeService.deleteAll(toDeleteScopeMaterialNodes as Set<ScopeMaterialNode>)
            val toDeleteScopeDenominators = savedScope.scopeDenominators.minus(scopeDenominators)
            scopeDenominatorService.deleteAll(toDeleteScopeDenominators)
            val toDeleteScopeAnalysisParams =
                savedScope.scopeAnalysisParams.minus(scopeAnalysisParams)
            scopeAnalysisParamService.deleteAll(toDeleteScopeAnalysisParams)
        }

        entityRepository.saveAndFlush(entity)
        scopeMaterialNodeService.saveAll(scopeMaterialNodes)
        scopeDenominatorService.saveAll(scopeDenominators)
        scopeAnalysisParamService.saveAll(scopeAnalysisParams)
    }

    override fun repositoryPageQuery(filter: ScopeFilter): Page<VScopeTranslated> {
        val specification = ScopeSpecification(filter)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    override fun viewToDto(entity: VScopeTranslated, translated: Boolean): ScopeDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }
    override fun viewToDetailDto(entity: VScopeTranslated, translated: Boolean): ScopeDetailDto {
        val dto = if (translated) mapper.mapToDetail(entity) else mapper.mapToDetailEn(entity)
        dto.scopeDenominators =
            entity
                .scopeDenominators
                .mapNotNull { it.material }
                .map {
                    if (translated) materialMapper.mapToSimple(it)
                    else materialMapper.mapToSimpleEn(it)
                }
                .sortedBy { it.id }
        dto.scopeAnalysisParams =
            entity
                .scopeAnalysisParams
                .mapNotNull { it.analysisParam }
                .map {
                    if (translated) analysisParamMapper.mapSimple(it)
                    else analysisParamMapper.mapSimpleEn(it)
                }
                .sortedBy { it.id }
        return dto
    }

    override fun repositoryGetAllQuery(filter: ScopeReadAllFilter?): List<VScopeTranslated> {
        return filter?.let {
            viewRepository.findAll(
                advScopeVRepository
                    .getFilteredScopes(filter)
                    .idsInSpecification(VScopeTranslated_.idView),
                filter.toSort()
            )
        }
            ?: throw IllegalStateException("ScopeReadAllFilterDto can not be null")
    }

    @Transactional
    override fun create(createDto: CreateScopeDto): ScopeDto {
        return super.create(createDto)
    }

    @Transactional
    override fun update(id: Long, updateDto: CreateScopeDto): ScopeDto {
        return super.update(id, updateDto)
    }
}
