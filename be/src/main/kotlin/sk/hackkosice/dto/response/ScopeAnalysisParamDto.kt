package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.simple.SimpleScopeDto

@Schema(description = "scopeAnalysisParamDto")
class ScopeAnalysisParamDto {
    @Schema(description = "id") var id: Long? = null

    @Schema(description = "scope") var scope: SimpleScopeDto? = null

    @Schema(description = "analysisParam") var analysisParam: AnalysisParamDto? = null

    @Schema(description = "objVersion") var objVersion: Long? = 0
}
