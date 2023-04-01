package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.MeterTypeDto
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.mapper.MeterTypeMapper
import sk.esten.uss.gbco2.model.entity.meter_type.MeterType
import sk.esten.uss.gbco2.model.repository.meter_type.MeterTypeRepository

@Service
class MeterTypeService(
    override val entityRepository: MeterTypeRepository,
    private val mapper: MeterTypeMapper,
) :
    CrudService<
        MeterType,
        MeterTypeDto,
        MeterTypeDto,
        MeterTypeDto,
        MeterTypeDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun entityToDto(entity: MeterType, translated: Boolean): MeterTypeDto {
        return mapper.map(entity)
    }

    override fun createEntity(createDto: MeterTypeDto): MeterType {
        return mapper.map(createDto)
    }

    override fun updateEntity(updateDto: MeterTypeDto, entity: MeterType) {
        mapper.update(updateDto, entity)
    }

    override fun entityToDetailDto(entity: MeterType, translated: Boolean): MeterTypeDto =
        entityToDto(entity, translated)
}
