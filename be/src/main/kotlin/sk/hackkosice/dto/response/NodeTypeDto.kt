package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeTypeDto

@Schema(description = "nodeTypeDto")
class NodeTypeDto : SimpleNodeTypeDto() {
    @Schema(description = "materialReport") var materialReport: SimpleMaterialDto? = null

    @Schema(description = "objVersion", nullable = false, defaultValue = "0")
    var objVersion: Long? = 0
}
