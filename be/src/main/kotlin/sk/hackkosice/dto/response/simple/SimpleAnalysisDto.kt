package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDateTime

@Schema(description = "simpleAnalysisDto")
open class SimpleAnalysisDto {
    @Schema(description = "id") var id: Long? = null

    @Schema(description = "validFrom") var validFrom: LocalDateTime? = null

    @Schema(description = "formattedFactorA") var formattedFactorA: BigDecimal? = null

    @Schema(description = "formattedFactorB") var formattedFactorB: BigDecimal? = null

    @Schema(description = "formattedUnitAbbrTo") var formattedUnitAbbrTo: String? = null
}
