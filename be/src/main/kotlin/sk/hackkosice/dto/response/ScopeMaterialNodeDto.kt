package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.simple.SimpleScopeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleScopeMaterialNodeDto

@Schema(description = "scopeMaterialNodeDto")
class ScopeMaterialNodeDto : SimpleScopeMaterialNodeDto() {
    @Schema(description = "scope") var scope: SimpleScopeDto? = null

    @Schema(description = "materialNode") var materialNode: MaterialNodeDto? = null

    @Schema(description = "objVersion") var objVersion: Long? = 0
}
