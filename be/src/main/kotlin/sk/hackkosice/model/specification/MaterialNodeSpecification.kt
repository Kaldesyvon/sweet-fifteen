package sk.esten.uss.gbco2.model.specification

import javax.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification
import sk.esten.uss.gbco2.dto.request.filter.MaterialNodeFilter
import sk.esten.uss.gbco2.model.entity.analysis_param.VAnalysisParamTranslated_
import sk.esten.uss.gbco2.model.entity.material.VMaterialTranslated_
import sk.esten.uss.gbco2.model.entity.material_node.VMaterialNodeTranslated
import sk.esten.uss.gbco2.model.entity.material_node.VMaterialNodeTranslated_
import sk.esten.uss.gbco2.model.entity.material_node_analysis_param.VMaterialNodeAnalysisParamTranslated_
import sk.esten.uss.gbco2.model.entity.node.VNodeTranslated_
import sk.esten.uss.gbco2.model.entity.unit_type.VUnitTypeTranslated_
import sk.esten.uss.gbco2.service.NodeService
import sk.esten.uss.gbco2.utils.idsInSpecification
import sk.esten.uss.gbco2.utils.principalLangOrEn

class MaterialNodeSpecification(
    private val filter: MaterialNodeFilter,
    private val nodeService: NodeService
) : GenericSpecification<VMaterialNodeTranslated> {
    override fun toPredicate(
        root: Root<VMaterialNodeTranslated>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {

        query.distinct(true)

        val material = root.join(VMaterialNodeTranslated_.material, JoinType.LEFT)
        val node = root.join(VMaterialNodeTranslated_.node, JoinType.LEFT)

        val specification: Specification<VMaterialNodeTranslated> =
            Specification.where(
                    equal(principalLangOrEn().id, root.get(VMaterialNodeTranslated_.languageId))
                )
                .and(
                    Specification.where(
                            contains(filter.fullTextSearch, root.get(VMaterialNodeTranslated_.name))
                        )
                        .or(
                            contains(
                                filter.fullTextSearch,
                                material.get(VMaterialTranslated_.nameTranslated)
                            )
                        )
                        .or(
                            contains(
                                filter.fullTextSearch,
                                node.get(VNodeTranslated_.nameTranslated)
                            )
                        )
                )
                .and(
                    filter.nodeId?.let {
                        nodeService
                            .getAllSubNodeIds(listOf(it))
                            .idsInSpecification(node.get(VNodeTranslated_.id))
                    }
                )
                .and(equal(filter.materialId, material.get(VMaterialTranslated_.id)))
                .and(booleanEqual(true, filter.product, root.get(VMaterialNodeTranslated_.product)))
                .and(booleanEqual(true, filter.input, root.get(VMaterialNodeTranslated_.input)))
                .and(booleanEqual(true, filter.output, root.get(VMaterialNodeTranslated_.output)))
                .and(
                    equal(
                        filter.unitTypeId,
                        material
                            .join(VMaterialTranslated_.unitType, JoinType.LEFT)
                            .get(VUnitTypeTranslated_.id)
                    )
                )
                .and(
                    equal(
                        filter.analysisParamId,
                        root.join(
                                VMaterialNodeTranslated_.materialNodeAnalysisParams,
                                JoinType.LEFT
                            )
                            .join(
                                VMaterialNodeAnalysisParamTranslated_.analysisParam,
                                JoinType.LEFT
                            )
                            .get(VAnalysisParamTranslated_.id)
                    )
                )
        return specification.toPredicate(root, query, criteriaBuilder)
    }
}
