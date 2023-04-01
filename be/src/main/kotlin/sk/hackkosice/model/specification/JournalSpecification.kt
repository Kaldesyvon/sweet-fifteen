package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.JournalFilter
import sk.esten.uss.gbco2.model.entity.journal.VJournalTranslated
import sk.esten.uss.gbco2.model.entity.journal.VJournalTranslated_
import sk.esten.uss.gbco2.model.entity.journal_type.VJournalTypeTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class JournalSpecification(private val filter: JournalFilter) :
    GenericSpecification<VJournalTranslated> {

    private fun isInserted(
        flag: Boolean?,
        root: Root<VJournalTranslated>
    ): Specification<VJournalTranslated>? {
        return booleanEqual(flag, "I", root.get(VJournalTranslated_.iud))
    }

    private fun isUpdated(
        flag: Boolean?,
        root: Root<VJournalTranslated>
    ): Specification<VJournalTranslated>? {
        return booleanEqual(flag, "U", root.get(VJournalTranslated_.iud))
    }

    private fun isDeleted(
        flag: Boolean?,
        root: Root<VJournalTranslated>
    ): Specification<VJournalTranslated>? {
        return booleanEqual(flag, "D", root.get(VJournalTranslated_.iud))
    }

    override fun toPredicate(
        root: Root<VJournalTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<VJournalTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VJournalTranslated_.languageId))
                )
                .and(isInserted(filter.inserted, root))
                .and(isDeleted(filter.deleted, root))
                .and(isUpdated(filter.updated, root))
                .and(greaterThanOrEqualTo(filter.dateFrom, root.get(VJournalTranslated_.created)))
                .and(lessThanOrEqualTo(filter.dateTo, root.get(VJournalTranslated_.created)))
                .and(equal(filter.userLogin, root.get(VJournalTranslated_.createdBy)))
                .and(contains(filter.detailContains, root.get(VJournalTranslated_.detail)))
                .and(
                    equal(
                        filter.journalTypeId,
                        root.get(VJournalTranslated_.journalType).get(VJournalTypeTranslated_.id)
                    )
                )

        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
