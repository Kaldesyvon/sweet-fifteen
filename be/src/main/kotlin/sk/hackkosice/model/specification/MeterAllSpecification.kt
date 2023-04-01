package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.MeterReadAllFilter
import sk.esten.uss.gbco2.model.entity.meter.VMeterTranslated
import sk.esten.uss.gbco2.model.entity.meter.VMeterTranslated_
import sk.esten.uss.gbco2.model.entity.node.VNodeTranslated_
import sk.esten.uss.gbco2.model.entity.unit_type.VUnitTypeTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class MeterAllSpecification(private val filter: MeterReadAllFilter) :
    GenericSpecification<VMeterTranslated> {

    override fun toPredicate(
        root: Root<VMeterTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<VMeterTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VMeterTranslated_.languageId))
                )
                .and(
                    equal(
                        filter.unitTypeId,
                        root.get(VMeterTranslated_.unitType).get(VUnitTypeTranslated_.id)
                    )
                )
                .and(
                    equal(
                        filter.nodeId,
                        root.get(VMeterTranslated_.nodeLocation).get(VNodeTranslated_.id)
                    )
                )
                .and(
                    filter.validTo?.let {
                        Specification.where(isNull(root.get(VMeterTranslated_.validTo)))
                            .or(greaterThanOrEqualTo(it, root.get(VMeterTranslated_.validTo)))
                    }
                )
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
