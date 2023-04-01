package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.AnalysisParamExprFilter
import sk.esten.uss.gbco2.dto.response.AnalysisParamExprDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.AnalysisParamExprMapper
import sk.esten.uss.gbco2.model.entity.analysis_param_expr.AnalysisParamExpr
import sk.esten.uss.gbco2.model.entity.analysis_param_expr.VAnalysisParamExprTranslated
import sk.esten.uss.gbco2.model.repository.analysis_param_expr.AnalysisParamExprERepository
import sk.esten.uss.gbco2.model.repository.analysis_param_expr.AnalysisParamExprVRepository
import sk.esten.uss.gbco2.model.specification.AnalysisParamExprSpecification

@Service
class AnalysisParamExprService(
    override val entityRepository: AnalysisParamExprERepository,
    override val viewRepository: AnalysisParamExprVRepository,
    private val mapper: AnalysisParamExprMapper,
) :
    CrudServiceView<
        AnalysisParamExpr,
        VAnalysisParamExprTranslated,
        AnalysisParamExprDto,
        AnalysisParamExprDto,
        AnalysisParamExprDto,
        AnalysisParamExprDto,
        Long,
        PageableParamsFilterDto,
        AnalysisParamExprFilter>() {
    override fun viewToDto(
        entity: VAnalysisParamExprTranslated,
        translated: Boolean
    ): AnalysisParamExprDto = if (translated) mapper.map(entity) else mapper.mapEn(entity)

    override fun viewToDetailDto(
        entity: VAnalysisParamExprTranslated,
        translated: Boolean
    ): AnalysisParamExprDto = viewToDto(entity, translated)

    override fun createEntity(createDto: AnalysisParamExprDto): AnalysisParamExpr {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun updateEntity(updateDto: AnalysisParamExprDto, entity: AnalysisParamExpr) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    override fun repositoryGetAllQuery(
        filter: AnalysisParamExprFilter?
    ): List<VAnalysisParamExprTranslated> {
        return filter?.let {
            viewRepository.findAll(AnalysisParamExprSpecification(it), it.toSort())
        }
            ?: throw IllegalStateException("AnalysisParamExprFilter can not be null")
    }
}
