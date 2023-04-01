@file:JvmName(name = "Extensions")

package sk.esten.uss.gbco2.utils

import com.nimbusds.jwt.JWTParser
import java.io.InputStream
import java.sql.Clob
import java.text.Normalizer
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import org.apache.commons.lang3.StringUtils
import org.apache.tika.Tika
import org.hibernate.jpa.TypedParameterValue
import org.hibernate.type.StandardBasicTypes
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import sk.esten.uss.gbco2.config.jwt.PrincipalUser
import sk.esten.uss.gbco2.dto.response.AccessTokenDto
import sk.esten.uss.gbco2.dto.response.simple.BaseSimpleDto
import sk.esten.uss.gbco2.exceptions.NotFoundException
import sk.esten.uss.gbco2.exceptions.ValidationException
import sk.esten.uss.gbco2.model.entity.language.Language
import sk.esten.uss.gbco2.model.entity.role.Role

fun <K : Any, V : Any> multiValueMapOf(vararg pairs: Pair<K, V>): MultiValueMap<K, V> {
    return LinkedMultiValueMap<K, V>().apply { pairs.forEach { add(it.first, it.second) } }
}

private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()

fun String.removeDiacritics(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_UNACCENT.replace(temp, "")
}

/** get principal object from Security context */
fun principal(): PrincipalUser? {
    return SecurityContextHolder.getContext()
        ?.authentication
        ?.takeIf { authentication -> authentication.isAuthenticated }
        ?.principal as
        PrincipalUser?
}

/** get fullname of principal */
fun principalFullName(): String {
    return "${principal()?.name} ${principal()?.surname}"
}

/** get language of principal or default (EN) */
fun principalLangOrEn(): Language {
    return Language().apply { id = principal()?.languageId ?: Constants.EN_LANG_ID }
}

/** get logged user's unit set id of principal or default (Metric) */
fun principalUnitSetIdOrMetricId(): Long {
    return principal()?.unitSetId ?: Constants.METRIC_UNIT_SET_ID
}

/** get id of principal or null */
fun principalIdOrNull(): Long? {
    return principal()?.id
}

fun hasRole(role: Role): Boolean {
    return role.id?.let { hasRole(it) }?.or(false) == true
}

fun hasRole(role: String): Boolean {
    return principal()
        ?.authorizations
        ?.any { authorization -> authorization.roleCode == role }
        ?.or(false) == true
}

fun hasRole(roleId: Long): Boolean {
    return principal()
        ?.authorizations
        ?.any { authorization -> authorization.roleId == roleId }
        ?.or(false) == true
}

fun getUserRoleIds(): List<Long>? {
    return principal()
        ?.authorizations
        ?.mapNotNull { authorization -> authorization.roleId }
        ?.toList()
}

/** get username from AccessToken response */
fun ResponseEntity<AccessTokenDto>.username(): String {
    val jwt = JWTParser.parse(body?.accessToken)
    return jwt.jwtClaimsSet.getStringClaim("preferred_username")
        ?: jwt.jwtClaimsSet.getStringClaim("unique_name")
}

fun getDateAtEndOfMonth(year: Int, month: Int?): LocalDate =
    YearMonth.of(year, month ?: 12).atEndOfMonth()

fun LocalDate?.formatDateToMonthWithUserLocale(): String? {
    return this?.format(
        DateTimeFormatter.ofPattern("M/yyyy").withZone(principal()?.currentTimeZone)
    )
}

fun formatDateForExport(dateTime: LocalDateTime?): String? {
    return dateTime?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"))
}

fun LocalDateTime?.format(pattern: String): String? =
    this?.format(DateTimeFormatter.ofPattern(pattern))

fun LocalDate.atEndOfDay(): LocalDateTime {
    return this.atTime(LocalTime.MAX)
}

fun Int.atStartOfYear(): LocalDateTime {
    return LocalDate.of(this, Month.JANUARY, 1).atStartOfDay()
}

fun Int.atEndOfYear(): LocalDateTime {
    return LocalDate.of(this, Month.DECEMBER, 31).atEndOfDay()
}

fun Clob?.stringValue(): String? {
    this?.let {
        return it.getSubString(1, Math.toIntExact(it.length()))
    }
    return null
}

fun String.getInputStream(classLoader: ClassLoader): InputStream {
    return classLoader.getResourceAsStream(this)
        ?: throw NotFoundException("File with path $this was not found!")
}

fun LocalDateTime?.toTypedParameterValue(): TypedParameterValue {
    return TypedParameterValue(StandardBasicTypes.TIMESTAMP, this.toUtilDate())
}

fun Long?.toTypedParameterValue(): TypedParameterValue {
    return TypedParameterValue(StandardBasicTypes.LONG, this)
}

fun LocalDateTime?.toUtilDate(): Date? {
    return this?.let { Date(it.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) }
}

fun <T : BaseSimpleDto> List<T>.sortBySelectionItemsOrder(sortingOrder: List<Long>): List<T> =
    this.sortedWith { a, b -> sortingOrder.indexOf(a.id).compareTo(sortingOrder.indexOf(b.id)) }
        .toList()

fun MutableList<Long>.appendValueIfNotExists(item: Long) {
    val list = this
    if (list.any { it == item }.not()) {
        list.add(item)
    }
}

fun <T> T?.getOrValidationException(description: String): T {
    return this ?: throw ValidationException(description = description)
}

inline fun <reified T> Any?.tryCast(): T {
    if (this is T) return this
    throw ClassCastException("Value cannot be cast")
}

fun String.trimSpaces(): String {
    return StringUtils.normalizeSpace(this)
}

fun String.detectContentType(): MediaType = MediaType.parseMediaType(this)

fun ByteArray.detectContentType(): MediaType = Tika().detect(this).detectContentType()
