package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "scopeMaterialDataDto")
class ScopeMaterialDataDto {
    @Schema(description = "factorA", nullable = true) var factorA: Boolean? = null

    @Schema(description = "factorB", nullable = true) var factorB: Boolean? = null

    @Schema(description = "factorC", nullable = true) var factorC: Boolean? = null

    @Schema(description = "materialId", nullable = true) var materialId: Long? = null
}
