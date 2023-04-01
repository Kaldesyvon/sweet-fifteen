package sk.esten.uss.gbco2.generics.controller

import io.swagger.v3.oas.annotations.Operation
import java.io.Serializable
import javax.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.generics.model.IdEntity
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.metrics.TimeExecution

interface PageableCrudController<
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
    @Operation(summary = "get all entities paginated with filter")
    @GetMapping(value = ["/paginated"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllPaginated(@Valid filter: FILTER): ResponseEntity<Page<DTO>> {
        return ResponseEntity.ok().body(crudService.getPaginatedWithFilter(filter))
    }
}
