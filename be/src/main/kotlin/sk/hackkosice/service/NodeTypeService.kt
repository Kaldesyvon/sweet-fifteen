package sk.esten.uss.gbco2.service

import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateNodeTypeDto
import sk.esten.uss.gbco2.dto.request.filter.NodeTypeFilter
import sk.esten.uss.gbco2.dto.response.NodeTypeDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.NodeTypeMapper
import sk.esten.uss.gbco2.model.entity.node_type.NodeType
import sk.esten.uss.gbco2.model.entity.node_type.VNodeTypeTranslated
import sk.esten.uss.gbco2.model.repository.node_type.AdvNodeTypeVRepository
import sk.esten.uss.gbco2.model.repository.node_type.NodeTypeERepository
import sk.esten.uss.gbco2.model.repository.node_type.NodeTypeVRepository
import sk.esten.uss.gbco2.model.specification.NodeTypeSpecification
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.principalLangOrEn
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
class NodeTypeService(
    override val entityRepository: NodeTypeERepository,
    override val viewRepository: NodeTypeVRepository,
    private val advNodeTypeRepository: AdvNodeTypeVRepository,
    private val mapper: NodeTypeMapper,
) :
    CrudServiceView<
        NodeType,
        VNodeTypeTranslated,
        NodeTypeDto,
        NodeTypeDto,
        CreateNodeTypeDto,
        CreateNodeTypeDto,
        Long,
        NodeTypeFilter,
        ReadAllParamsFilterDto>() {

    override fun viewToDto(entity: VNodeTypeTranslated, translated: Boolean): NodeTypeDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: CreateNodeTypeDto): NodeType {
        val obj = mapper.map(createDto)
        newEnTranslation(obj.nameK, createDto.name, obj.translations)
        return obj
    }

    override fun updateEntity(updateDto: CreateNodeTypeDto, entity: NodeType) {
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.translations)
    }

    override fun repositoryPageQuery(filter: NodeTypeFilter): Page<VNodeTypeTranslated> {
        val specification = NodeTypeSpecification(filter)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    override fun viewToDetailDto(entity: VNodeTypeTranslated, translated: Boolean): NodeTypeDto =
        viewToDto(entity, translated)

    fun getAllWithMaterialReport(nodeIds: List<Long>?): List<NodeTypeDto> =
        advNodeTypeRepository.findAllByMaterialReportAndLanguageId(principalLangOrEn().id, nodeIds)
            .map { mapper.map(it) }
}
