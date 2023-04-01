package sk.esten.uss.gbco2.dto.response.detail

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.QuantityAnalysisParamDto
import sk.esten.uss.gbco2.dto.response.quantity.QuantityDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleUnitDto

@Schema(description = "quantityDetailDto")
class QuantityDetailDto : QuantityDto() {

    @Schema(description = "memo") var memo: String? = null

    @Schema(description = "analysisParams list - dto with `AnalysisParam` and `Analysis` assigned")
    var materialAnalysisParams: MutableList<QuantityAnalysisParamDto>? = mutableListOf()

    @Schema(description = "unit") var unit: SimpleUnitDto? = null

    @Schema(description = "remoteMaterialCodeId - ID to material conversion selected")
    var remoteMaterialCodeId: Long? = null
}
