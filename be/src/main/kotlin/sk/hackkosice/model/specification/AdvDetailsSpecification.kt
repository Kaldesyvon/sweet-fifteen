package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.AdvDetailsFilter
import sk.esten.uss.gbco2.exceptions.ValidationException
import sk.esten.uss.gbco2.model.entity.adv_details.VAdvDetailsTranslated
import sk.esten.uss.gbco2.model.entity.adv_details.VAdvDetailsTranslated_
import sk.esten.uss.gbco2.model.entity.material.VMaterialTranslated_
import sk.esten.uss.gbco2.model.entity.material_node.MaterialNode_
import sk.esten.uss.gbco2.model.entity.node.Node_
import sk.esten.uss.gbco2.model.entity.quantity.IOEnum
import sk.esten.uss.gbco2.utils.Constants
import sk.esten.uss.gbco2.utils.principalLangOrEn

class AdvDetailsSpecification(private val filter: AdvDetailsFilter) :
    GenericSpecification<VAdvDetailsTranslated> {
    override fun toPredicate(
        root: Root<VAdvDetailsTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {

        val specification: Specification<VAdvDetailsTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VAdvDetailsTranslated_.languageId))
                )
                .and(
                    equal(Constants.METRIC_UNIT_SET_ID, root.get(VAdvDetailsTranslated_.unitSetId))
                )
                .and(equal(filter.nodeId, root.get(VAdvDetailsTranslated_.nodeId)))
                .and(equal(filter.materialId, root.get(VAdvDetailsTranslated_.materialId)))
                .and(
                    equal(filter.analysisParamId, root.get(VAdvDetailsTranslated_.analysisParamId))
                )
                .and(equal(filter.statusId, root.get(VAdvDetailsTranslated_.statusId)))
                .and(
                    greaterThanOrEqualTo(
                        filter.from?.withDayOfMonth(1),
                        root.get(VAdvDetailsTranslated_.month)
                    )
                )
                .and(
                    lessThanOrEqualTo(
                        filter.to?.withDayOfMonth(
                            filter.to?.month?.length(filter.to?.isLeapYear ?: false) ?: 28
                        ),
                        root.get(VAdvDetailsTranslated_.month)
                    )
                )
                .and(filterIO(filter.io, filter.nodeId, root))

        return specification.toPredicate(root, query, criteriaBuilder)
    }

    private fun filterIO(
        io: IOEnum?,
        nodeId: Long?,
        root: Root<VAdvDetailsTranslated>
    ): Specification<VAdvDetailsTranslated>? {
        return io?.let {
            val materialJoin = root.join(VAdvDetailsTranslated_.material, JoinType.LEFT)
            val materialNodeJoin =
                materialJoin.join(VMaterialTranslated_.materialNodes, JoinType.LEFT)
            val nodeJoin = materialNodeJoin.join(MaterialNode_.node, JoinType.LEFT)
            when (it) {
                IOEnum.INPUT ->
                    Specification.where(equal(true, materialNodeJoin.get(MaterialNode_.input)))
                        .and(equal(nodeId, nodeJoin.get(Node_.id)))
                IOEnum.OUTPUT ->
                    Specification.where(equal(true, materialNodeJoin.get(MaterialNode_.output)))
                        .and(equal(nodeId, nodeJoin.get(Node_.id)))
                IOEnum.DUST_FROM_CHIMNEYS -> {
                    throw ValidationException(
                        description = "io value 'DUST_FROM_CHIMNEYS' is not allowed!"
                    )
                }
            }
        }
    }
}
