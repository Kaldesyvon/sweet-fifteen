package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.ScopeMaterialNodeFilter
import sk.esten.uss.gbco2.dto.response.ScopeMaterialNodeDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.ScopeMaterialNodeMapper
import sk.esten.uss.gbco2.model.entity.scope_material_node.ScopeMaterialNode
import sk.esten.uss.gbco2.model.entity.scope_material_node.VScopeMaterialNodeTranslated
import sk.esten.uss.gbco2.model.repository.scope_material_node.ScopeMaterialNodeERepository
import sk.esten.uss.gbco2.model.repository.scope_material_node.ScopeMaterialNodeVRepository
import sk.esten.uss.gbco2.model.specification.ScopeMaterialNodeSpecification

@Service
class ScopeMaterialNodeService(
    override val entityRepository: ScopeMaterialNodeERepository,
    override val viewRepository: ScopeMaterialNodeVRepository,
    private val mapper: ScopeMaterialNodeMapper
) :
    CrudServiceView<
        ScopeMaterialNode,
        VScopeMaterialNodeTranslated,
        ScopeMaterialNodeDto,
        ScopeMaterialNodeDto,
        ScopeMaterialNodeDto,
        ScopeMaterialNodeDto,
        Long,
        ScopeMaterialNodeFilter,
        ReadAllParamsFilterDto>() {
    override fun viewToDto(
        entity: VScopeMaterialNodeTranslated,
        translated: Boolean
    ): ScopeMaterialNodeDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun viewToDetailDto(
        entity: VScopeMaterialNodeTranslated,
        translated: Boolean
    ): ScopeMaterialNodeDto = viewToDto(entity, translated)

    override fun createEntity(createDto: ScopeMaterialNodeDto): ScopeMaterialNode {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun updateEntity(updateDto: ScopeMaterialNodeDto, entity: ScopeMaterialNode) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    override fun repositoryPageQuery(
        filter: ScopeMaterialNodeFilter
    ): Page<VScopeMaterialNodeTranslated> {
        val specification = ScopeMaterialNodeSpecification(filter)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    fun getMaterialNodeIdsByScope(scopeId: Long): List<Long> {
        return entityRepository.getMaterialNodeIdsByScope(scopeId)
    }

    fun getAllByMaterialNodeId(materialNodeId: Long): MutableSet<ScopeMaterialNode> {
        return entityRepository.findAllByMaterialNodeId(materialNodeId)
    }

    fun deleteAll(toDelete: Set<ScopeMaterialNode>) {
        entityRepository.deleteAllInBatch(toDelete)
    }

    fun saveAll(toSave: Set<ScopeMaterialNode>) {
        entityRepository.saveAllAndFlush(toSave)
    }
}
