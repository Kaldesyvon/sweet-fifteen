package sk.esten.uss.gbco2.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.esten.uss.gbco2.dto.request.create.CreateAnalysisDto
import sk.esten.uss.gbco2.dto.request.filter.AnalysisFilter
import sk.esten.uss.gbco2.dto.request.filter.AnalysisReadAllFilter
import sk.esten.uss.gbco2.dto.request.update.UpdateAnalysisDto
import sk.esten.uss.gbco2.dto.response.AnalysisDto
import sk.esten.uss.gbco2.dto.response.detail.AnalysisDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisDto
import sk.esten.uss.gbco2.generics.controller.*
import sk.esten.uss.gbco2.mapper.AnalysisMapper
import sk.esten.uss.gbco2.model.entity.analysis.Analysis
import sk.esten.uss.gbco2.model.entity.analysis.VAnalysisTranslated
import sk.esten.uss.gbco2.service.AnalysisService

@RestController
@RequestMapping(path = ["/analysis"])
class AnalysisController(
    override val crudService: AnalysisService,
    private val mapper: AnalysisMapper,
) :
    PageableCrudControllerView<
        Analysis,
        VAnalysisTranslated,
        AnalysisDto,
        AnalysisDetailDto,
        CreateAnalysisDto,
        UpdateAnalysisDto,
        Long,
        AnalysisFilter,
        AnalysisReadAllFilter>,
    ReadDetailCrudControllerView<
        Analysis,
        VAnalysisTranslated,
        AnalysisDto,
        AnalysisDetailDto,
        CreateAnalysisDto,
        UpdateAnalysisDto,
        Long,
        AnalysisFilter,
        AnalysisReadAllFilter>,
    CreateCrudControllerView<
        Analysis,
        VAnalysisTranslated,
        AnalysisDto,
        AnalysisDetailDto,
        CreateAnalysisDto,
        UpdateAnalysisDto,
        Long,
        AnalysisFilter,
        AnalysisReadAllFilter>,
    UpdateCrudControllerView<
        Analysis,
        VAnalysisTranslated,
        AnalysisDto,
        AnalysisDetailDto,
        CreateAnalysisDto,
        UpdateAnalysisDto,
        Long,
        AnalysisFilter,
        AnalysisReadAllFilter>,
    DeleteCrudControllerView<
        Analysis,
        VAnalysisTranslated,
        AnalysisDto,
        AnalysisDetailDto,
        CreateAnalysisDto,
        UpdateAnalysisDto,
        Long,
        AnalysisFilter,
        AnalysisReadAllFilter>,
    ReadAllCrudControllerView<
        Analysis,
        VAnalysisTranslated,
        AnalysisDto,
        SimpleAnalysisDto,
        AnalysisDetailDto,
        CreateAnalysisDto,
        UpdateAnalysisDto,
        Long,
        AnalysisFilter,
        AnalysisReadAllFilter> {

    override fun entityToSimpleDto(
        entity: VAnalysisTranslated,
        translated: Boolean
    ): SimpleAnalysisDto = mapper.mapSimple(entity)
}
