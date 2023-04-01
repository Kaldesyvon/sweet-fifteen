package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisParamDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisParamExprDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialNodeDto

@Schema(description = "materialNodeAnalysisParamDto")
class MaterialNodeAnalysisParamDto {
    @Schema(description = "id") var id: Long? = null

    @Schema(description = "objVersion") var objVersion: Long? = 0

    @Schema(description = "processAdv") var processAdv: Boolean? = null

    @Schema(description = "stdDeviation") var stdDeviation: BigDecimal? = null

    @Schema(description = "memo") var memo: String? = null

    @Schema(description = "analysisParam") var analysisParam: SimpleAnalysisParamDto? = null

    @Schema(description = "materialNode") var materialNode: SimpleMaterialNodeDto? = null

    @Schema(description = "analysisParamExpr")
    var analysisParamExpr: SimpleAnalysisParamExprDto? = null

    @Schema(description = "contentDeterminationMethod")
    var contentDeterminationMethod: String? = null

    @Schema(description = "materialNodeAdvBasis")
    var materialNodeAdvBasis: SimpleMaterialNodeDto? = null
}
