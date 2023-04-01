package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.NodeLevelDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.NodeLevelMapper
import sk.esten.uss.gbco2.model.entity.node_level.NodeLevel
import sk.esten.uss.gbco2.model.entity.node_level.VNodeLevelTranslated
import sk.esten.uss.gbco2.model.repository.node_level.NodeLevelERepository
import sk.esten.uss.gbco2.model.repository.node_level.NodeLevelVRepository
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
class NodeLevelService(
    override val entityRepository: NodeLevelERepository,
    override val viewRepository: NodeLevelVRepository,
    private val mapper: NodeLevelMapper,
) :
    CrudServiceView<
        NodeLevel,
        VNodeLevelTranslated,
        NodeLevelDto,
        NodeLevelDto,
        NodeLevelDto,
        NodeLevelDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {
    override fun viewToDto(entity: VNodeLevelTranslated, translated: Boolean): NodeLevelDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: NodeLevelDto): NodeLevel {
        val obj = mapper.map(createDto)
        newEnTranslation(obj.nameK, createDto.name, obj.translations)
        return obj
    }

    override fun updateEntity(updateDto: NodeLevelDto, entity: NodeLevel) {
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.translations)
    }

    fun getNodeLevelIcon(id: Long): String? {
        return entityRepository.findById(id).map { mapper.mapIconToBase64(it.icon) }.orElse(null)
    }

    fun getNodeLevels(): List<NodeLevel> {
        return entityRepository.findAll()
    }

    override fun viewToDetailDto(entity: VNodeLevelTranslated, translated: Boolean): NodeLevelDto =
        viewToDto(entity, translated)
}
