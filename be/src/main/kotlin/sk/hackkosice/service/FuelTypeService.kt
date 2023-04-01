package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.FuelTypeDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.FuelTypeMapper
import sk.esten.uss.gbco2.model.entity.fuel_type.FuelType
import sk.esten.uss.gbco2.model.entity.fuel_type.VFuelTypeTranslated
import sk.esten.uss.gbco2.model.repository.fuel_type.FuelTypeERepository
import sk.esten.uss.gbco2.model.repository.fuel_type.FuelTypeVRepository
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
class FuelTypeService(
    override val entityRepository: FuelTypeERepository,
    override val viewRepository: FuelTypeVRepository,
    private val mapper: FuelTypeMapper,
) :
    CrudServiceView<
        FuelType,
        VFuelTypeTranslated,
        FuelTypeDto,
        FuelTypeDto,
        FuelTypeDto,
        FuelTypeDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {
    override fun viewToDto(entity: VFuelTypeTranslated, translated: Boolean): FuelTypeDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: FuelTypeDto): FuelType {
        val obj = mapper.map(createDto)
        newEnTranslation(obj.nameK, createDto.name, obj.translations)
        return obj
    }

    override fun updateEntity(updateDto: FuelTypeDto, entity: FuelType) {
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.translations)
    }

    override fun viewToDetailDto(entity: VFuelTypeTranslated, translated: Boolean): FuelTypeDto =
        viewToDto(entity, translated)
}
