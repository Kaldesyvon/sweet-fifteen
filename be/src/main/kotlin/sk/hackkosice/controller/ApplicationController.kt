package sk.esten.uss.gbco2.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import sk.esten.uss.gbco2.dto.response.AppVersionDto
import sk.esten.uss.gbco2.metrics.TimeExecution
import sk.esten.uss.gbco2.properties.ApplicationProperties

@RestController
@Validated
@SecurityRequirements
class ApplicationController(val applicationProperties: ApplicationProperties) {

    @TimeExecution
    @GetMapping(path = ["/"])
    @Operation(summary = "Health check")
    fun healthCheck(): ResponseEntity<String> {
        return ResponseEntity.ok("OK")
    }

    @TimeExecution
    @GetMapping(path = ["/version"])
    @Operation(summary = "App version")
    fun appVersion(): ResponseEntity<AppVersionDto> {
        return ResponseEntity.ok(
            AppVersionDto().apply {
                version = applicationProperties.version
                buildTime = applicationProperties.buildTime
            }
        )
    }
}
