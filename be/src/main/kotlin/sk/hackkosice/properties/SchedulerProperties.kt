package sk.esten.uss.gbco2.properties

import javax.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@Validated
@ConfigurationProperties(prefix = "application.scheduler")
data class SchedulerProperties(

    /** Cron expression for everyday scheduled job */
    @field:NotBlank var everyDayCron: String? = null,
)
