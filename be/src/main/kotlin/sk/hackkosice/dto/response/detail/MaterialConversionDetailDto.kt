package sk.esten.uss.gbco2.dto.response.detail

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.MaterialConversionDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleUnitDto

@Schema(description = "MaterialConversionDetailDto")
class MaterialConversionDetailDto : MaterialConversionDto() {

    @Schema(description = "simpleNodeDto") var node: SimpleNodeDto? = null

    @Schema(description = "simpleUnitDto") var unit: SimpleUnitDto? = null
}
