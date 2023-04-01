package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.ScopeDenominatorFilter
import sk.esten.uss.gbco2.model.entity.material.VMaterialTranslated_
import sk.esten.uss.gbco2.model.entity.scope.VScopeTranslated_
import sk.esten.uss.gbco2.model.entity.scope_denominator.VScopeDenominatorTranslated
import sk.esten.uss.gbco2.model.entity.scope_denominator.VScopeDenominatorTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class ScopeDenominatorSpecification(private val scopeDenominatorFilter: ScopeDenominatorFilter?) :
    GenericSpecification<VScopeDenominatorTranslated> {
    override fun toPredicate(
        root: Root<VScopeDenominatorTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<VScopeDenominatorTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VScopeDenominatorTranslated_.languageId))
                )
                .and(
                    equal(
                        scopeDenominatorFilter?.scopeId,
                        root.join(VScopeDenominatorTranslated_.scope, JoinType.LEFT)
                            .get(VScopeTranslated_.id)
                    )
                )
                .and(
                    equal(
                        scopeDenominatorFilter?.materialId,
                        root.join(VScopeDenominatorTranslated_.material, JoinType.LEFT)
                            .get(VMaterialTranslated_.id)
                    )
                )
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
