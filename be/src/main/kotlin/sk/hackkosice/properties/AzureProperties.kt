package sk.esten.uss.gbco2.properties

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@Validated
@ConfigurationProperties(prefix = "azure.activedirectory")
data class AzureProperties(

    /** Url to authorize endpoint of OAuth2 server */
    @field:NotBlank var authorizeUrl: String? = null,

    /** Url to token endpoint of OAuth2 server */
    @field:NotBlank var tokenUrl: String? = null,

    /** Application (client) ID */
    @field:NotBlank var clientId: String? = null,

    /** Client secret */
    @field:NotBlank var clientSecret: String? = null,

    /** Directory (tenant) ID */
    @field:NotBlank var tenantId: String? = null,

    /** Scope to be used in authorize and token requests */
    @field:NotEmpty var scope: List<@NotBlank String?> = listOf()
)
