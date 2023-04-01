package sk.esten.uss.gbco2.generics.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import java.io.Serializable
import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.generics.model.IdEntity
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.metrics.TimeExecution

interface CreateCrudController<
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
    @Operation(summary = "create entity")
    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponse(description = "Created", responseCode = "201")
    fun createEntity(@Valid @RequestBody createDto: CREATE_DTO): ResponseEntity<DTO> {
        return ResponseEntity(crudService.create(createDto), HttpStatus.CREATED)
    }
}
