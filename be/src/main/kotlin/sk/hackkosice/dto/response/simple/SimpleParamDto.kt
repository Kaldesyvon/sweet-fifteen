package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleParamDto")
class SimpleParamDto {
    @Schema(description = "id") var id: Long? = null

    @Schema(description = "code") var code: String? = null

    @Schema(description = "value") var value: String? = null
}
