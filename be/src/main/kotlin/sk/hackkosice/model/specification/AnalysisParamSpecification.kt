package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.AnalysisParamFilter
import sk.esten.uss.gbco2.model.entity.analysis_param.VAnalysisParamTranslated
import sk.esten.uss.gbco2.model.entity.analysis_param.VAnalysisParamTranslated_
import sk.esten.uss.gbco2.model.entity.analysis_param_type.VAnalysisParamTypeTranslated_
import sk.esten.uss.gbco2.model.entity.unit_type.VUnitTypeTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class AnalysisParamSpecification(private val filter: AnalysisParamFilter) :
    GenericSpecification<VAnalysisParamTranslated> {
    override fun toPredicate(
        root: Root<VAnalysisParamTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<VAnalysisParamTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VAnalysisParamTranslated_.languageId))
                )
                .and(
                    Specification.where(
                            containsIgnoreAccent(
                                filter.fullTextSearch,
                                root.get(VAnalysisParamTranslated_.nameTranslated)
                            )
                        )
                        .or(
                            containsIgnoreAccent(
                                filter.fullTextSearch,
                                root.get(VAnalysisParamTranslated_.nameEn)
                            )
                        )
                )
                .and(
                    equal(
                        filter.unitTypeId,
                        root.get(VAnalysisParamTranslated_.unitType).get(VUnitTypeTranslated_.id)
                    )
                )
                .and(
                    equal(
                        filter.analysisParamTypeId,
                        root.get(VAnalysisParamTranslated_.analysisParamType)
                            .get(VAnalysisParamTypeTranslated_.id)
                    )
                )
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
