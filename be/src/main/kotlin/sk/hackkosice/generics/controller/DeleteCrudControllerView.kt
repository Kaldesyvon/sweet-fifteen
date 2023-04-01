package sk.esten.uss.gbco2.generics.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import java.io.Serializable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.generics.model.IdEntity
import sk.esten.uss.gbco2.generics.model.IdViewEntity
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.metrics.TimeExecution

interface DeleteCrudControllerView<
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
    @Operation(summary = "delete entity")
    @DeleteMapping(path = ["/{id}"])
    @ApiResponse(description = "No Content", responseCode = "204")
    fun deleteEntity(@PathVariable id: ID): ResponseEntity<HttpStatus> {
        crudService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
