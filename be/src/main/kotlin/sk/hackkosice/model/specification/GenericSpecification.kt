package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.Path
import org.springframework.data.jpa.domain.Specification

interface GenericSpecification<T> : Specification<T> {

    /** if boolean is true do filter.equal(path), else null */
    fun <TYPE> booleanEqual(bool: Boolean?, filter: TYPE?, path: Path<TYPE>): Specification<T>? {
        return if (bool == true)
            return filter?.let {
                return Specification { _, _, builder -> builder.equal(path, it) }
            }
        else null
    }

    fun <TYPE> equal(filter: TYPE?, path: Path<TYPE>): Specification<T>? {
        return filter?.let {
            // empty string handling as a 'null' (do not filter)
            if (it is String && it.isBlank()) {
                return null
            }
            return Specification { _, _, builder -> builder.equal(path, it) }
        }
    }

    fun <TYPE> isNull(path: Path<TYPE>): Specification<T>? {
        return Specification { _, _, builder -> builder.isNull(path) }
    }

    fun <TYPE> isNotNull(path: Path<TYPE>): Specification<T>? {
        return Specification { _, _, builder -> builder.isNotNull(path) }
    }

    fun <TYPE> isIn(filter: List<TYPE>?, path: Path<TYPE>): Specification<T>? {
        return filter?.let {
            return Specification { _, _, _ -> path.`in`(it) }
        }
    }

    fun contains(
        filter: String?,
        path: Path<String>,
        ignoreAccent: Boolean = true
    ): Specification<T>? {
        if (filter?.isEmpty() == true) {
            return null
        }
        return if (!ignoreAccent)
            return filter?.let {
                return Specification { _, _, builder ->
                    builder.like(
                        builder.lower(path),
                        builder.lower(builder.literal("%${it.trim()}%"))
                    )
                }
            }
        else containsIgnoreAccent(filter, path)
    }

    fun containsIgnoreAccent(filter: String?, path: Path<String>): Specification<T>? {
        if (filter?.isEmpty() == true) {
            return null
        }
        return filter?.let {
            return Specification { _, _, builder ->
                val p = builder.literal("US7ASCII")
                builder.like(
                    builder.lower(builder.function("CONVERT", String::class.java, path, p)),
                    builder.lower(
                        builder.function(
                            "CONVERT",
                            String::class.java,
                            builder.literal("%${it.trim()}%"),
                            p
                        )
                    )
                )
            }
        }
    }

    fun <TYPE : Comparable<TYPE>> greaterThanOrEqualTo(
        filter: TYPE?,
        path: Path<TYPE>
    ): Specification<T>? {
        return filter?.let {
            return Specification { _, _, builder -> builder.greaterThanOrEqualTo(path, it) }
        }
    }

    fun <TYPE : Comparable<TYPE>> lessThanOrEqualTo(
        filter: TYPE?,
        path: Path<TYPE>
    ): Specification<T>? {
        return filter?.let {
            return Specification { _, _, builder -> builder.lessThanOrEqualTo(path, it) }
        }
    }
}
