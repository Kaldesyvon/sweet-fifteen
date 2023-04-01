package sk.esten.uss.gbco2.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description = "Access token data",
)
class AccessTokenDto {
    @JsonProperty("token_type") var tokenType: String? = null
    @JsonProperty("scope") var scope: String? = null
    @JsonProperty("expires_in") var expiresIn: Long? = null
    @JsonProperty("ext_expires_in") var extExpiresIn: Long? = null
    @JsonProperty("access_token") var accessToken: String? = null
    @JsonProperty("refresh_token") var refreshToken: String? = null
}
