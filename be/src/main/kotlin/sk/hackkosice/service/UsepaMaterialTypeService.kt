package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateUsepaMaterialTypeDto
import sk.esten.uss.gbco2.dto.response.UsepaMaterialTypeDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.UsepaMaterialTypeMapper
import sk.esten.uss.gbco2.model.entity.usepa_material_type.UsepaMaterialType
import sk.esten.uss.gbco2.model.entity.usepa_material_type.VUsepaMaterialTypeTranslated
import sk.esten.uss.gbco2.model.repository.usepa_material_type.UsepaMaterialTypeERepository
import sk.esten.uss.gbco2.model.repository.usepa_material_type.UsepaMaterialTypeVRepository
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
class UsepaMaterialTypeService(
    override val entityRepository: UsepaMaterialTypeERepository,
    override val viewRepository: UsepaMaterialTypeVRepository,
    private val mapper: UsepaMaterialTypeMapper,
) :
    CrudServiceView<
        UsepaMaterialType,
        VUsepaMaterialTypeTranslated,
        UsepaMaterialTypeDto,
        UsepaMaterialTypeDto,
        CreateUsepaMaterialTypeDto,
        CreateUsepaMaterialTypeDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun viewToDto(
        entity: VUsepaMaterialTypeTranslated,
        translated: Boolean
    ): UsepaMaterialTypeDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: CreateUsepaMaterialTypeDto): UsepaMaterialType {
        val obj = mapper.map(createDto)
        newEnTranslation(obj.nameK, createDto.name, obj.nameTranslations)
        return obj
    }

    override fun updateEntity(updateDto: CreateUsepaMaterialTypeDto, entity: UsepaMaterialType) {
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.nameTranslations)
    }

    override fun viewToDetailDto(
        entity: VUsepaMaterialTypeTranslated,
        translated: Boolean
    ): UsepaMaterialTypeDto = viewToDto(entity, translated)
}
