package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.MaterialConversionFilter
import sk.esten.uss.gbco2.model.entity.material.Material_
import sk.esten.uss.gbco2.model.entity.material_conversion.VMaterialConversionTranslated
import sk.esten.uss.gbco2.model.entity.material_conversion.VMaterialConversionTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class MaterialConversionSpecification(private val filter: MaterialConversionFilter) :
    GenericSpecification<VMaterialConversionTranslated> {
    override fun toPredicate(
        root: Root<VMaterialConversionTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<VMaterialConversionTranslated> =
            Specification.where(
                    equal(
                        filter.materialId,
                        root.get(VMaterialConversionTranslated_.material).get(Material_.id)
                    )
                )
                .and(
                    equal(
                        filter.loadTabAnalysis,
                        root.get(VMaterialConversionTranslated_.loadTabAnalysis)
                    )
                )
                .and(
                    equal(
                        filter.loadTabQuantity,
                        root.get(VMaterialConversionTranslated_.loadTabQuantity)
                    )
                )
                .and(
                    equal(
                        principalLangOrEn().id,
                        root.get(VMaterialConversionTranslated_.languageId)
                    )
                )

        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
