package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleRoleDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleUserDto

class AuthorizationDto {
    @Schema(description = "id") var id: Long? = null

    @Schema(description = "user") var user: SimpleUserDto? = null

    @Schema(description = "role") var role: SimpleRoleDto? = null

    @Schema(description = "node") var node: SimpleNodeDto? = null

    @Schema(description = "objVersion") var objVersion: Long? = 0
}
