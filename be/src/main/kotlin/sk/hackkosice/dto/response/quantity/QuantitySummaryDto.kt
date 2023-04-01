package sk.esten.uss.gbco2.dto.response.quantity

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "quantitySummaryDto")
class QuantitySummaryDto(
    @Schema(description = "total quantities - list of summed quantity with corresponding unit")
    var totalQuantities: List<TotalQuantityDto>?,
    @Schema(
        description =
            "analysesAssigned - count of assigned analyses to the quantities for the selected analysis param"
    )
    var analysesAssigned: Int,
    @Schema(
        description =
            "analysesMissing - count of missing analyses to the quantities for the selected analysis param"
    )
    var analysesMissing: Int,
    @Schema(
        description =
            "analysesNA - count of NA analyses to the quantities for the selected analysis param"
    )
    var analysesNA: Int,
    @Schema(description = "countMdl") var countMdl: Long,
    @Schema(description = "totalInput - total input quantity for selected analysis param")
    var totalInput: BigDecimal?,
    @Schema(description = "totalOutput - total output quantity for selected analysis param")
    var totalOutput: BigDecimal?,
    @Schema(description = "totalFactorA - for the selected analysis param")
    var totalFactorA: BigDecimal?,
    @Schema(description = "totalFactorB - for the selected analysis param")
    var totalFactorB: BigDecimal?,
    @Schema(description = "totalFactorC - for the selected analysis param")
    var totalFactorC: BigDecimal?,
    @Schema(
        description =
            "unit of analysis param selected in the filter -> common for: totalInput, totalOutput, totalFactorA/B/C"
    )
    var unit: String?,
)
