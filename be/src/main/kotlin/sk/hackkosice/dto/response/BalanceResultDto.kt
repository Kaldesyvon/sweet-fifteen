package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "balanceResultDto")
class BalanceResultDto {

    @Schema(description = "id") var id: String? = null

    @Schema(description = "month") var month: Int? = null

    @Schema(description = "year") var year: Int? = null

    @Schema(description = "CO2 emissions") var carbonSum: BigDecimal? = null

    @Schema(description = "CO2 input") var carbonInput: BigDecimal? = null

    @Schema(description = "CO2 output") var carbonOutput: BigDecimal? = null

    @Schema(description = "CO2 unit in the table header for CO2 columns")
    var carbonUnit: String? = null

    @Schema(description = "energy") var energySum: BigDecimal? = null

    @Schema(description = "energy input") var energyInput: BigDecimal? = null

    @Schema(description = "energy ouput") var energyOutput: BigDecimal? = null

    @Schema(description = "ENERGY unit in the table header for ENERGY columns")
    var energyUnit: String? = null

    @Schema(description = "uncertaintyInput - null in the corporation balance results")
    var uncertaintyInput: BigDecimal? = null

    @Schema(description = "uncertaintyOutput - null in the corporation balance results")
    var uncertaintyOutput: BigDecimal? = null

    @Schema(description = "unitSetId") var unitSetId: Long? = null
}
