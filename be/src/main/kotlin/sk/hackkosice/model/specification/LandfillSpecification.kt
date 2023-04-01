package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.LandfillFilter
import sk.esten.uss.gbco2.model.entity.landfill.VLandfillTranslated
import sk.esten.uss.gbco2.model.entity.landfill.VLandfillTranslated_
import sk.esten.uss.gbco2.model.entity.node.VNodeTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class LandfillSpecification(private val filter: LandfillFilter) :
    GenericSpecification<VLandfillTranslated> {
    override fun toPredicate(
        root: Root<VLandfillTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {

        val specification: Specification<VLandfillTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VLandfillTranslated_.languageId))
                )
                .and(
                    if (filter.nodeId != null) {
                        equal(
                            filter.nodeId,
                            root.join(VLandfillTranslated_.nodes).get(VNodeTranslated_.id)
                        )
                    } else null
                )
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
