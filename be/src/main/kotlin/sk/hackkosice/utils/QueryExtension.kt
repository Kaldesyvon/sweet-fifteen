package sk.esten.uss.gbco2.utils

import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDateTime
import javax.persistence.Query
import javax.persistence.Tuple

fun Tuple.getBigDecimal(key: String): BigDecimal? = this.get(key, BigDecimal::class.java) ?: null

fun Tuple.getString(key: String): String? = this.get(key, String::class.java) ?: null

fun Tuple.getLong(key: String): Long? = this.get(key, Long::class.java) ?: null

fun Tuple.getLocalDateTime(key: String): LocalDateTime? {
    val timestamp = this.get(key, Timestamp::class.java) ?: null
    return timestamp?.toLocalDateTime()
}

fun Query.setParameters(params: MutableMap<String, Any?>): Query {
    for ((key, value) in params) {
        this.setParameter(key, value)
    }
    return this
}
