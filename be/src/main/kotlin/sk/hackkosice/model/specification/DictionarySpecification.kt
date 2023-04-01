package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.DictionaryFilter
import sk.esten.uss.gbco2.model.entity.dictionary.Dictionary
import sk.esten.uss.gbco2.model.entity.dictionary.Dictionary_
import sk.esten.uss.gbco2.model.entity.dictionary.VDictionaryTranslated
import sk.esten.uss.gbco2.model.entity.dictionary.VDictionaryTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class DictionarySpecification(private val filter: DictionaryFilter) :
    GenericSpecification<VDictionaryTranslated> {

    override fun toPredicate(
        root: Root<VDictionaryTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {

        var specification: Specification<VDictionaryTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VDictionaryTranslated_.languageId))
                )
                .and(equal(filter.key, root.get(VDictionaryTranslated_.key)))
                .and(equal(filter.translation, root.get(VDictionaryTranslated_.translation)))
                .and(equal(filter.languageId, root.get(VDictionaryTranslated_.entryLanguageId)))
                .and(
                    equal(
                        filter.dictionaryTableId,
                        root.get(VDictionaryTranslated_.dictionaryTableId)
                    )
                )

        filter.notExistsLanguageId?.let {
            // sub-query for not-existing translations
            val languageKeysSubQuery = query.subquery(String::class.java)
            val subRoot = languageKeysSubQuery.from(Dictionary::class.java)
            languageKeysSubQuery.select(subRoot.get(Dictionary_.key))
            languageKeysSubQuery.where(
                criteriaBuilder.equal(subRoot.get(Dictionary_.languageId), it)
            )
            specification =
                specification.and { _, _, _ ->
                    root.get(VDictionaryTranslated_.key).`in`(languageKeysSubQuery).not()
                }
        }
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
