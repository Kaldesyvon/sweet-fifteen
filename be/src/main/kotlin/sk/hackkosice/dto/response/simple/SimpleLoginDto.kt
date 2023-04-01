package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "simpleLoginDto")
open class SimpleLoginDto {

    @Schema(description = "id") var id: Long? = null

    @Schema(description = "login") var login: String? = null

    @Schema(description = "loginAd") var loginAd: String? = null

    @Schema(description = "loginDate") var loginDate: LocalDateTime? = null
}
