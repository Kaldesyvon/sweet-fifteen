package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.QuantityMonthFilter
import sk.esten.uss.gbco2.model.entity.material.Material_
import sk.esten.uss.gbco2.model.entity.material_node.MaterialNode_
import sk.esten.uss.gbco2.model.entity.node.Node_
import sk.esten.uss.gbco2.model.entity.quantity_month.QuantityMonth
import sk.esten.uss.gbco2.model.entity.quantity_month.QuantityMonth_
import sk.esten.uss.gbco2.model.entity.scope.ScopeSuper_
import sk.esten.uss.gbco2.model.entity.scope_material_node.ScopeMaterialNode_

class QuantityMonthSpecification(private val quantityMonthFilter: QuantityMonthFilter) :
    GenericSpecification<QuantityMonth> {

    override fun toPredicate(
        root: Root<QuantityMonth>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        query.distinct(true)
        val specification: Specification<QuantityMonth> =
            Specification.where(
                    equal(quantityMonthFilter.nodeId, root.get(QuantityMonth_.node).get(Node_.id))
                )
                .and(
                    isIn(
                        quantityMonthFilter.materialIds,
                        root.get(QuantityMonth_.material).get(Material_.id)
                    )
                )
                .and(
                    lessThanOrEqualTo(
                        quantityMonthFilter.getDisplayToDate(),
                        root.get(QuantityMonth_.month)
                    )
                )
                .and(
                    greaterThanOrEqualTo(
                        quantityMonthFilter.getDisplayToDate().withDayOfYear(1),
                        root.get(QuantityMonth_.month)
                    )
                )
                .and(
                    equal(
                        quantityMonthFilter.scopeId,
                        root.join(QuantityMonth_.materialNode, JoinType.LEFT)
                            .join(MaterialNode_.scopeMaterialNode, JoinType.LEFT)
                            .join(ScopeMaterialNode_.scope, JoinType.LEFT)
                            .get(ScopeSuper_.id)
                    )
                )
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
