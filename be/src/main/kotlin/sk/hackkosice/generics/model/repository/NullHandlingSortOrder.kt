package sk.esten.uss.gbco2.generics.model.repository

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root
import org.springframework.data.domain.Sort

fun <T : Any> Sort.applyOrderWithNullHandling(
    builder: CriteriaBuilder,
    root: Root<T>,
    query: CriteriaQuery<*>
) {
    val order = this.first()
    val ascending = order.isAscending
    val ignoreCase = order.isIgnoreCase
    val nullFirst = order.nullHandling == Sort.NullHandling.NULLS_FIRST

    val nullStrategyExpr =
        builder
            .selectCase<Any>()
            .`when`(builder.isNotNull(root.get<Any>(order.property)), 1)
            .otherwise(0)

    val nullFirstOrder =
        if (nullFirst) builder.asc(nullStrategyExpr) else builder.desc(nullStrategyExpr)

    val sortExpr =
        if (ignoreCase) builder.lower(root.get(order.property)) else root.get(order.property)

    val propertyOrder = if (ascending) builder.asc(sortExpr) else builder.desc(sortExpr)

    query.orderBy(mutableListOf(nullFirstOrder, propertyOrder))
}
