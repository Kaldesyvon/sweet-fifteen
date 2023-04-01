package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateMeterConversionDto
import sk.esten.uss.gbco2.dto.request.update.UpdateMeterConversionDto
import sk.esten.uss.gbco2.dto.response.MeterConversionDto
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.mapper.MeterConversionMapper
import sk.esten.uss.gbco2.model.entity.meter_conversion.MeterConversion
import sk.esten.uss.gbco2.model.repository.meter_conversion.MeterConversionRepository

@Service
class MeterConversionService(
    private val mapper: MeterConversionMapper,
    override val entityRepository: MeterConversionRepository
) :
    CrudService<
        MeterConversion,
        MeterConversionDto,
        MeterConversionDto,
        CreateMeterConversionDto,
        UpdateMeterConversionDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun entityToDetailDto(
        entity: MeterConversion,
        translated: Boolean
    ): MeterConversionDto {
        return mapper.map(entity)
    }

    override fun entityToDto(entity: MeterConversion, translated: Boolean): MeterConversionDto {
        return mapper.map(entity)
    }

    override fun createEntity(createDto: CreateMeterConversionDto): MeterConversion {
        return mapper.map(createDto)
    }

    override fun updateEntity(updateDto: UpdateMeterConversionDto, entity: MeterConversion) {
        return mapper.update(updateDto, entity)
    }
}
