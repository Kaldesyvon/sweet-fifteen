package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.MaterialTypeDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.MaterialTypeMapper
import sk.esten.uss.gbco2.model.entity.material_type.MaterialType
import sk.esten.uss.gbco2.model.entity.material_type.VMaterialTypeTranslated
import sk.esten.uss.gbco2.model.repository.material_type.MaterialTypeERepository
import sk.esten.uss.gbco2.model.repository.material_type.MaterialTypeVRepository
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
class MaterialTypeService(
    private val mapper: MaterialTypeMapper,
    override val entityRepository: MaterialTypeERepository,
    override val viewRepository: MaterialTypeVRepository,
) :
    CrudServiceView<
        MaterialType,
        VMaterialTypeTranslated,
        MaterialTypeDto,
        MaterialTypeDto,
        MaterialTypeDto,
        MaterialTypeDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun createEntity(createDto: MaterialTypeDto): MaterialType {
        val obj = mapper.map(createDto)
        newEnTranslation(obj.nameK, createDto.name, obj.translations)
        return obj
    }

    override fun updateEntity(updateDto: MaterialTypeDto, entity: MaterialType) {
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.translations)
    }

    override fun viewToDto(entity: VMaterialTypeTranslated, translated: Boolean): MaterialTypeDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun viewToDetailDto(
        entity: VMaterialTypeTranslated,
        translated: Boolean
    ): MaterialTypeDto = viewToDto(entity, translated)

    fun getAllMaterialTypesByMaterialType(materialTypeIds: List<Long>): List<MaterialType> {
        return entityRepository.findAllByIdIn(materialTypeIds)
    }
}
