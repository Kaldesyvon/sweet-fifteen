package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.ScopeFilter
import sk.esten.uss.gbco2.model.entity.material.Material_
import sk.esten.uss.gbco2.model.entity.material.VMaterialTranslated_
import sk.esten.uss.gbco2.model.entity.material_node.VMaterialNodeTranslated_
import sk.esten.uss.gbco2.model.entity.node.Node_
import sk.esten.uss.gbco2.model.entity.scope.VScopeTranslated
import sk.esten.uss.gbco2.model.entity.scope.VScopeTranslated_
import sk.esten.uss.gbco2.model.entity.scope_denominator.VScopeDenominatorTranslated_
import sk.esten.uss.gbco2.model.entity.scope_material_node.VScopeMaterialNodeTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class ScopeSpecification(private val filter: ScopeFilter) : GenericSpecification<VScopeTranslated> {

    override fun toPredicate(
        root: Root<VScopeTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {

        query.distinct(true)

        val materialNode =
            root.join(VScopeTranslated_.scopeMaterialNodes, JoinType.LEFT)
                .join(VScopeMaterialNodeTranslated_.materialNode, JoinType.LEFT)

        val specification: Specification<VScopeTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VScopeTranslated_.languageId))
                )
                .and(contains(filter.fullTextSearch, root.get(VScopeTranslated_.nameTranslated)))
                .and(
                    equal(
                        filter.denominatorId,
                        root.join(VScopeTranslated_.scopeDenominators, JoinType.LEFT)
                            .get(VScopeDenominatorTranslated_.material)
                            .get(VMaterialTranslated_.id)
                    )
                )
                .and(
                    equal(
                        filter.nodeId,
                        materialNode
                            .join(VMaterialNodeTranslated_.node, JoinType.LEFT)
                            .get(Node_.id)
                    )
                )
                .and(
                    equal(
                        filter.materialId,
                        materialNode
                            .join(VMaterialNodeTranslated_.material, JoinType.LEFT)
                            .get(Material_.id)
                    )
                )
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
