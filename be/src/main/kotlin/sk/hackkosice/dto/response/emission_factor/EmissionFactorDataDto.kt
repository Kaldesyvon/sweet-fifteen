package sk.esten.uss.gbco2.dto.response.emission_factor

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "emissionFactorDataDto - data dto page consist of")
class EmissionFactorDataDto() {

    @Schema(description = "name - for sorting, use `NAME`") var name: String? = null

    @Schema(description = "unitToAbbr - for sorting, use `UNIT_TO_ABBR`")
    var unitToAbbr: String? = null

    @Schema(description = "quantity - for sorting, use `QUANTITY`") var quantity: BigDecimal? = null

    @Schema(description = "energy - for sorting, use `ENERGY`") var energy: BigDecimal? = null

    @Schema(description = "carbon - for sorting, use `CARBON`") var carbon: BigDecimal? = null

    @Schema(description = "co2 - for sorting, use `CO2`") var co2: BigDecimal? = null

    @Schema(description = "co2Quantity - for sorting, use `CO2_QUANTITY`")
    var co2Quantity: BigDecimal? = null

    @Schema(description = "co2Energy - for sorting, use `CO2_ENERGY`")
    var co2Energy: BigDecimal? = null

    @Schema(description = "bcMaterialCode - for sorting, use `BC_MATERIAL_CODE`")
    var bcMaterialCode: String? = null

    constructor(queryResultData: Array<*>?) : this() {
        name = queryResultData?.get(0) as String
        unitToAbbr = queryResultData[1] as String
        quantity = queryResultData[2] as BigDecimal
        energy = queryResultData[3] as BigDecimal
        carbon = queryResultData[4] as BigDecimal
        co2 = queryResultData[5] as BigDecimal
        co2Quantity = queryResultData[6] as BigDecimal
        co2Energy = queryResultData[7] as BigDecimal
        if (queryResultData.size == 9) {
            bcMaterialCode = queryResultData[8] as String
        }
    }
}
