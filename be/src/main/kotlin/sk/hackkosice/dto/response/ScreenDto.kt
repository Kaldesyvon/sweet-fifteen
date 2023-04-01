package sk.esten.uss.gbco2.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.simple.SimpleRoleDto

@Schema(description = "screenDto")
class ScreenDto {

    @Schema(description = "code") var code: String? = null

    @Schema(description = "list of menu items which are ordered from root menu item ascending")
    var menuItemsOrder: List<String?> = mutableListOf()

    @Schema(description = "title") var title: String? = null

    @Schema(description = "list of roles, which are needed to access requested screen menu item")
    @JsonProperty("menuRoles")
    var roles: List<SimpleRoleDto>? = mutableListOf()

    @Schema(description = "helpLabel") var helpLabel: String? = null
}
