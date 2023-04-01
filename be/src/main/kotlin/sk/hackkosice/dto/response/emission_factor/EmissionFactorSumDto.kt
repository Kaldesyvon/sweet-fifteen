package sk.esten.uss.gbco2.dto.response.emission_factor

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "emissionFactorSumDto - summary dto for currently filter applied")
class EmissionFactorSumDto() {
    @Schema(description = "carbon sum") var carbon: BigDecimal? = null

    @Schema(description = "co2 sum") var co2: BigDecimal? = null

    @Schema(description = "energy sum") var energy: BigDecimal? = null

    @Schema(description = "co2Energy sum") var co2Energy: BigDecimal? = null

    constructor(queryResultData: Array<*>?) : this() {
        carbon = queryResultData?.get(0) as BigDecimal
        co2 = queryResultData[1] as BigDecimal
        energy = queryResultData[2] as BigDecimal
        co2Energy = queryResultData[3] as BigDecimal
    }
}
