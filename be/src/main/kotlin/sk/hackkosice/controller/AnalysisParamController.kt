package sk.esten.uss.gbco2.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.esten.uss.gbco2.dto.request.ExpressionValidationDto
import sk.esten.uss.gbco2.dto.request.create.CreateAnalysisParamDto
import sk.esten.uss.gbco2.dto.request.filter.AnalysisParamFilter
import sk.esten.uss.gbco2.dto.request.filter.AnalysisParamReadAllFilter
import sk.esten.uss.gbco2.dto.request.update.UpdateAnalysisParamDto
import sk.esten.uss.gbco2.dto.response.AnalysisParamDto
import sk.esten.uss.gbco2.dto.response.detail.AnalysisParamDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisParamDto
import sk.esten.uss.gbco2.generics.controller.*
import sk.esten.uss.gbco2.mapper.AnalysisParamMapper
import sk.esten.uss.gbco2.metrics.TimeExecution
import sk.esten.uss.gbco2.model.entity.analysis_param.AnalysisParam
import sk.esten.uss.gbco2.model.entity.analysis_param.VAnalysisParamTranslated
import sk.esten.uss.gbco2.service.AnalysisParamService

@RestController
@RequestMapping(path = ["/analysis-param"])
class AnalysisParamController(
    override val crudService: AnalysisParamService,
    val mapper: AnalysisParamMapper
) :
    PageableCrudControllerView<
        AnalysisParam,
        VAnalysisParamTranslated,
        AnalysisParamDto,
        AnalysisParamDetailDto,
        CreateAnalysisParamDto,
        UpdateAnalysisParamDto,
        Long,
        AnalysisParamFilter,
        AnalysisParamReadAllFilter>,
    ReadAllCrudControllerView<
        AnalysisParam,
        VAnalysisParamTranslated,
        AnalysisParamDto,
        SimpleAnalysisParamDto,
        AnalysisParamDetailDto,
        CreateAnalysisParamDto,
        UpdateAnalysisParamDto,
        Long,
        AnalysisParamFilter,
        AnalysisParamReadAllFilter>,
    ReadDetailCrudControllerView<
        AnalysisParam,
        VAnalysisParamTranslated,
        AnalysisParamDto,
        AnalysisParamDetailDto,
        CreateAnalysisParamDto,
        UpdateAnalysisParamDto,
        Long,
        AnalysisParamFilter,
        AnalysisParamReadAllFilter>,
    UpdateCrudControllerView<
        AnalysisParam,
        VAnalysisParamTranslated,
        AnalysisParamDto,
        AnalysisParamDetailDto,
        CreateAnalysisParamDto,
        UpdateAnalysisParamDto,
        Long,
        AnalysisParamFilter,
        AnalysisParamReadAllFilter>,
    CreateCrudControllerView<
        AnalysisParam,
        VAnalysisParamTranslated,
        AnalysisParamDto,
        AnalysisParamDetailDto,
        CreateAnalysisParamDto,
        UpdateAnalysisParamDto,
        Long,
        AnalysisParamFilter,
        AnalysisParamReadAllFilter>,
    DeleteCrudControllerView<
        AnalysisParam,
        VAnalysisParamTranslated,
        AnalysisParamDto,
        AnalysisParamDetailDto,
        CreateAnalysisParamDto,
        UpdateAnalysisParamDto,
        Long,
        AnalysisParamFilter,
        AnalysisParamReadAllFilter> {

    @TimeExecution
    @Operation(summary = "validate analysis param expression")
    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE], path = ["/expression-validation"])
    @ApiResponse(description = "OK", responseCode = "200")
    fun validateAnalysisParamExpression(
        @Valid @RequestBody expressionValidationDto: ExpressionValidationDto
    ): ResponseEntity<Void> {
        crudService.validateAnalysisParamExpression(expressionValidationDto.analysisParamExpression)
        return ResponseEntity(HttpStatus.OK)
    }

    @TimeExecution
    @Operation(summary = "get all analysis params from material node ap table")
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE], path = ["/all/material-node-ap"])
    @ApiResponse(description = "OK", responseCode = "200")
    fun getAllAnalysisParamFromMaterialNodeAP(
        @Valid filter: AnalysisParamReadAllFilter
    ): List<SimpleAnalysisParamDto> {
        return crudService.getAllFromMaterialNodeAP(filter)
    }

    override fun entityToSimpleDto(
        entity: VAnalysisParamTranslated,
        translated: Boolean
    ): SimpleAnalysisParamDto {
        return if (translated) mapper.mapSimple(entity) else mapper.mapSimpleEn(entity)
    }
}
