package sk.esten.uss.gbco2.dto.response.tree

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.MaterialDto

@Schema(description = "materialTreeDto")
class MaterialTreeDto : MaterialDto() {
    /** attribute name 'children' !! important */
    @Schema(description = "children") var children: List<MaterialTreeDto>? = mutableListOf()
}
