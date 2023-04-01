package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.ExpressionValidationDto
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.MaterialNodeAnalysisParamDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.ExpressionValidationMapper
import sk.esten.uss.gbco2.model.entity.material_node_analysis_param.MaterialNodeAnalysisParam
import sk.esten.uss.gbco2.model.entity.material_node_analysis_param.VMaterialNodeAnalysisParamTranslated
import sk.esten.uss.gbco2.model.repository.material_node_analysis_param.AdvMaterialNodeAnalysisParamERepository
import sk.esten.uss.gbco2.model.repository.material_node_analysis_param.MaterialNodeAnalysisParamERepository
import sk.esten.uss.gbco2.model.repository.material_node_analysis_param.MaterialNodeAnalysisParamVRepository

@Service
class MaterialNodeAnalysisParamService(
    override val viewRepository: MaterialNodeAnalysisParamVRepository,
    override val entityRepository: MaterialNodeAnalysisParamERepository,
    private val advEntityRepository: AdvMaterialNodeAnalysisParamERepository,
    private val expressionValidationMapper: ExpressionValidationMapper,
) :
    CrudServiceView<
        MaterialNodeAnalysisParam,
        VMaterialNodeAnalysisParamTranslated,
        MaterialNodeAnalysisParamDto,
        MaterialNodeAnalysisParamDto,
        MaterialNodeAnalysisParamDto,
        MaterialNodeAnalysisParamDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {
    fun getAllByNodesAndProcessAdv(
        nodeIds: List<Long>,
        processAdv: Boolean
    ): List<MaterialNodeAnalysisParam> =
        advEntityRepository.findAllByNodesAndProcessAdv(nodeIds, processAdv)

    fun getAllByNodesAndYear(nodeIds: List<Long>, year: Int): List<ExpressionValidationDto> =
        advEntityRepository.findAllExpressionValidationData(nodeIds, year).map {
            expressionValidationMapper.mapProjection(it)
        }

    fun getExpressionValidationVariables(
        nodeIds: List<Long>,
        year: Int,
        codes: List<String>
    ): List<String> = advEntityRepository.findAllExpressionValidationVariables(nodeIds, year, codes)

    override fun viewToDto(
        entity: VMaterialNodeAnalysisParamTranslated,
        translated: Boolean
    ): MaterialNodeAnalysisParamDto {
        TODO("Not yet implemented")
    }

    override fun viewToDetailDto(
        entity: VMaterialNodeAnalysisParamTranslated,
        translated: Boolean
    ): MaterialNodeAnalysisParamDto {
        TODO("Not yet implemented")
    }

    override fun createEntity(createDto: MaterialNodeAnalysisParamDto): MaterialNodeAnalysisParam {
        TODO("Not yet implemented")
    }

    override fun updateEntity(
        updateDto: MaterialNodeAnalysisParamDto,
        entity: MaterialNodeAnalysisParam
    ) {
        TODO("Not yet implemented")
    }

    fun getAllByMaterialNodeId(materialNodeId: Long): MutableSet<MaterialNodeAnalysisParam> {
        return entityRepository.findAllByMaterialNodeId(materialNodeId)
    }
}
