package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.FormulaCalculationFilter
import sk.esten.uss.gbco2.model.entity.analysis.VAnalysisTranslated_
import sk.esten.uss.gbco2.model.entity.analysis_param.VAnalysisParamTranslated_
import sk.esten.uss.gbco2.model.entity.formula_calculation.VFormulaCalculationTranslated
import sk.esten.uss.gbco2.model.entity.formula_calculation.VFormulaCalculationTranslated_
import sk.esten.uss.gbco2.utils.principal
import sk.esten.uss.gbco2.utils.principalLangOrEn

class FormulaCalculationSpecification(private val filter: FormulaCalculationFilter) :
    GenericSpecification<VFormulaCalculationTranslated> {
    override fun toPredicate(
        root: Root<VFormulaCalculationTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<VFormulaCalculationTranslated> =
            Specification.where(
                    equal(
                        principalLangOrEn().id,
                        root.get(VFormulaCalculationTranslated_.languageId)
                    )
                )
                .and(
                    equal(
                        principal()?.unitSetId,
                        root.get(VFormulaCalculationTranslated_.analysisUnitSetId)
                    )
                )
                .and(
                    equal(
                        filter.analysisParamId,
                        root.get(VFormulaCalculationTranslated_.analysisCalculated)
                            .get(VAnalysisTranslated_.analysisParam)
                            .get(VAnalysisParamTranslated_.id)
                    )
                )
                .and(
                    greaterThanOrEqualTo(
                        filter.createdFrom,
                        root.get(VFormulaCalculationTranslated_.analysisCalculated)
                            .get(VAnalysisTranslated_.created)
                    )
                )
                .and(
                    lessThanOrEqualTo(
                        filter.createdTo,
                        root.get(VFormulaCalculationTranslated_.analysisCalculated)
                            .get(VAnalysisTranslated_.created)
                    )
                )

        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
