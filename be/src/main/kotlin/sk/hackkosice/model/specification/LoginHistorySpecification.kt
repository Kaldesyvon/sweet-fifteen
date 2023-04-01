package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.LoginHistoryFilter
import sk.esten.uss.gbco2.model.entity.login.Login
import sk.esten.uss.gbco2.model.entity.login.Login_
import sk.esten.uss.gbco2.utils.atEndOfDay

class LoginHistorySpecification(private val filter: LoginHistoryFilter) :
    GenericSpecification<Login> {

    override fun toPredicate(
        root: Root<Login>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<Login> =
            Specification.where(
                    Specification.where(equal(filter.login, root.get(Login_.login)))
                        .or(equal(filter.login, root.get(Login_.loginAd)))
                )
                .and(
                    Specification.where(
                        greaterThanOrEqualTo(
                            filter.from?.atStartOfDay(),
                            root.get(Login_.loginDate)
                        )
                    )
                )
                .and(
                    Specification.where(
                        lessThanOrEqualTo(filter.to?.atEndOfDay(), root.get(Login_.loginDate))
                    )
                )
                .and(booleanEqual(true, filter.success, root.get(Login_.success)))

        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
