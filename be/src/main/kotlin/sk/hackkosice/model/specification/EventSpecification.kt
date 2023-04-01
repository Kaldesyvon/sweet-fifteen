package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.EventFilter
import sk.esten.uss.gbco2.model.entity.event.VEventTranslated
import sk.esten.uss.gbco2.model.entity.event.VEventTranslated_
import sk.esten.uss.gbco2.model.entity.event_type.VEventTypeTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class EventSpecification(private val filter: EventFilter) : GenericSpecification<VEventTranslated> {
    override fun toPredicate(
        root: Root<VEventTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<VEventTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VEventTranslated_.languageId))
                )
                .and(greaterThanOrEqualTo(filter.dateFrom, root.get(VEventTranslated_.eventDate)))
                .and(lessThanOrEqualTo(filter.dateTo, root.get(VEventTranslated_.eventDate)))
                .and(
                    equal(
                        filter.eventLevelId,
                        root.get(VEventTranslated_.eventType).get(VEventTypeTranslated_.eventLevel)
                    )
                )
                .and(
                    equal(
                        filter.eventTypeId,
                        root.get(VEventTranslated_.eventType).get(VEventTypeTranslated_.id)
                    )
                )
                .and(equal(filter.method, root.get(VEventTranslated_.method)))
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
