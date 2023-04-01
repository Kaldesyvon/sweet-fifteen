package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "unitConversionDto")
class UnitConversionDto {

    @Schema(description = "unitTypeName") var unitTypeName: String? = null
    @Schema(description = "quantityFrom") var quantityFrom: BigDecimal? = null
    @Schema(description = "unitFromAbbr") var unitFromAbbr: String? = null
    @Schema(description = "quantityTo") var quantityTo: BigDecimal? = null
    @Schema(description = "unitToAbbr") var unitToAbbr: String? = null
}
