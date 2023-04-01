package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.MaterialGroupFilter
import sk.esten.uss.gbco2.model.entity.material.VMaterialTranslated
import sk.esten.uss.gbco2.model.entity.material.VMaterialTranslated_
import sk.esten.uss.gbco2.model.entity.material_node.MaterialNode_
import sk.esten.uss.gbco2.model.entity.node.Node_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class MaterialGroupSpecification(private val filter: MaterialGroupFilter) :
    GenericSpecification<VMaterialTranslated> {

    override fun toPredicate(
        root: Root<VMaterialTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {

        val nodes = root.join(VMaterialTranslated_.materialNodes, JoinType.LEFT)

        val specification: Specification<VMaterialTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VMaterialTranslated_.languageId))
                )
                .and(equal(filter.nodeId, nodes.get(MaterialNode_.node).get(Node_.id)))
                .and(isNotNull(root.get(VMaterialTranslated_.parentMaterial)))
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
