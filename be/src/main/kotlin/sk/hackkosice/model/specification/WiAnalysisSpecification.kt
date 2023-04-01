package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.WiAnalysisFilter
import sk.esten.uss.gbco2.model.entity.wi.analysis.VWiAnalysisTranslated
import sk.esten.uss.gbco2.model.entity.wi.analysis.VWiAnalysisTranslated_
import sk.esten.uss.gbco2.model.entity.wi.node.VWiAnalysisNodeTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class WiAnalysisSpecification(
    private val filter: WiAnalysisFilter,
) : GenericSpecification<VWiAnalysisTranslated> {
    override fun toPredicate(
        root: Root<VWiAnalysisTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder,
    ): Predicate? {
        val specification: Specification<VWiAnalysisTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VWiAnalysisTranslated_.languageId))
                )
                .and(
                    containsIgnoreAccent(
                        filter.fullTextSearch,
                        root.get(VWiAnalysisTranslated_.name)
                    )
                )
                .and(equal(filter.scopeId, root.get(VWiAnalysisTranslated_.scopeId)))
                .and(
                    containsIgnoreAccent(
                        filter.createdBy,
                        root.get(VWiAnalysisTranslated_.createdBy)
                    )
                )
                .and(inNode(filter.nodeId, root))

        return specification.toPredicate(root, query, criteriaBuilder)
    }

    private fun inNode(
        nodeId: Long?,
        root: Root<VWiAnalysisTranslated>,
    ): Specification<VWiAnalysisTranslated>? {
        return nodeId.let {
            val nodes = root.join(VWiAnalysisTranslated_.nodes, JoinType.LEFT)
            equal(it, nodes.get(VWiAnalysisNodeTranslated_.nodeId))
        }
    }
}
