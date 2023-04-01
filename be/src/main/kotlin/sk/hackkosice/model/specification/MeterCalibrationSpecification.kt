package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.MeterCalibrationFilter
import sk.esten.uss.gbco2.model.entity.meter.VMeterTranslated_
import sk.esten.uss.gbco2.model.entity.meter_calibration.VMeterCalibrationTranslated
import sk.esten.uss.gbco2.model.entity.meter_calibration.VMeterCalibrationTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class MeterCalibrationSpecification(private val filter: MeterCalibrationFilter) :
    GenericSpecification<VMeterCalibrationTranslated> {

    override fun toPredicate(
        root: Root<VMeterCalibrationTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<VMeterCalibrationTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VMeterCalibrationTranslated_.languageId))
                )
                .and(
                    equal(
                        filter.meterId,
                        root.get(VMeterCalibrationTranslated_.meter).get(VMeterTranslated_.id)
                    )
                )
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
