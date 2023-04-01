package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleAnalysisParamExprDto")
open class SimpleAnalysisParamExprDto {
    @Schema(description = "id") var id: Long? = null

    @Schema(description = "expression") var expr: String? = null
}
