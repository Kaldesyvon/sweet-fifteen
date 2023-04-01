package sk.esten.uss.gbco2.dto.response.detail

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.AnalysisParamDto
import sk.esten.uss.gbco2.dto.response.UnitSetSettingsApDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisParamExprDto

@Schema(description = "analysisParamDetailDto")
class AnalysisParamDetailDto : AnalysisParamDto() {
    @Schema(description = "analysisParamExpressions")
    var analysisParamExpressions: MutableList<SimpleAnalysisParamExprDto> = mutableListOf()

    @Schema(description = "unitSetSettingsAps")
    var unitSetSettingsAps: MutableList<UnitSetSettingsApDto> = mutableListOf()
}
