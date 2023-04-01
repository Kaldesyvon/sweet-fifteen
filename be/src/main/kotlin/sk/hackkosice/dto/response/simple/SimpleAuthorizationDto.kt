package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

class SimpleAuthorizationDto {
    @Schema(description = "id", nullable = false) var id: Long? = null

    @Schema(description = "role", nullable = false) var role: SimpleRoleDto? = null

    @Schema(description = "node", nullable = false) var node: SimpleNodeDto? = null

    @Schema(description = "objVersion") var objVersion: Long? = 0
}
