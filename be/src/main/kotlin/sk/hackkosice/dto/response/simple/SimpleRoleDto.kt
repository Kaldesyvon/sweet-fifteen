package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleRoleDto")
open class SimpleRoleDto : BaseSimpleDto() {

    @Schema(description = "nodeRole") var nodeRole: Boolean? = true
}
