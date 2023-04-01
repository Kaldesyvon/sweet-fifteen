package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleMaterialNodeDto")
open class SimpleMaterialNodeDto : BaseSimpleDto() {
    @Schema(description = "input") var input: Boolean? = false

    @Schema(description = "output") var output: Boolean? = false

    @Schema(description = "tier") var tier: Int? = null

    @Schema(description = "product") var product: Boolean? = null

    @Schema(description = "memo") var memo: String? = null
}
