package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.filter.MaterialNodeTypeReadAllFilter
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialTypeDto
import sk.esten.uss.gbco2.mapper.MaterialTypeMapper
import sk.esten.uss.gbco2.model.entity.material_node_type.MaterialNodeType
import sk.esten.uss.gbco2.model.repository.material_node_type.MaterialNodeTypeERepository
import sk.esten.uss.gbco2.model.repository.material_type.MaterialTypeVRepository
import sk.esten.uss.gbco2.utils.principalLangOrEn

@Service
class MaterialNodeTypeService(
    val materialTypeViewRepository: MaterialTypeVRepository,
    val entityRepository: MaterialNodeTypeERepository,
    val mapper: MaterialTypeMapper
) {
    fun getAllMaterialTypesByMaterialId(
        filter: MaterialNodeTypeReadAllFilter?
    ): List<SimpleMaterialTypeDto> {
        return filter?.let {
            materialTypeViewRepository.getAllMaterialTypesByMaterialId(
                    it.materialId,
                    principalLangOrEn().id
                )
                .map { proj -> mapper.mapProjection(proj) }
        }
            ?: throw IllegalStateException("MaterialTypeReadAllFilter can not be null")
    }

    fun getMaterialNodeIdsByMaterialType(materialTypeId: Long): List<Long> {
        return entityRepository.getMaterialNodeIdsByMaterialType(materialTypeId)
    }

    fun getAllByMaterialNodeId(materialNodeId: Long): MutableSet<MaterialNodeType> {
        return entityRepository.getAllByMaterialNodeId(materialNodeId)
    }
}
