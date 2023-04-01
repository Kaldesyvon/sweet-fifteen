package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Application data")
class AppVersionDto {
    @Schema(description = "Application version", example = "0.0.1") var version: String? = null
    @Schema(description = "Application build date time") var buildTime: LocalDateTime? = null
}
