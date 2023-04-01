package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleStatePostCodeDto")
open class SimpleStatePostCodeDto {
    @Schema(description = "id") var id: Long? = null

    @Schema(description = "postalCode") var postalCode: String? = null

    @Schema(description = "state") var state: String? = null
}
