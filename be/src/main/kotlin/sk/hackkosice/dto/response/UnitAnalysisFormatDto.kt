package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Size
import sk.esten.uss.gbco2.dto.response.simple.SimpleUnitAnalysisFormatDto

@Schema(description = "unitAnalysisFormatDto")
class UnitAnalysisFormatDto : SimpleUnitAnalysisFormatDto() {
    @Schema(description = "koef", nullable = false) var koef: BigDecimal? = null

    @Max(10)
    @Min(0)
    @Schema(description = "maxFractionDigits", nullable = false, maximum = "10", minimum = "0")
    var maxFractionDigits: Int? = 0

    @Size(max = 20)
    @Schema(description = "code", nullable = true, maxLength = 20)
    var code: String? = null

    @Schema(description = "objVersion", nullable = false, defaultValue = "0")
    var objVersion: Long? = 0
}
