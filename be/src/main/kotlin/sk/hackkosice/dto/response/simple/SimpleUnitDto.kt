package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleUnitDto")
open class SimpleUnitDto : BaseSimpleDto() {
    @Schema(description = "code") var code: String? = null

    @Schema(description = "abbr") var abbr: String? = null

    @Schema(description = "unitTypeId") var unitTypeId: Long? = null
}
