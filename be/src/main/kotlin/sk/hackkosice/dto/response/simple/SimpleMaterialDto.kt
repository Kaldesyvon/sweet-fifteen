package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleMaterialDto")
open class SimpleMaterialDto : BaseSimpleDto() {
    @Schema(description = "reportPos") var reportPos: Int? = null

    @Schema(description = "unitTypeId") var unitTypeId: Long? = null

    @Schema(description = "materialGroupId - parent material entity ID")
    var materialGroupId: Long? = null
}
