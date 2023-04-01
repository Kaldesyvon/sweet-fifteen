package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.model.entity.quantity.IOEnum

@Schema(description = "paretoResultDto")
class ParetoResultDto {
    @Schema(description = "id of the material") var materialId: Long? = null

    @Schema(description = "input/output") var io: IOEnum? = null

    @Schema(description = "sum of quantites - actual value to be plotted as the bar chart")
    var value: Double? = null

    @Schema(description = "cumulative sum of quantities - to be plotted as the line chart")
    var cumulativeValue: Double? = null

    @Schema(description = "percentage of the cumulative sum to the total sum of all entries")
    var ratio: Double? = null

    @Schema(description = "user's selected units") var units: String? = null

    @Schema(description = "description of the result") var label: String? = null
}
