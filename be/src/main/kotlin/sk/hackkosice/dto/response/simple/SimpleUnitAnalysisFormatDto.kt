package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleUnitAnalysisFormatDto")
open class SimpleUnitAnalysisFormatDto : BaseSimpleDto() {
    @Schema(description = "abbr") var abbr: String? = null
}
