package sk.esten.uss.gbco2.dto.response.quantity

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import javax.persistence.Tuple
import sk.esten.uss.gbco2.utils.setScale

@Schema(description = "totalQuantityDto")
class TotalQuantityDto() {

    @Schema(description = "quantity - summed quantity grouped by unit")
    var quantity: BigDecimal? = null

    @Schema(description = "unit - unit of the quantity") var unit: String? = null

    constructor(resultTuple: Tuple) : this() {
        quantity = setScale(resultTuple.get(0, BigDecimal::class.java), 3)
        unit = resultTuple.get(1, String::class.java)
    }
}
