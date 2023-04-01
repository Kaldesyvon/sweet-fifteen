package sk.esten.uss.gbco2.generics.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import java.io.Serializable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.generics.model.IdEntity
import sk.esten.uss.gbco2.generics.model.IdViewEntity
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.metrics.TimeExecution

interface ReadDetailCrudControllerView<
    ENTITY : IdEntity<ID>,
    VIEW : IdViewEntity<ID>,
    DTO : Any,
    DETAIL_DTO : DTO,
    CREATE_DTO : Any,
    UPDATE_DTO : Any,
    ID : Serializable,
    FILTER : PageableParamsFilterDto,
    FILTER_ALL : ReadAllParamsFilterDto> {

    val crudService:
        CrudServiceView<
            ENTITY, VIEW, DTO, DETAIL_DTO, CREATE_DTO, UPDATE_DTO, ID, FILTER, FILTER_ALL>

    @TimeExecution
    @Operation(summary = "entity detail by ID")
    @GetMapping(value = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(
        @PathVariable id: ID,
        @Parameter(
            description = "If `false` returns fields in English language, otherwise user's language"
        )
        @RequestParam(required = false, defaultValue = "true")
        translated: Boolean = true
    ): ResponseEntity<DETAIL_DTO> {
        return ResponseEntity.ok().body(crudService.getById(id, translated))
    }
}
