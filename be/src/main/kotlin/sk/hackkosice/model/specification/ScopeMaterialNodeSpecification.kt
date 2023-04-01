package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.ScopeMaterialNodeFilter
import sk.esten.uss.gbco2.model.entity.scope.VScopeTranslated_
import sk.esten.uss.gbco2.model.entity.scope_material_node.VScopeMaterialNodeTranslated
import sk.esten.uss.gbco2.model.entity.scope_material_node.VScopeMaterialNodeTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class ScopeMaterialNodeSpecification(private val filter: ScopeMaterialNodeFilter) :
    GenericSpecification<VScopeMaterialNodeTranslated> {
    override fun toPredicate(
        root: Root<VScopeMaterialNodeTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {

        query.distinct(true)

        val specification: Specification<VScopeMaterialNodeTranslated> =
            Specification.where(
                    equal(
                        principalLangOrEn().id,
                        root.get(VScopeMaterialNodeTranslated_.languageId)
                    )
                )
                .and(
                    equal(
                        filter.scopeId,
                        root.join(VScopeMaterialNodeTranslated_.scope, JoinType.LEFT)
                            .get(VScopeTranslated_.id)
                    )
                )

        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
