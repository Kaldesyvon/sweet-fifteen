package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import sk.esten.uss.gbco2.dto.response.simple.*

@Schema(
    description = "advDetailDto",
)
class AdvDetailDto {
    @Schema(description = "id") var id: Long? = null
    @Schema(description = "status") var status: AdvStatusDto? = null
    @Schema(description = "markedAsValid") var markedAsValid: LocalDateTime? = null
    @Schema(description = "advValid") var advValid: Boolean? = null

    @Schema(description = "valIntensityMin") var valIntensityMin: BigDecimal? = null
    @Schema(description = "valIntensity") var valIntensity: BigDecimal? = null
    @Schema(description = "valIntensityMax") var valIntensityMax: BigDecimal? = null
    @Schema(description = "intensityMean") var intensityMean: BigDecimal? = null
    @Schema(description = "intensityStd") var intensityStd: BigDecimal? = null
    @Schema(description = "year/month") var month: LocalDate? = null
    @Schema(description = "node") var node: SimpleNodeDto? = null
    @Schema(description = "material") var material: SimpleMaterialDto? = null
    @Schema(description = "analysisParam") var analysisParam: SimpleAnalysisParamDto? = null

    @Schema(description = "quantity") var quantity: BigDecimal? = null
    @Schema(description = "unitAbbr") var unitAbbr: String? = null

    @Schema(description = "factorA") var factorA: BigDecimal? = null
    @Schema(description = "factorA") var factorB: BigDecimal? = null
    @Schema(description = "formattedUnitAbbr") var formattedUnitAbbr: String? = null
    @Schema(description = "basisMaterial") var basisMaterial: SimpleMaterialDto? = null
    @Schema(description = "advParams") var advParams: String? = null
}
