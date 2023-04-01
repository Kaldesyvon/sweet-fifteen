package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleNodeDto")
open class SimpleNodeDto : BaseSimpleDto() {
    @Schema(description = "nodeLevel") var nodeLevel: SimpleNodeLevelDto? = null

    @Schema(description = "processPosition") var processPosition: Long? = null

    @Schema(description = "reportPos") var reportPos: Long? = null

    @Schema(description = "regionId") open var regionId: Long? = null
}
