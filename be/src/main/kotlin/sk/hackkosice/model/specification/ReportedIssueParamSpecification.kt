package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.ReportedIssueFilter
import sk.esten.uss.gbco2.model.entity.reported_issue.ReportedIssue
import sk.esten.uss.gbco2.model.entity.reported_issue.ReportedIssue_
import sk.esten.uss.gbco2.utils.atEndOfDay

class ReportedIssueParamSpecification(private val filter: ReportedIssueFilter) :
    GenericSpecification<ReportedIssue> {
    override fun toPredicate(
        root: Root<ReportedIssue>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<ReportedIssue> =
            Specification.where(equal(filter.state, root.get(ReportedIssue_.state)))
                .and(equal(filter.priority, root.get(ReportedIssue_.priority)))
                .and(equal(filter.type, root.get(ReportedIssue_.type)))
                .and(containsIgnoreAccent(filter.login, root.get(ReportedIssue_.createdBy)))
                .and(
                    greaterThanOrEqualTo(
                        filter.dateFrom?.atStartOfDay(),
                        root.get(ReportedIssue_.createdAt)
                    )
                )
                .and(
                    lessThanOrEqualTo(
                        filter.dateTo?.atEndOfDay(),
                        root.get(ReportedIssue_.createdAt)
                    )
                )
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
