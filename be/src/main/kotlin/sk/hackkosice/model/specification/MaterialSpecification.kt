package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.MaterialFilter
import sk.esten.uss.gbco2.model.entity.material.VMaterialTranslated
import sk.esten.uss.gbco2.model.entity.material.VMaterialTranslated_
import sk.esten.uss.gbco2.model.entity.unit_type.VUnitTypeTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class MaterialSpecification(private val filter: MaterialFilter, private val isTree: Boolean) :
    GenericSpecification<VMaterialTranslated> {
    private fun isParent(
        isTree: Boolean,
        root: Root<VMaterialTranslated>
    ): Specification<VMaterialTranslated>? {
        return if (isTree) return isNull(root.get(VMaterialTranslated_.parentMaterial)) else null
    }

    override fun toPredicate(
        root: Root<VMaterialTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {

        val specification: Specification<VMaterialTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VMaterialTranslated_.languageId))
                )
                .and(
                    Specification.where(
                            contains(
                                filter.fullTextSearch,
                                root.get(VMaterialTranslated_.nameTranslated)
                            )
                        )
                        .or(contains(filter.fullTextSearch, root.get(VMaterialTranslated_.memo)))
                        .or(contains(filter.fullTextSearch, root.get(VMaterialTranslated_.code)))
                )
                .and(equal(filter.product, root.get(VMaterialTranslated_.product)))
                .and(
                    equal(
                        filter.unitTypeId,
                        root.get(VMaterialTranslated_.unitType).get(VUnitTypeTranslated_.id)
                    )
                )
                .and(isParent(isTree, root))
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
