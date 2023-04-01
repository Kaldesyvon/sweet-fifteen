package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleNodeTreeDto")
class SimpleNodeTreeDto : SimpleNodeDto() {
    @Schema(description = "children") var children: List<SimpleNodeTreeDto>? = mutableListOf()
    @Schema(description = "region") var region: SimpleRegionDto? = null
    @Schema(description = "parentNode") var parentNode: SimpleNodeDto? = null
}
