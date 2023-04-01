package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeDto

class QuantityMonthDto {

    @Schema(description = "id") var id: String? = null

    @Schema(description = "node") var node: SimpleNodeDto? = null
}
