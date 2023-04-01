package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisParamDto

@Schema(description = "quantityAnalysisParamDto")
class QuantityAnalysisParamDto {

    @Schema(description = "simpleAnalysisParam") var analysisParam: SimpleAnalysisParamDto? = null

    @Schema(description = "simpleAnalysis") var analysis: SimpleAnalysisDto? = null

    @Schema(description = "memo") var memo: String? = null
}
