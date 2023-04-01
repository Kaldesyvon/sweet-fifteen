package sk.esten.uss.gbco2.service.exports.data

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import javax.persistence.Tuple

class SummationResult() {

    @Schema(description = "id - id of item which sum is calculated for") var id: String? = null

    @Schema(description = "quantity - sum of quantity") var value: BigDecimal? = null

    @Schema(description = "unit_abbr - unit_abbr of quantity") var unitAbbr: String? = null

    constructor(tuple: Tuple) : this() {
        id = tuple.get("id", String::class.java)
        value = tuple.get("value", BigDecimal::class.java)
        if (tuple.elements.size == 3) {
            unitAbbr = tuple.get("unitAbbr", String::class.java)
        }
    }
}
