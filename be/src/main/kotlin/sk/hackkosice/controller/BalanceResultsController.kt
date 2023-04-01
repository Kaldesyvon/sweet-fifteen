package sk.esten.uss.gbco2.controller

import io.swagger.v3.oas.annotations.Operation
import javax.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sk.esten.uss.gbco2.dto.request.filter.CorpBalanceResultsFilterDto
import sk.esten.uss.gbco2.dto.response.BalanceResultDto
import sk.esten.uss.gbco2.metrics.TimeExecution
import sk.esten.uss.gbco2.service.BalanceResultsService

@RestController
@RequestMapping(path = ["/balance-results"])
class BalanceResultsController(
    private val balanceResultsService: BalanceResultsService,
) {
    @TimeExecution
    @Operation(summary = "get balance results for the node Kosice (1412)")
    @GetMapping(value = ["/kosice"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getKosiceBalanceResults(@RequestParam year: Int): ResponseEntity<List<BalanceResultDto>> {
        return ResponseEntity.ok().body(balanceResultsService.getKosiceBalanceResults(year))
    }

    @TimeExecution
    @Operation(summary = "get balance results by the filter specified")
    @GetMapping(value = ["/corporation"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getCorporationBalanceResults(
        @Valid filter: CorpBalanceResultsFilterDto
    ): ResponseEntity<List<BalanceResultDto>> {
        return ResponseEntity.ok().body(balanceResultsService.getCorporationBalanceResults(filter))
    }
}
