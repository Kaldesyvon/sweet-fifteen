package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.UnitParamsFilter
import sk.esten.uss.gbco2.model.entity.unit.VGbcUnitTranslated
import sk.esten.uss.gbco2.model.entity.unit.VGbcUnitTranslated_
import sk.esten.uss.gbco2.model.entity.unit_type.VUnitTypeTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class UnitSpecification(private val filter: UnitParamsFilter) :
    GenericSpecification<VGbcUnitTranslated> {

    override fun toPredicate(
        root: Root<VGbcUnitTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<VGbcUnitTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VGbcUnitTranslated_.languageId))
                )
                .and(
                    equal(
                        filter.unitTypeId,
                        root.get(VGbcUnitTranslated_.unitType).get(VUnitTypeTranslated_.id)
                    )
                )
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
