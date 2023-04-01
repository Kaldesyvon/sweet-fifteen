package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleRegionDto")
open class SimpleRegionDto : BaseSimpleDto() {
    @Schema(description = "memo") var memo: String? = null

    @Schema(description = "reportPos") var reportPos: Long? = null
}
