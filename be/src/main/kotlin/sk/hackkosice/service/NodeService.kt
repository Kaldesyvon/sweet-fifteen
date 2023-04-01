package sk.esten.uss.gbco2.service

import java.time.LocalDate
import org.apache.commons.lang3.NotImplementedException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateNodeDto
import sk.esten.uss.gbco2.dto.request.filter.NodeFilter
import sk.esten.uss.gbco2.dto.request.filter.NodeFilterTree
import sk.esten.uss.gbco2.dto.request.update.UpdateNodeDto
import sk.esten.uss.gbco2.dto.response.NodeDto
import sk.esten.uss.gbco2.dto.response.detail.NodeDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeTreeDto
import sk.esten.uss.gbco2.dto.response.tree.NodeTreeDto
import sk.esten.uss.gbco2.exceptions.ValidationException
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.NodeMapper
import sk.esten.uss.gbco2.model.entity.node.Node
import sk.esten.uss.gbco2.model.entity.node.VNodeTranslated
import sk.esten.uss.gbco2.model.entity.node.VNodeTranslated_
import sk.esten.uss.gbco2.model.repository.node.AdvNodeVRepository
import sk.esten.uss.gbco2.model.repository.node.NodeERepository
import sk.esten.uss.gbco2.model.repository.node.NodeVRepository
import sk.esten.uss.gbco2.model.specification.NodeSpecification
import sk.esten.uss.gbco2.utils.*
import sk.esten.uss.gbco2.utils.atEndOfYear
import sk.esten.uss.gbco2.utils.atStartOfYear

@Service
class NodeService(
    override val entityRepository: NodeERepository,
    override val viewRepository: NodeVRepository,
    private val advNodeVRepository: AdvNodeVRepository,
    private val mapper: NodeMapper,
) :
    CrudServiceView<
        Node,
        VNodeTranslated,
        NodeDto,
        NodeDetailDto,
        CreateNodeDto,
        UpdateNodeDto,
        Long,
        NodeFilter,
        ReadAllParamsFilterDto>() {
    override fun viewToDto(entity: VNodeTranslated, translated: Boolean): NodeDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: CreateNodeDto): Node {
        val validTo = createDto.validTo
        if (validTo != null && validTo.isBefore(createDto.validFrom)) {
            throw ValidationException(description = "Valid to cannot be set before valid from")
        }
        val entity = mapper.map(createDto)
        newEnTranslation(entity.nameK, createDto.name, entity.translations)
        return entity
    }

    @Transactional
    override fun updateInternal(id: Long, updateDto: UpdateNodeDto): NodeDto {
        val entity = entityRepository.findById(id).get()
        val validTo = updateDto.validTo
        if (validTo != null && validTo.isBefore(updateDto.validFrom)) {
            throw ValidationException(description = "Valid to cannot be set before valid from")
        }
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.translations)
        return persistInternal(entity)
    }

    override fun updateEntity(updateDto: UpdateNodeDto, entity: Node) {
        throw NotImplementedException("method is overridden")
    }

    @Transactional(readOnly = true)
    fun getAllSubNodeIds(nodeIds: List<Long>?): List<Long> {
        return entityRepository.findAllSubNodeIds(nodeIds)
    }

    @Transactional(readOnly = true)
    fun getAllSubNodeIds(nodeIds: List<Long>?, year: Int?): List<Long> {
        val dateFrom = year?.atStartOfYear()
        val dateTo = year?.atEndOfYear()
        return entityRepository.findAllSubNodeIds(nodeIds, dateFrom, dateTo)
    }

    @Transactional(readOnly = true)
    fun getAllSubNodeIds(nodeId: Long?): List<Long> {
        return viewRepository.findAll(
                viewRepository
                    .findAllSubNodes(nodeId?.let { listOf(it) }, principalLangOrEn().id)
                    .idsInSpecification(VNodeTranslated_.idView)
            )
            .mapNotNull { node: VNodeTranslated -> node.id }
    }

    @Transactional(readOnly = true)
    fun getAllValidSubNodeIds(nodeId: Long?, validFrom: LocalDate, validTo: LocalDate): List<Long> {
        return viewRepository.findAll(
                nodeId.let {
                    viewRepository
                        .findAllValidSubNodes(it, principalLangOrEn().id, validFrom, validTo)
                        .idsInSpecification(VNodeTranslated_.idView)
                }
            )
            .mapNotNull { node: VNodeTranslated -> node.id }
    }

    override fun repositoryPageQuery(filter: NodeFilter): Page<VNodeTranslated> {
        val specification = NodeSpecification(filter)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    @Transactional(readOnly = true)
    fun getChildrenOfParent(parentId: Long?, filter: ReadAllParamsFilterDto): List<NodeDto> {
        return if (parentId == null) {
            viewRepository
                .findAllRootNodes(principalLangOrEn().id, filter.toSort())
                .map { mapper.map(it) }
                .toList()
        } else {
            viewRepository
                .findAllByParentId(parentId, principalLangOrEn().id, filter.toSort())
                .map { mapper.map(it) }
                .toList()
        }
    }

    @Transactional(readOnly = true)
    fun getSimpleChildrenOfParent(
        parentId: Long?,
        filter: ReadAllParamsFilterDto
    ): List<SimpleNodeDto> {
        return if (parentId == null) {
            viewRepository
                .findAllRootNodes(principalLangOrEn().id, filter.toSort())
                .map { mapper.mapSimple(it) }
                .toList()
        } else {
            viewRepository
                .findAllByParentId(parentId, principalLangOrEn().id, filter.toSort())
                .map { mapper.mapSimple(it) }
                .toList()
        }
    }

    @Transactional(readOnly = true)
    fun getNodeTree(filter: NodeFilterTree): List<NodeTreeDto> {
        return viewRepository
            .findAll(
                advNodeVRepository.getNodeTree(filter).idsInSpecification(VNodeTranslated_.idView),
                filter.toSort()
            )
            .toNodeTree()
            .map { mapper.mapTree(it) }
    }

    @Transactional(readOnly = true)
    fun getSimpleNodeTree(filter: NodeFilterTree): List<SimpleNodeTreeDto> {
        return viewRepository
            .findAll(
                advNodeVRepository.getNodeTree(filter).idsInSpecification(VNodeTranslated_.idView),
                filter.toSort()
            )
            .toNodeTree()
            .map { mapper.mapSimpleTree(it) }
    }

    @Transactional(readOnly = true)
    fun getAllowedNodeIds(nodeId: Long, yearParam: Int): List<Long> {
        val filter =
            NodeFilterTree().apply {
                nodeIds = listOf(nodeId)
                year = yearParam
            }
        return viewRepository.findAll(
                advNodeVRepository.getNodeTree(filter).idsInSpecification(VNodeTranslated_.idView),
                filter.toSort()
            )
            .mapNotNull { it.id }
    }

    @Transactional(readOnly = true)
    fun getNodesByNodeLevel(filter: ReadAllParamsFilterDto?, nodeLevel: Long?): List<NodeDto> {
        return filter?.let { filterDto ->
            viewRepository.findAllByNodeLevel(nodeLevel, principalLangOrEn().id, filterDto.toSort())
                .map { mapper.map(it) }
        }
            ?: throw ValidationException(description = "Filter can not be null")
    }

    @Transactional(readOnly = true)
    fun getSimpleNodesByNodeLevel(
        filter: ReadAllParamsFilterDto?,
        nodeLevel: Long?
    ): List<SimpleNodeDto> {
        return filter?.let { filterDto ->
            viewRepository.findAllByNodeLevel(nodeLevel, principalLangOrEn().id, filterDto.toSort())
                .map { mapper.mapSimple(it) }
        }
            ?: throw ValidationException(description = "Filter can not be null")
    }

    override fun repositoryGetAllQuery(filter: ReadAllParamsFilterDto?): List<VNodeTranslated> {
        return if (filter is NodeFilterTree) {
            viewRepository.findAll(languageEqual(filter.toSort()))
        } else super.repositoryGetAllQuery(filter)
    }

    fun getAllNodesByNodeIds(nodeIds: List<Long>?, toSort: Sort): List<VNodeTranslated> {
        return viewRepository.findAll(
            viewRepository
                .findAllWithoutSubNodes(nodeIds, principalLangOrEn().id)
                .idsInSpecification(VNodeTranslated_.idView),
            toSort
        )
    }

    override fun viewToDetailDto(entity: VNodeTranslated, translated: Boolean): NodeDetailDto {
        return if (translated) mapper.mapDetail(entity) else mapper.mapDetailEn(entity)
    }

    fun getRootNodeId(): Long {
        return viewRepository.findByParentNodeIsNull()
    }

    fun getAllNodesToLevel(level: Int, year: Int, nodeId: Long?): List<Node> {
        return entityRepository.findAllNodesToLevel(level, year, nodeId)
    }

    fun getNodesByNodeTypes(nodeIds: List<Long>?): Map<String, String> =
        advNodeVRepository.getNodesWithNodeTypes(nodeIds)

    fun getAllNodesByYear(year: Int): List<Node?> {
        val dateFrom = year.atStartOfYear()
        val dateTo = year.atEndOfYear()
        return entityRepository.findAllByYear(dateFrom, dateTo)
    }

    @Transactional(readOnly = true)
    fun getTranslatedName(nodeId: Long, languageId: Long = principalLangOrEn().id ?: 1): String? {
        return viewRepository.findTranslationByIdAndLanguage(nodeId, languageId)
    }

    fun getAll(): List<Node> {
        return entityRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun getNodesInMaterialNode(nodeLevel: Long?): List<SimpleNodeDto> {
        return advNodeVRepository.getNodesInMaterialNode(nodeLevel)
    }
}
