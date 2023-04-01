package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "webservicesDto")
class WebservicesDto {

    @Schema(description = "id") var id: Long? = null

    @Schema(description = "login") var login: String? = null

    @Schema(description = "called") var called: LocalDateTime? = null

    @Schema(description = "success") var success: Boolean? = false

    @Schema(description = "dataReceivedSize") var dataReceivedSize: Long? = null

    @Schema(description = "dataSentSize") var dataSentSize: Long? = null
}
