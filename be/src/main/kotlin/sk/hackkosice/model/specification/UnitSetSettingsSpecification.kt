package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.UnitSetSettingsFilter
import sk.esten.uss.gbco2.model.entity.material.VMaterialTranslated_
import sk.esten.uss.gbco2.model.entity.unit_set.VUnitSetTranslated_
import sk.esten.uss.gbco2.model.entity.unit_set_settings.VUnitSetSettingsTranslated
import sk.esten.uss.gbco2.model.entity.unit_set_settings.VUnitSetSettingsTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class UnitSetSettingsSpecification(private val filter: UnitSetSettingsFilter) :
    GenericSpecification<VUnitSetSettingsTranslated> {
    override fun toPredicate(
        root: Root<VUnitSetSettingsTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<VUnitSetSettingsTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VUnitSetSettingsTranslated_.languageId))
                )
                .and(
                    equal(
                        filter.unitSetId,
                        root.get(VUnitSetSettingsTranslated_.unitSet).get(VUnitSetTranslated_.id)
                    )
                )
                .and(
                    equal(
                        filter.materialId,
                        root.get(VUnitSetSettingsTranslated_.material).get(VMaterialTranslated_.id)
                    )
                )
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
