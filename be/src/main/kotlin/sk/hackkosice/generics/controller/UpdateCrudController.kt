package sk.esten.uss.gbco2.generics.controller

import io.swagger.v3.oas.annotations.Operation
import java.io.Serializable
import javax.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.generics.model.IdEntity
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.metrics.TimeExecution

interface UpdateCrudController<
    ENTITY : IdEntity<ID>,
    DTO : Any,
    DETAIL_DTO : DTO,
    CREATE_DTO : Any,
    UPDATE_DTO : Any,
    ID : Serializable,
    FILTER : PageableParamsFilterDto,
    FILTER_ALL : ReadAllParamsFilterDto> {

    val crudService:
        CrudService<ENTITY, DTO, DETAIL_DTO, CREATE_DTO, UPDATE_DTO, ID, FILTER, FILTER_ALL>

    @TimeExecution
    @Operation(summary = "update entity")
    @PutMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateEntity(
        @PathVariable id: ID,
        @Valid @RequestBody updateDto: UPDATE_DTO
    ): ResponseEntity<DTO> {
        return ResponseEntity.ok().body(crudService.update(id, updateDto))
    }
}
