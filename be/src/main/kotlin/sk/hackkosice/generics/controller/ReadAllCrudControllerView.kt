package sk.esten.uss.gbco2.generics.controller

import io.swagger.v3.oas.annotations.Operation
import java.io.Serializable
import javax.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.generics.model.IdEntity
import sk.esten.uss.gbco2.generics.model.IdViewEntity
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.metrics.TimeExecution

// DTO type is here because of correct CrudService injection
interface ReadAllCrudControllerView<
    ENTITY : IdEntity<ID>,
    VIEW : IdViewEntity<ID>,
    DTO : Any,
    SIMPLE_DTO : Any,
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
    @Operation(summary = "get all entities not-paginated")
    @GetMapping(value = ["/all"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll(@Valid filter: FILTER_ALL?): ResponseEntity<List<SIMPLE_DTO>> {
        return ResponseEntity.ok()
            .body(crudService.getAllWithFilter(filter).map { entityToSimpleDto(it) })
    }

    fun entityToSimpleDto(entity: VIEW, translated: Boolean = true): SIMPLE_DTO
}
