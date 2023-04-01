package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.model.entity.meter.Meter_
import sk.esten.uss.gbco2.model.entity.meter_certificate.MeterCertificate
import sk.esten.uss.gbco2.model.entity.meter_certificate.MeterCertificate_

class MeterCertificateSpecification(private val meterId: Long) :
    GenericSpecification<MeterCertificate> {

    override fun toPredicate(
        root: Root<MeterCertificate>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<MeterCertificate> =
            Specification.where(equal(meterId, root.get(MeterCertificate_.meter).get(Meter_.id)))
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
