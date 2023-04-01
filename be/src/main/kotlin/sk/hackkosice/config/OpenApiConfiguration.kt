package sk.esten.uss.gbco2.config

import io.swagger.v3.core.converter.ModelConverters
import io.swagger.v3.oas.models.*
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.security.OAuthFlow
import io.swagger.v3.oas.models.security.OAuthFlows
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import java.util.function.Consumer
import org.springdoc.core.GroupedOpenApi
import org.springdoc.core.customizers.OpenApiCustomiser
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import sk.esten.uss.gbco2.dto.response.ErrorDto

@Configuration
class OpenApiConfiguration {

    private val oauthPath = "/oauth2/**"
    private val healthPath = "/"
    private val versionPath = "/version"
    private val operationIdRegex = Regex("_[0-9]+")

    @Bean
    fun customOpenAPI(
        @Value("\${springdoc.swagger-ui.oauth.endpoint-url:}") oauthEndpointUrl: String?,
        @Value("\${springdoc.swagger-ui.custom-server-path:/}") path: String
    ): OpenAPI {
        val components =
            Components()
                .addSecuritySchemes(
                    "bearerAuth",
                    SecurityScheme().apply {
                        name = "bearerAuth"
                        type = SecurityScheme.Type.HTTP
                        bearerFormat = "JWT"
                        scheme = "bearer"
                    }
                )
        val securityRequirements = SecurityRequirement().addList("bearerAuth")
        oauthEndpointUrl?.let {
            if (it.isNotBlank()) {
                securityRequirements.addList("OAUTH")
                components.addSecuritySchemes(
                    "OAUTH",
                    SecurityScheme().apply {
                        name = "OAUTH"
                        scheme = "bearer"
                        bearerFormat = "JWT"
                        type = SecurityScheme.Type.OAUTH2
                        flows =
                            OAuthFlows().apply {
                                authorizationCode =
                                    OAuthFlow().apply {
                                        authorizationUrl = "${oauthEndpointUrl}/oauth2/authorize"
                                        tokenUrl = "${oauthEndpointUrl}/oauth2/token"
                                    }
                            }
                    }
                )
            }
        }

        return OpenAPI()
            .info(
                Info().apply {
                    title = "GBCO2 API"
                    description = "Rest API documentation for GBCO2 application."
                    version = "1.0"
                    contact =
                        Contact().apply {
                            url = "https://esten.sk"
                            name = "Esten s.r.o."
                        }
                }
            )
            .components(components)
            .addSecurityItem(securityRequirements)
            .servers(listOf(Server().apply { url = path }))
    }

    @Bean
    fun applicationApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("application")
            .displayName("Application")
            .pathsToMatch(healthPath, versionPath)
            .build()
            .apply { openApiCustomisers.add(customiseDefaultResponses()) }
    }

    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("api")
            .displayName("Api")
            .pathsToMatch("/**")
            .pathsToExclude(oauthPath, healthPath, versionPath)
            .build()
            .apply { openApiCustomisers.add(customiseDefaultResponses()) }
    }

    @Bean
    fun loginApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("auth")
            .displayName("Oauth2")
            .pathsToMatch(oauthPath)
            .build()
            .apply { openApiCustomisers.add(customiseDefaultResponses()) }
    }

    @Bean
    fun customiseDefaultResponses(): OpenApiCustomiser {

        val errorResponseSchema: Schema<ErrorDto> = Schema<ErrorDto>()
        errorResponseSchema.`$ref` = "#/components/schemas/ErrorDto"
        return OpenApiCustomiser { openApi ->
            // sort alphabetically by path
            openApi.paths =
                Paths().apply {
                    openApi.paths.keys.sorted().forEach(Consumer { put(it, openApi.paths[it]) })
                }

            openApi.components.schemas.putAll(
                ModelConverters.getInstance().read(ErrorDto::class.java)
            )
            openApi.paths.values.forEach(
                Consumer { pathItem: PathItem ->
                    pathItem
                        .readOperations()
                        .forEach(
                            Consumer { operation: Operation ->
                                operation.operationId =
                                    operation.operationId.replace(
                                        regex = operationIdRegex,
                                        replacement = ""
                                    )

                                val apiResponses = operation.responses
                                apiResponses.addApiResponse(
                                    "400",
                                    createApiResponse("Business error", errorResponseSchema)
                                )
                                // (operation.security == null)
                                // because in OpenAPI is security enabled by default
                                // if is not null or empty then default is overridden see
                                // @SecurityRequirements
                                if (operation.security == null) {
                                    apiResponses.addApiResponse(
                                        "401",
                                        createApiResponse("Unauthorized", errorResponseSchema)
                                    )
                                }
                                apiResponses.addApiResponse(
                                    "500",
                                    createApiResponse("Technical error", errorResponseSchema)
                                )
                            }
                        )
                }
            )
        }
    }

    private fun createApiResponse(message: String, schema: Schema<ErrorDto>): ApiResponse {
        val mediaType = MediaType()
        mediaType.schema(schema)
        return ApiResponse()
            .description(message)
            .content(Content().addMediaType(APPLICATION_JSON_VALUE, mediaType))
    }
}
