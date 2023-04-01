package sk.esten.uss.gbco2.config.jwt

import java.time.ZoneId
import org.springframework.security.core.userdetails.User
import sk.esten.uss.gbco2.dto.PrincipalAuthorizationDto
import sk.esten.uss.gbco2.dto.PrincipalUserDto

/** Principal object fo GBCO2 application contains authorities, language and other settings */
class PrincipalUser(user: PrincipalUserDto) :
    User(user.login, "", user.authorizations.map { authorization -> authorization }.toList()) {
    val id: Long? = user.id
    val languageId: Long? = user.idLanguage
    val languageCode: String? = user.codeLanguage
    val authorizations: List<PrincipalAuthorizationDto> = user.authorizations
    val unitSetId: Long? = user.idUnitSet
    val name = user.name
    val surname = user.surname
    val loginAd = user.loginAd
    val currentTimeZone: ZoneId = ZoneId.of(user.timezone ?: ZoneId.systemDefault().id)
}
