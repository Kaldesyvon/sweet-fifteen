package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema

class ScopeDenominatorDto {

    @Schema(description = "id", nullable = false) var id: Long? = null

    @Schema(description = "objVersion", nullable = false, defaultValue = "0")
    var objVersion: Long? = 0

    @Schema(description = "scope", nullable = false) var scope: ScopeDto? = null

    @Schema(description = "material", nullable = true) var material: MaterialDto? = null
}
