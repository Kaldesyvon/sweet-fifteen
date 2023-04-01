package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.QuantityFilter
import sk.esten.uss.gbco2.model.entity.material_node.VMaterialNodeTranslated_
import sk.esten.uss.gbco2.model.entity.quantity.VQuantityTranslated
import sk.esten.uss.gbco2.model.entity.quantity.VQuantityTranslated_
import sk.esten.uss.gbco2.model.entity.scope.VScopeTranslated_
import sk.esten.uss.gbco2.model.entity.scope_material_node.VScopeMaterialNodeTranslated_
import sk.esten.uss.gbco2.service.NodeService
import sk.esten.uss.gbco2.utils.idsInSpecification
import sk.esten.uss.gbco2.utils.principalLangOrEn
import sk.esten.uss.gbco2.utils.principalUnitSetIdOrMetricId

class QuantitySpecification(
    private val filter: QuantityFilter,
    private val nodeService: NodeService
) : GenericSpecification<VQuantityTranslated> {

    private fun isSentViaWS(
        flag: Boolean?,
        root: Root<VQuantityTranslated>
    ): Specification<VQuantityTranslated>? {
        return when (flag) {
            true -> isNotNull(root.get(VQuantityTranslated_.remoteCode))
            false -> isNull(root.get(VQuantityTranslated_.remoteCode))
            else -> null
        }
    }

    private fun hasAnalysis(
        flag: Boolean?,
        root: Root<VQuantityTranslated>
    ): Specification<VQuantityTranslated>? {
        return when (flag) {
            true -> isNotNull(root.get(VQuantityTranslated_.analysisViewId))
            false -> isNull(root.get(VQuantityTranslated_.analysisViewId))
            else -> null
        }
    }

    override fun toPredicate(
        root: Root<VQuantityTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {

        query.distinct(true)

        val specification =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VQuantityTranslated_.languageId))
                )
                .and(
                    equal(principalUnitSetIdOrMetricId(), root.get(VQuantityTranslated_.unitSetId))
                )
                .and(
                    if (filter.withSubNodes == true) {
                        filter.nodeId?.let {
                            nodeService
                                .getAllSubNodeIds(listOf(it))
                                .idsInSpecification(root.get(VQuantityTranslated_.nodeId))
                        }
                    } else {
                        equal(filter.nodeId, root.get(VQuantityTranslated_.nodeId))
                    }
                )
                .and(
                    equal(
                        filter.scopeId,
                        root.join(VQuantityTranslated_.materialNode, JoinType.LEFT)
                            .join(VMaterialNodeTranslated_.scopeMaterialNodes, JoinType.LEFT)
                            .get(VScopeMaterialNodeTranslated_.scope)
                            .get(VScopeTranslated_.id)
                    )
                )
                .and(equal(filter.materialId, root.get(VQuantityTranslated_.materialId)))
                .and(
                    equal(
                        filter.remoteMaterialCodeId,
                        root.get(VQuantityTranslated_.remoteMaterialCodeId)
                    )
                )
                .and(
                    greaterThanOrEqualTo(
                        filter.dateFromStart,
                        root.get(VQuantityTranslated_.dateFrom)
                    )
                )
                .and(lessThanOrEqualTo(filter.dateFromEnd, root.get(VQuantityTranslated_.dateFrom)))
                .and(equal(filter.materialTypeId, root.get(VQuantityTranslated_.materialTypeId)))
                .and(isSentViaWS(filter.sentViaWs, root))
                .and(equal(filter.analysisParamId, root.get(VQuantityTranslated_.analysisParamId)))
                .and(equal(filter.io, root.get(VQuantityTranslated_.io)))
                .and(equal(filter.autoAnalysis, root.get(VQuantityTranslated_.autoAnalysis)))
                .and(hasAnalysis(filter.hasAnalysis, root))
                .and(equal(filter.mdlA, root.get(VQuantityTranslated_.mdlA)))

        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
