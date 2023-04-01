package sk.esten.uss.gbco2.service

import javax.servlet.http.HttpServletRequest
import org.slf4j.MDC
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.scheduler.Schedulers
import sk.esten.uss.gbco2.config.tracing.TraceIdFilter.Companion.TRACE_ID_KEY
import sk.esten.uss.gbco2.dto.response.AccessTokenDto
import sk.esten.uss.gbco2.exceptions.ForbiddenException
import sk.esten.uss.gbco2.properties.AzureProperties
import sk.esten.uss.gbco2.utils.multiValueMapOf
import sk.esten.uss.gbco2.utils.username

@Service
class SecurityService(
    val webClient: WebClient,
    val azureProperties: AzureProperties,
    val loginService: LoginService,
    val userService: UserService
) {

    /** build authorize url */
    fun getAuthorizeUrl(redirectUri: String, state: String?, req: HttpServletRequest?): String {
        val uribuilder =
            UriComponentsBuilder.fromUriString(azureProperties.authorizeUrl.orEmpty())
                .queryParam("response_type", "code")
                .queryParam("client_id", azureProperties.clientId)
                .queryParam("scope", azureProperties.scope.joinToString(" "))
                .queryParam("redirect_uri", redirectUri)
                .also { builder -> state?.let { builder.queryParam("state", it) } }
        return uribuilder.toUriString()
    }

    /** calling AD token endpoint to retrieve access token using authorization code */
    fun accessToken(
        redirectUri: String,
        code: String,
        req: HttpServletRequest?
    ): ResponseEntity<AccessTokenDto> {
        val body =
            multiValueMapOf(
                "grant_type" to "authorization_code",
                "code" to code,
                "client_id" to azureProperties.clientId.orEmpty(),
                "client_secret" to azureProperties.clientSecret.orEmpty(),
                "redirect_uri" to redirectUri,
            )

        return processRequest(body, req)
    }

    /** calling AD token endpoint to retrieve access token using refresh token */
    fun refreshToken(
        refreshToken: String,
        req: HttpServletRequest?
    ): ResponseEntity<AccessTokenDto> {
        val body =
            multiValueMapOf(
                "grant_type" to "refresh_token",
                "refresh_token" to refreshToken,
                "client_id" to azureProperties.clientId.orEmpty(),
                "client_secret" to azureProperties.clientSecret.orEmpty(),
                "scope" to azureProperties.scope.joinToString(" "),
            )
        return processRequest(body, req)
    }

    @Transactional
    fun processRequest(
        body: MultiValueMap<String, String>,
        req: HttpServletRequest?
    ): ResponseEntity<AccessTokenDto> {
        loginService.logAttemptLogin(req?.remoteAddr, req?.remotePort)
        val traceId = MDC.get(TRACE_ID_KEY)
        return webClient
            .post()
            .uri(azureProperties.tokenUrl.orEmpty())
            .bodyValue(body)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .retrieve()
            .toEntity(AccessTokenDto::class.java)
            .publishOn(Schedulers.boundedElastic())
            .doOnSuccess {
                MDC.put(TRACE_ID_KEY, traceId)
                val user = userService.findUserPrincipalByLogin(it.username())
                if (user == null) {
                    loginService.logLogin(it.username(), req?.remoteAddr, req?.remotePort, false)
                    throw ForbiddenException(
                        description = "[${it.username()}] GbcUser is disabled or not in database!"
                    )
                } else {
                    if (user.authorizations.isEmpty()) {
                        loginService.logLogin(
                            it.username(),
                            req?.remoteAddr,
                            req?.remotePort,
                            false
                        )
                        throw ForbiddenException(
                            description = "[${it.username()}] GbcUser doesn't have any role!"
                        )
                    } else {
                        loginService.logLogin(it.username(), req?.remoteAddr, req?.remotePort)
                    }
                }
                MDC.remove(TRACE_ID_KEY)
            }
            .blockOptional()
            .orElse(ResponseEntity.internalServerError().build())
    }
}
