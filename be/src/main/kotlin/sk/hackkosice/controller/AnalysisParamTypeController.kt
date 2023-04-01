package sk.esten.uss.gbco2.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.AnalysisParamTypeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisParamTypeDto
import sk.esten.uss.gbco2.generics.controller.ReadAllCrudControllerView
import sk.esten.uss.gbco2.mapper.AnalysisParamTypeMapper
import sk.esten.uss.gbco2.model.entity.analysis_param_type.AnalysisParamType
import sk.esten.uss.gbco2.model.entity.analysis_param_type.VAnalysisParamTypeTranslated
import sk.esten.uss.gbco2.service.AnalysisParamTypeService

@RestController
@RequestMapping(path = ["/analysis-param-type"])
class AnalysisParamTypeController(
    override val crudService: AnalysisParamTypeService,
    val mapper: AnalysisParamTypeMapper
) :
    ReadAllCrudControllerView<
        AnalysisParamType,
        VAnalysisParamTypeTranslated,
        AnalysisParamTypeDto,
        SimpleAnalysisParamTypeDto,
        AnalysisParamTypeDto,
        AnalysisParamTypeDto,
        AnalysisParamTypeDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto> {

    override fun entityToSimpleDto(
        entity: VAnalysisParamTypeTranslated,
        translated: Boolean
    ): SimpleAnalysisParamTypeDto {
        return if (translated) mapper.mapSimple(entity) else mapper.mapSimpleEn(entity)
    }
}
