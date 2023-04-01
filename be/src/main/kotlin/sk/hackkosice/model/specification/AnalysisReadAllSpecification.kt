package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.AnalysisReadAllFilter
import sk.esten.uss.gbco2.model.entity.analysis.VAnalysisTranslated
import sk.esten.uss.gbco2.model.entity.analysis.VAnalysisTranslated_
import sk.esten.uss.gbco2.model.entity.analysis_param.VAnalysisParamTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn
import sk.esten.uss.gbco2.utils.principalUnitSetIdOrMetricId

class AnalysisReadAllSpecification(private val filter: AnalysisReadAllFilter) :
    GenericSpecification<VAnalysisTranslated> {
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
                    equal(
                        filter.analysisParamId,
                        root.get(VAnalysisTranslated_.analysisParam)
                            .get(VAnalysisParamTranslated_.id)
                    )
                )
                .and(equal(filter.nodeId, root.get(VAnalysisTranslated_.nodeId)))
                .and(equal(filter.materialId, root.get(VAnalysisTranslated_.materialId)))
                .and(lessThanOrEqualTo(filter.validIn, root.get(VAnalysisTranslated_.validFrom)))
                .and(
                    Specification.where(
                            greaterThanOrEqualTo(
                                filter.validIn,
                                root.get(VAnalysisTranslated_.validTo)
                            )
                        )
                        .or(isNull(root.get(VAnalysisTranslated_.validTo)))
                )
                .and(
                    equal(
                        filter.remoteMaterialCodeSelectedId,
                        root.get(VAnalysisTranslated_.remoteMaterialCodeId)
                    )
                )

        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
