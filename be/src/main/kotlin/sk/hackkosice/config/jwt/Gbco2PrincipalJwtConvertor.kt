package sk.esten.uss.gbco2.config.jwt

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.jwt.Jwt
import sk.esten.uss.gbco2.service.UserService

class Gbco2PrincipalJwtConvertor(private val userService: UserService) :
    Converter<Jwt, Gbco2AuthenticationToken> {

    override fun convert(jwt: Jwt): Gbco2AuthenticationToken {
        val username =
            jwt.getClaimAsString("preferred_username") ?: jwt.getClaimAsString("unique_name")
        val user =
            userService.findUserPrincipalByLogin(username)
                ?: throw UsernameNotFoundException(
                    "User '$username' does not exist or is disabled!"
                )
        return Gbco2AuthenticationToken(jwt, PrincipalUser(user))
    }
}
