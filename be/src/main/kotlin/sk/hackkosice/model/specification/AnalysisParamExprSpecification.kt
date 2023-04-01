package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.AnalysisParamExprFilter
import sk.esten.uss.gbco2.model.entity.analysis_param.VAnalysisParamTranslated_
import sk.esten.uss.gbco2.model.entity.analysis_param_expr.VAnalysisParamExprTranslated
import sk.esten.uss.gbco2.model.entity.analysis_param_expr.VAnalysisParamExprTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class AnalysisParamExprSpecification(private val filter: AnalysisParamExprFilter) :
    GenericSpecification<VAnalysisParamExprTranslated> {
    override fun toPredicate(
        root: Root<VAnalysisParamExprTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<VAnalysisParamExprTranslated> =
            Specification.where(
                    equal(
                        principalLangOrEn().id,
                        root.get(VAnalysisParamExprTranslated_.languageId)
                    )
                )
                .and(
                    equal(
                        filter.analysisParamId,
                        root.get(VAnalysisParamExprTranslated_.analysisParam)
                            .get(VAnalysisParamTranslated_.id)
                    )
                )
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
