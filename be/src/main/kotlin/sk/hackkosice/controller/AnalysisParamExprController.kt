package sk.esten.uss.gbco2.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.AnalysisParamExprFilter
import sk.esten.uss.gbco2.dto.response.AnalysisParamExprDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisParamExprDto
import sk.esten.uss.gbco2.generics.controller.ReadAllCrudControllerView
import sk.esten.uss.gbco2.mapper.AnalysisParamExprMapper
import sk.esten.uss.gbco2.model.entity.analysis_param_expr.AnalysisParamExpr
import sk.esten.uss.gbco2.model.entity.analysis_param_expr.VAnalysisParamExprTranslated
import sk.esten.uss.gbco2.service.AnalysisParamExprService

@RestController
@RequestMapping(path = ["/analysis-param-expr"])
class AnalysisParamExprController(
    override val crudService: AnalysisParamExprService,
    private val mapper: AnalysisParamExprMapper
) :
    ReadAllCrudControllerView<
        AnalysisParamExpr,
        VAnalysisParamExprTranslated,
        AnalysisParamExprDto,
        SimpleAnalysisParamExprDto,
        AnalysisParamExprDto,
        AnalysisParamExprDto,
        AnalysisParamExprDto,
        Long,
        PageableParamsFilterDto,
        AnalysisParamExprFilter> {

    override fun entityToSimpleDto(
        entity: VAnalysisParamExprTranslated,
        translated: Boolean
    ): SimpleAnalysisParamExprDto = mapper.mapSimple(entity)
}
