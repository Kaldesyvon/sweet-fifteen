package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.UsersFilter
import sk.esten.uss.gbco2.model.entity.authorization.VAuthorizationTranslated_
import sk.esten.uss.gbco2.model.entity.node.VNodeTranslated_
import sk.esten.uss.gbco2.model.entity.role.VRoleTranslated_
import sk.esten.uss.gbco2.model.entity.user.VUserTranslated
import sk.esten.uss.gbco2.model.entity.user.VUserTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class UserSpecification(private val filter: UsersFilter) : GenericSpecification<VUserTranslated> {

    override fun toPredicate(
        root: Root<VUserTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {

        query.distinct(true)

        val specification: Specification<VUserTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VUserTranslated_.languageId))
                )
                .and(
                    when (filter.nodeId) {
                        -1L -> isNull(root.get(VUserTranslated_.node))
                        else ->
                            equal(
                                filter.nodeId,
                                root.get(VUserTranslated_.node).get(VNodeTranslated_.id)
                            )
                    }
                )
                .and(
                    Specification.where(
                            contains(filter.fullTextSearch, root.get(VUserTranslated_.name))
                        )
                        .or(contains(filter.fullTextSearch, root.get(VUserTranslated_.surname)))
                        .or(contains(filter.fullTextSearch, root.get(VUserTranslated_.login)))
                        .or(contains(filter.fullTextSearch, root.get(VUserTranslated_.function)))
                        .or(contains(filter.fullTextSearch, root.get(VUserTranslated_.phone)))
                        .or(contains(filter.fullTextSearch, root.get(VUserTranslated_.email)))
                )
                .and(
                    filter.roleId?.let {
                        equal(
                            it,
                            root.join(VUserTranslated_.authorizations)
                                .join(VAuthorizationTranslated_.role)
                                .get(VRoleTranslated_.id)
                        )
                    }
                )
                .and(equal(filter.enabled, root.get(VUserTranslated_.enabled)))

        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
