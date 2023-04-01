package sk.esten.uss.gbco2.controller

import io.swagger.v3.oas.annotations.Operation
import javax.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.esten.uss.gbco2.dto.request.filter.EmissionFactorFilter
import sk.esten.uss.gbco2.dto.response.emission_factor.EmissionFactorDto
import sk.esten.uss.gbco2.metrics.TimeExecution
import sk.esten.uss.gbco2.service.EmissionFactorService

@RestController
@RequestMapping(path = ["/emission-factor"])
class EmissionFactorController(
    private val crudService: EmissionFactorService,
) {
    @TimeExecution
    @Operation(
        summary = "get all emission factors paginated filtered by filter with sum dto included"
    )
    @GetMapping(value = ["/paginated"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllPaginated(@Valid filter: EmissionFactorFilter): ResponseEntity<EmissionFactorDto> =
        ResponseEntity.ok().body(crudService.getEmissionFactors(filter))
}
