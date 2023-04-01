package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleNodeTypeDto")
open class SimpleNodeTypeDto : BaseSimpleDto() {
    @Schema(description = "simpleNodeLevelDto") var nodeLevel: SimpleNodeLevelDto? = null

    @Schema(description = "memo") var memo: String? = null

    @Schema(description = "reportPos") var reportPos: Long? = null

    @Schema(description = "code") var code: String? = null
}
