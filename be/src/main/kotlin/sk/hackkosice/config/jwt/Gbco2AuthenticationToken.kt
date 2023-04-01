package sk.esten.uss.gbco2.config.jwt

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt

class Gbco2AuthenticationToken(private val jwt: Jwt, private val user: PrincipalUser) :
    AbstractAuthenticationToken(user.authorities) {

    override fun getCredentials() = jwt // borrowed from JwtAuthenticationToken

    override fun getPrincipal() = user

    override fun isAuthenticated() = true // decoding of jwt is authentication
}
