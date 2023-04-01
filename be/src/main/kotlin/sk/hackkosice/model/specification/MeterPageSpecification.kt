package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.MeterFilter
import sk.esten.uss.gbco2.model.entity.fuel_type.VFuelTypeTranslated_
import sk.esten.uss.gbco2.model.entity.meter.VMeterTranslated
import sk.esten.uss.gbco2.model.entity.meter.VMeterTranslated_
import sk.esten.uss.gbco2.model.entity.node.VNodeTranslated_
import sk.esten.uss.gbco2.service.NodeService
import sk.esten.uss.gbco2.utils.idsInSpecification
import sk.esten.uss.gbco2.utils.principalLangOrEn

class MeterPageSpecification(
    private val filter: MeterFilter,
    private val nodeService: NodeService
) : GenericSpecification<VMeterTranslated> {

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
                        filter.fuelTypeId,
                        root.get(VMeterTranslated_.fuelType).get(VFuelTypeTranslated_.id)
                    )
                )
                .and(
                    if (filter.withSubNodes == true) {
                        filter.nodeId?.let {
                            nodeService
                                .getAllSubNodeIds(listOf(it))
                                .idsInSpecification(
                                    root.get(VMeterTranslated_.nodeLocation)
                                        .get(VNodeTranslated_.id)
                                )
                        }
                    } else {
                        equal(
                            filter.nodeId,
                            root.get(VMeterTranslated_.nodeLocation).get(VNodeTranslated_.id)
                        )
                    }
                )
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
