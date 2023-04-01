package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleUsepaMaterialTypeDto")
open class SimpleUsepaMaterialTypeDto : BaseSimpleDto() {
    @Schema(description = "code") var code: String? = null
}
