package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDateTime

@Schema(description = "simpleFormulaCalculationDto")
class SimpleFormulaCalculationDto {

    @Schema(description = "analysisCalculatedId") var analysisCalculatedId: Long? = null

    @Schema(description = "analysisInputId") var analysisInputId: Long? = null

    @Schema(description = "analysisInputMaterialNodeName")
    var analysisInputMaterialNodeName: String? = null

    @Schema(description = "analysisInputAnalysisParamName")
    var analysisInputAnalysisParamName: String? = null

    @Schema(description = "analysisInputFactorA") var analysisInputFactorA: BigDecimal? = null

    @Schema(description = "analysisInputFactorB") var analysisInputFactorB: BigDecimal? = null

    @Schema(description = "analysisInputFactorC") var analysisInputFactorC: BigDecimal? = null

    @Schema(description = "analysisInputValidFrom")
    var analysisInputValidFrom: LocalDateTime? = null

    @Schema(description = "expr") var expr: String? = null
}
