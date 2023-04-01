package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.AuthorizationFilterDto
import sk.esten.uss.gbco2.model.entity.authorization.VAuthorizationTranslated
import sk.esten.uss.gbco2.model.entity.authorization.VAuthorizationTranslated_
import sk.esten.uss.gbco2.model.entity.node.VNodeTranslated_
import sk.esten.uss.gbco2.model.entity.role.VRoleTranslated_
import sk.esten.uss.gbco2.model.entity.user.VUserTranslated_
import sk.esten.uss.gbco2.utils.principalLangOrEn

class AuthorizationSpecification(private val filter: AuthorizationFilterDto) :
    GenericSpecification<VAuthorizationTranslated> {
    override fun toPredicate(
        root: Root<VAuthorizationTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val specification: Specification<VAuthorizationTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VAuthorizationTranslated_.languageId))
                )
                .and(
                    equal(
                        filter.nodeId,
                        root.get(VAuthorizationTranslated_.node).get(VNodeTranslated_.id)
                    )
                )
                .and(
                    equal(
                        filter.roleId,
                        root.get(VAuthorizationTranslated_.role).get(VRoleTranslated_.id)
                    )
                )
                .and(
                    Specification.where(
                            equal(
                                filter.login,
                                root.get(VAuthorizationTranslated_.user).get(VUserTranslated_.login)
                            )
                        )
                        .or(
                            equal(
                                filter.login,
                                root.get(VAuthorizationTranslated_.user)
                                    .get(VUserTranslated_.loginAd)
                            )
                        )
                )

        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
