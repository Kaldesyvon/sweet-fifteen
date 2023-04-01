package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.WebservicesFilter
import sk.esten.uss.gbco2.model.entity.webservices.Webservices
import sk.esten.uss.gbco2.model.entity.webservices.Webservices_

class WebservicesSpecification(private val filter: WebservicesFilter) :
    GenericSpecification<Webservices> {

    private fun containsWithEscapeChar(
        filter: String?,
        path: Path<String>,
        escapeChar: Char = '!'
    ): Specification<Webservices>? {
        if (filter?.isEmpty() == true) {
            return null
        }
        return filter?.let {
            return Specification { _, _, builder ->
                builder.like(
                    builder.lower(path),
                    builder.lower(builder.literal("%${it.trim()}%")),
                    escapeChar
                )
            }
        }
    }

    override fun toPredicate(
        root: Root<Webservices>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<Webservices> =
            Specification.where(
                    greaterThanOrEqualTo(filter.dateFrom, root.get(Webservices_.called))
                )
                .and(lessThanOrEqualTo(filter.dateTo, root.get(Webservices_.called)))
                .and(equal(filter.successCall, root.get(Webservices_.success)))
                .and(equal(filter.login, root.get(Webservices_.login)))
                .and(
                    Specification.where(
                            containsWithEscapeChar(
                                filter.fullTextSearch,
                                root.get(Webservices_.dataReceived)
                            )
                        )
                        .or(
                            containsWithEscapeChar(
                                filter.fullTextSearch,
                                root.get(Webservices_.dataSent)
                            )
                        )
                )

        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
