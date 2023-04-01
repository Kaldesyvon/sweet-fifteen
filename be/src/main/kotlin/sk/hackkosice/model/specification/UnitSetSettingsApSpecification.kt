package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.UnitSetSettingsApFilter
import sk.esten.uss.gbco2.model.entity.analysis_param.VAnalysisParamTranslated_
import sk.esten.uss.gbco2.model.entity.unit_set.VUnitSetTranslated_
import sk.esten.uss.gbco2.model.entity.unit_set_settings_ap.VUnitSetSettingsApTranslated
import sk.esten.uss.gbco2.model.entity.unit_set_settings_ap.VUnitSetSettingsApTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class UnitSetSettingsApSpecification(private val filter: UnitSetSettingsApFilter) :
    GenericSpecification<VUnitSetSettingsApTranslated> {

    override fun toPredicate(
        root: Root<VUnitSetSettingsApTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<VUnitSetSettingsApTranslated> =
            Specification.where(
                    equal(
                        principalLangOrEn().id,
                        root.get(VUnitSetSettingsApTranslated_.languageId)
                    )
                )
                .and(
                    equal(
                        filter.unitSetId,
                        root.get(VUnitSetSettingsApTranslated_.unitSet).get(VUnitSetTranslated_.id)
                    )
                )
                .and(
                    equal(
                        filter.analysisParamId,
                        root.get(VUnitSetSettingsApTranslated_.analysisParam)
                            .get(VAnalysisParamTranslated_.id)
                    )
                )
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
