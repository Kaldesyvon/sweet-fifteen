package sk.esten.uss.gbco2.dto.response.tree

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.NodeDto

@Schema(description = "nodeTreeDto")
class NodeTreeDto : NodeDto() {
    /** attribute name 'children' !! important */
    @Schema(description = "children") var children: List<NodeTreeDto>? = mutableListOf()
}
