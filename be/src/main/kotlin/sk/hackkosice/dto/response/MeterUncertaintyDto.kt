package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate
import sk.esten.uss.gbco2.dto.response.simple.SimpleUnitDto

@Schema(description = "meterUncertaintyDto")
class MeterUncertaintyDto {

    @Schema(description = "id") var id: Long? = null

    @Schema(description = "lowerRange") var lowerRange: BigDecimal? = null

    @Schema(description = "uncertainty") var uncertainty: BigDecimal? = null

    @Schema(description = "upperRange") var upperRange: BigDecimal? = null

    @Schema(description = "validFrom") var validFrom: LocalDate? = null

    @Schema(description = "simpleUnitDto") var unit: SimpleUnitDto? = null

    @Schema(description = "objVersion") var objVersion: Long = 0
}
