package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import sk.esten.uss.gbco2.dto.response.simple.SimpleLoginDto

@Schema(description = "loginDto")
class LoginDto : SimpleLoginDto() {

    @Schema(description = "logoutDate") var logoutDate: LocalDateTime? = null

    @Schema(description = "ip") var ip: String? = null

    @Schema(description = "success") var success: Boolean? = null
}
