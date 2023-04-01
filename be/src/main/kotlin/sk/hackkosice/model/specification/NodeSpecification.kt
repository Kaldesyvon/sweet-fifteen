package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.NodeFilter
import sk.esten.uss.gbco2.model.entity.material_node.MaterialNode_
import sk.esten.uss.gbco2.model.entity.node.VNodeTranslated
import sk.esten.uss.gbco2.model.entity.node.VNodeTranslated_
import sk.esten.uss.gbco2.model.entity.scope.Scope_
import sk.esten.uss.gbco2.model.entity.scope_material_node.ScopeMaterialNode_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class NodeSpecification(private val filter: NodeFilter) : GenericSpecification<VNodeTranslated> {

    override fun toPredicate(
        root: Root<VNodeTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<VNodeTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VNodeTranslated_.languageId))
                )
                .and(
                    filter.scopeId?.let {
                        val materialNodeJoin =
                            root.join(VNodeTranslated_.materialNodes, JoinType.LEFT)
                        val scopeMaterialNodeJoin =
                            materialNodeJoin.join(MaterialNode_.scopeMaterialNode, JoinType.LEFT)
                        equal(
                            it,
                            scopeMaterialNodeJoin.get(ScopeMaterialNode_.scope).get(Scope_.id)
                        )
                    }
                )
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
