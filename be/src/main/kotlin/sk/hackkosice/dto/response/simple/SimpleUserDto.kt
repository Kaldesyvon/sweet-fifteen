package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleUserDto")
open class SimpleUserDto {
    @Schema(description = "id", nullable = false) var id: Long? = null

    @Schema(description = "Microsoft SSO login - used in new app") var login: String? = null

    @Schema(description = "AD login - used in old app, & other systems") var loginAd: String? = null

    @Schema(description = "name") var name: String? = null

    @Schema(description = "surname") var surname: String? = null
}
