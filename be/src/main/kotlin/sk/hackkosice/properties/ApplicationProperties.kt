package sk.esten.uss.gbco2.properties

import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@Validated
@ConfigurationProperties(prefix = "application")
data class ApplicationProperties(

    /** Version of application */
    @field:NotBlank var version: String? = null,

    /** Build date of application */
    @field:NotNull
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var buildTime: LocalDateTime? = null,

    /** Allowed CORS origins to be allowed to request the app */
    var allowedCorsOrigins: List<@NotBlank String?> = listOf()
)
