package sk.esten.uss.gbco2.model.specification

import java.time.LocalTime
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.EmailFilter
import sk.esten.uss.gbco2.model.entity.email.Email
import sk.esten.uss.gbco2.model.entity.email.Email_

class EmailSpecification(private val filter: EmailFilter) : GenericSpecification<Email> {

    override fun toPredicate(
        root: Root<Email>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<Email> =
            Specification.where(isNotNull(root.get(Email_.id)))
                .and(equal(filter.login, root.get(Email_.login)))
                .and(
                    Specification.where(
                            containsIgnoreAccent(filter.fullTextSearch, root.get(Email_.subject))
                        )
                        .or(containsIgnoreAccent(filter.fullTextSearch, root.get(Email_.body)))
                        .or(
                            containsIgnoreAccent(
                                filter.fullTextSearch,
                                root.get(Email_.attachmentName)
                            )
                        )
                )
                .and(greaterThanOrEqualTo(filter.from?.atStartOfDay(), root.get(Email_.sent)))
                .and(lessThanOrEqualTo(filter.to?.atTime(LocalTime.MAX), root.get(Email_.sent)))

        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
