package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDateTime
import sk.esten.uss.gbco2.dto.response.detail.AnalysisDetailDto
import sk.esten.uss.gbco2.dto.response.simple.*

@Schema(
    description = "analysisDto",
    subTypes = [AnalysisDetailDto::class]
) // this annotation tells swagger to generate AnalysisDetailDto schema
open class AnalysisDto : SimpleAnalysisDto() {

    @Schema(description = "factorA") var factorA: BigDecimal? = null
    @Schema(description = "factorB") var factorB: BigDecimal? = null
    @Schema(description = "factorC") var factorC: BigDecimal? = null

    @Schema(description = "editable") var editable: Boolean? = null
    @Schema(description = "fromWs") var fromWs: Boolean? = null

    @Schema(description = "formattedFactorC") var formattedFactorC: BigDecimal? = null
    @Schema(description = "uncertainty") var uncertainty: BigDecimal? = null

    @Schema(description = "objVersion") var objVersion: Long? = 0

    @Schema(description = "simpleAnalysisParam") var analysisParam: SimpleAnalysisParamDto? = null

    @Schema(description = "simpleUnitNumenator") var unitNumenator: SimpleUnitDto? = null

    @Schema(description = "simpleUnitDenominator") var unitDenominator: SimpleUnitDto? = null

    @Schema(description = "simpleMaterialNodeDto") var materialNode: SimpleMaterialNodeDto? = null

    @Schema(description = "simpleMaterialDto") var material: SimpleMaterialDto? = null

    @Schema(description = "simpleNodeDto") var node: SimpleNodeDto? = null

    @Schema(description = "mdlA") var mdlA: String? = null

    @Schema(description = "remoteCode") var remoteCode: String? = null

    @Schema(description = "memo") var memo: String? = null

    @Schema(description = "createdBy") var createdBy: String? = null

    @Schema(description = "created") var created: LocalDateTime? = null

    @Schema(description = "modifiedBy") var modifiedBy: String? = null

    @Schema(description = "modified") var modified: LocalDateTime? = null
}
