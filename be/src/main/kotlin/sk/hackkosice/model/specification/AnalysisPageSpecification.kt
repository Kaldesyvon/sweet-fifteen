package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.AnalysisFilter
import sk.esten.uss.gbco2.model.entity.analysis.VAnalysisTranslated
import sk.esten.uss.gbco2.model.entity.analysis.VAnalysisTranslated_
import sk.esten.uss.gbco2.service.MaterialNodeTypeService
import sk.esten.uss.gbco2.service.NodeService
import sk.esten.uss.gbco2.service.ScopeMaterialNodeService
import sk.esten.uss.gbco2.utils.idsInSpecification
import sk.esten.uss.gbco2.utils.principalLangOrEn
import sk.esten.uss.gbco2.utils.principalUnitSetIdOrMetricId

class AnalysisPageSpecification(
    private val filter: AnalysisFilter,
    private val nodeService: NodeService,
    private val materialNodeTypeService: MaterialNodeTypeService,
    private val scopeMaterialNodeService: ScopeMaterialNodeService
) : GenericSpecification<VAnalysisTranslated> {
    override fun toPredicate(
        root: Root<VAnalysisTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder,
    ): Predicate? {
        val specification: Specification<VAnalysisTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VAnalysisTranslated_.languageId))
                )
                .and(
                    equal(principalUnitSetIdOrMetricId(), root.get(VAnalysisTranslated_.unitSetId))
                )
                .and(
                    if (filter.withSubNodes == true) {
                        filter.nodeId?.let {
                            nodeService
                                .getAllSubNodeIds(listOf(it))
                                .idsInSpecification(root.get(VAnalysisTranslated_.nodeId))
                        }
                    } else {
                        equal(filter.nodeId, root.get(VAnalysisTranslated_.nodeId))
                    }
                )
                .and(equal(filter.analysisParamId, root.get(VAnalysisTranslated_.analysisParamId)))
                .and(equal(filter.materialId, root.get(VAnalysisTranslated_.materialId)))
                .and(
                    equal(
                        filter.remoteMaterialCodeId,
                        root.get(VAnalysisTranslated_.remoteMaterialCodeId)
                    )
                )
                .and(
                    greaterThanOrEqualTo(
                        filter.dateFromStart,
                        root.get(VAnalysisTranslated_.validFrom)
                    )
                )
                .and(
                    lessThanOrEqualTo(filter.dateFromEnd, root.get(VAnalysisTranslated_.validFrom))
                )
                .and(equal(filter.calculated, root.get(VAnalysisTranslated_.calculated)))
                .and(isMdla(filter.mdl, root))
                .and(isSentViaWS(filter.sentViaWs, root))
                .and(
                    filter.materialTypeId?.let {
                        materialNodeTypeService
                            .getMaterialNodeIdsByMaterialType(it)
                            .idsInSpecification(root.get(VAnalysisTranslated_.materialNodeId))
                    }
                )
                .and(
                    filter.scopeId?.let {
                        scopeMaterialNodeService
                            .getMaterialNodeIdsByScope(it)
                            .idsInSpecification(root.get(VAnalysisTranslated_.materialNodeId))
                    }
                )
                .and(isSentViaWS(filter.sentViaWs, root))

        return specification.toPredicate(root, query, criteriaBuilder)
    }

    private fun isSentViaWS(
        flag: Boolean?,
        root: Root<VAnalysisTranslated>
    ): Specification<VAnalysisTranslated>? {
        return when (flag) {
            true -> isNotNull(root.get(VAnalysisTranslated_.remoteCode))
            false -> isNull(root.get(VAnalysisTranslated_.remoteCode))
            else -> null
        }
    }

    private fun isMdla(
        flag: Boolean?,
        root: Root<VAnalysisTranslated>
    ): Specification<VAnalysisTranslated>? {
        return when (flag) {
            true -> equal("<", root.get(VAnalysisTranslated_.mdlA))
            false ->
                or(isNull(root.get(VAnalysisTranslated_.mdlA)))
                    .or(equal("", root.get(VAnalysisTranslated_.mdlA)))
                    .or(equal(">", root.get(VAnalysisTranslated_.mdlA)))
            else -> null
        }
    }
}
