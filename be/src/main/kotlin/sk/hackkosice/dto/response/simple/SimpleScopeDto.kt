package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleScopeDto")
open class SimpleScopeDto : BaseSimpleDto() {

    @Schema(description = "denominators count") var denominatorsCount: Int = 0
}
