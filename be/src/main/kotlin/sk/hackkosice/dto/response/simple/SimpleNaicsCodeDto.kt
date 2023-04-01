package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleNaicsCodeDto")
open class SimpleNaicsCodeDto {

    @Schema(description = "id") var id: Long? = null

    @Schema(description = "code") var code: String? = null
}
