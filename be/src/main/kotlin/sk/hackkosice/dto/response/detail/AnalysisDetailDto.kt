package sk.esten.uss.gbco2.dto.response.detail

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.AnalysisDto
import sk.esten.uss.gbco2.dto.response.quantity.QuantityDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialConversionDto

@Schema(description = "AnalysisDetailDto")
class AnalysisDetailDto : AnalysisDto() {

    @Schema(description = "calcExpression") var calcExpr: String? = null

    @Schema(description = "numberOfMeasurements") var numberOfMeasurements: Int? = null
    @Schema(description = "frequencyOfMeasurements") var frequencyOfMeasurements: String? = null

    @Schema(
        description =
            "unitAnalysisFormatAbbr - if null, then unitAnalysisFormatAbbr should be in nominator / denominator format"
    )
    var unitAnalysisFormatAbbr: String? = null

    @Schema(description = "calculated") var calculated: Boolean? = null

    @Schema(
        description = "simpleMaterialConversionDto - material source code / remote material code"
    )
    var materialConversion: SimpleMaterialConversionDto? = null

    @Schema(description = "formulaCalculationInputAnalyses")
    var formulaCalculationInputAnalyses: MutableSet<AnalysisDto>? = mutableSetOf()

    @Schema(description = "quantities") var quantities: MutableSet<QuantityDto>? = mutableSetOf()
}
