package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "formulaCalculationDto")
class FormulaCalculationDto {
    @Schema(description = "analysisCalculated") var analysisCalculated: AnalysisDto? = null

    @Schema(description = "analysisInput") var analysisInput: AnalysisDto? = null

    @Schema(description = "expr") var expr: String? = null

    @Schema(description = "objVersion") var objVersion: Long? = 0
}
