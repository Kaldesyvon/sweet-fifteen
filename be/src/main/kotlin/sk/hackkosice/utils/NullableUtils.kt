package sk.esten.uss.gbco2.utils

// https://stackoverflow.com/a/35522422/10236314
/**
 * [safeLet] implementation of `?.let` function for multiple types, supporting mixed types between
 * checked parameters
 *
 * example usage:
 * ```kotlin
 *
 *  return safeLet(nodeId, materialId) { node_id, material_id ->
 *      entityRepository.findByNodeIdAndMaterialId(
 *          node_id,
 *          material_id
 *      )
 *  } ?: throw ValidationException(description = "[nodeId] or [materialId] is null!")
 * ```
 *
 * - if you need the [safeLet] implementation for the more params, just implement it below
 */
inline fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    block: (T1, T2, T3) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    block: (T1, T2, T3, T4) -> R?,
): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    p5: T5?,
    block: (T1, T2, T3, T4, T5) -> R?,
): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null)
        block(p1, p2, p3, p4, p5)
    else null
}

/** execute block only if is TRUE */
inline fun <R> Boolean?.trueLet(block: () -> R): R? {
    if (this == true) {
        return block()
    }
    return null
}

inline fun <T : Any, R : Any> someNull(
    vararg values: T?,
    block: () -> R?,
): R? {
    return if (values.any { it == null }) block() else null
}
