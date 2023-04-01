package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisParamDto

@Schema(description = "analysisParamExprDto")
class AnalysisParamExprDto {

    @Schema(description = "id") var id: Long? = null

    @Schema(description = "expression", nullable = false, maxLength = 2000) var expr: String? = null

    @Schema(description = "analysisParam") var analysisParam: SimpleAnalysisParamDto? = null

    @Schema(description = "objVersion", nullable = false, defaultValue = "0")
    var objVersion: Long? = 0
}
