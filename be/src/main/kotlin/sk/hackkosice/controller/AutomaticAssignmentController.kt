package sk.esten.uss.gbco2.controller

import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sk.esten.uss.gbco2.metrics.TimeExecution
import sk.esten.uss.gbco2.service.AutomaticAssignmentService

@RestController
@RequestMapping("/automatic-assignment")
class AutomaticAssignmentController(
    private val automaticAssignmentService: AutomaticAssignmentService
) {
    @TimeExecution
    @Operation(summary = "automatically assign analysis")
    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun runAutomaticAssignment(
        @RequestParam nodeId: Long,
        @RequestParam year: Int,
        @RequestParam materialId: Long
    ): ResponseEntity<HttpStatus> {
        val isTaskCreated =
            withContext(Dispatchers.IO) {
                automaticAssignmentService.runAAFromEP(nodeId, year, materialId)
            }
        return ResponseEntity(if (isTaskCreated.isActive) HttpStatus.OK else HttpStatus.BAD_REQUEST)
    }
}
