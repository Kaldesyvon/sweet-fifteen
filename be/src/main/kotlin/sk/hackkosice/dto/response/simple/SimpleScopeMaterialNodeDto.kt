package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleScopeMaterialNodeDto")
open class SimpleScopeMaterialNodeDto {
    @Schema(description = "id") var id: Long? = null

    @Schema(description = "factor A") var factorA: Boolean? = null

    @Schema(description = "factor B") var factorB: Boolean? = null

    @Schema(description = "factor C") var factorC: Boolean? = null

    @Schema(description = "useCalculation") var useCalculation: Boolean? = null
}
