package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.detail.AnalysisParamDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisParamDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleUnitTypeDto

@Schema(subTypes = [AnalysisParamDetailDto::class], description = "analysisParamDto")
open class AnalysisParamDto : SimpleAnalysisParamDto() {

    @Schema(description = "unitType") var unitType: SimpleUnitTypeDto? = null

    @Schema(description = "analysisParamType") var analysisParamType: AnalysisParamTypeDto? = null

    @Schema(description = "unitAnalysisFormat")
    var unitAnalysisFormat: UnitAnalysisFormatDto? = null

    @Schema(description = "parent") var parent: SimpleAnalysisParamDto? = null

    @Schema(description = "objVersion") var objVersion: Long? = 0
}
