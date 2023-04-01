package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "webserviceDataDto")
class WebserviceDataDto {

    @Schema(description = "dataReceived") var dataReceived: String? = null

    @Schema(description = "dataSent") var dataSent: String? = null
}
