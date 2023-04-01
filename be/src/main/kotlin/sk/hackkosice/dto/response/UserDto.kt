package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.simple.*

@Schema(description = "userDto")
class UserDto : SimpleUserDto() {
    @Schema(description = "email") var email: String? = null

    @Schema(description = "node", nullable = true) var node: SimpleNodeDto? = null

    @Schema(
        description =
            "field for `function`, but renamed to `position` because of JS function keyword",
    )
    var position: String? = null

    @Schema(description = "phone") var phone: String? = null

    @Schema(description = "enabled", nullable = false) var enabled: Boolean? = false

    @Schema(description = "mailNotification") var mailNotification: Boolean? = true

    @Schema(description = "language", nullable = false) var languageUser: SimpleLanguageDto? = null

    @Schema(description = "unitSet", nullable = false) var unitSet: UnitSetDto? = null

    @Schema(description = "timezone") var timezone: String? = null

    @Schema(description = "defaultNode", nullable = false) var defaultNode: SimpleNodeDto? = null

    @Schema(description = "defaultScope", nullable = true) var defaultScope: SimpleScopeDto? = null

    @Schema(description = "defaultAnalysisParam", nullable = true)
    var defaultAnalysisParam: SimpleAnalysisParamDto? = null

    @Schema(description = "authorizations")
    var authorizations: List<SimpleAuthorizationDto>? = emptyList()
}
