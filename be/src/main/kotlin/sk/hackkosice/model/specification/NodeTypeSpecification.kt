package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.NodeTypeFilter
import sk.esten.uss.gbco2.model.entity.node_level.VNodeLevelTranslated_
import sk.esten.uss.gbco2.model.entity.node_type.VNodeTypeTranslated
import sk.esten.uss.gbco2.model.entity.node_type.VNodeTypeTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class NodeTypeSpecification(private val filter: NodeTypeFilter) :
    GenericSpecification<VNodeTypeTranslated> {

    override fun toPredicate(
        root: Root<VNodeTypeTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<VNodeTypeTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VNodeTypeTranslated_.languageId))
                )
                .and(
                    equal(
                        filter.nodeLevelId,
                        root.get(VNodeTypeTranslated_.nodeLevel).get(VNodeLevelTranslated_.id)
                    )
                )
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
