package sk.esten.uss.gbco2.dto.response.wi.analysys

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import sk.esten.uss.gbco2.dto.response.detail.WiAnalysisDetailDto
import sk.esten.uss.gbco2.dto.response.simple.*

@Schema(subTypes = [WiAnalysisDetailDto::class], description = "wiAnalysisDto")
open class WiAnalysisDto : BaseSimpleDto() {
    @Schema(description = "scope") var scope: SimpleScopeDto? = null
    @Schema(description = "analysisParam") var analysisParam: SimpleAnalysisParamDto? = null
    @Schema(description = "materialProduced") var materialProduced: SimpleMaterialDto? = null
    @Schema(description = "createdBy") var createdBy: String? = null
    @Schema(description = "created") var created: LocalDateTime? = null
    @Schema(description = "nodes") var nodes: Set<SimpleNodeDto> = setOf()
    @Schema(description = "objVersion", nullable = false, defaultValue = "0")
    var objVersion: Long? = 0
}
