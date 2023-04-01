package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.simple.SimpleRoleDto

@Schema(description = "roleDto")
class RoleDto : SimpleRoleDto() {

    @Schema(description = "code") var code: String? = null

    @Schema(description = "memo") var memo: String? = null
}
