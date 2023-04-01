package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.MeterTypeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMeterTypeDto
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.meter_type.MeterType

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface MeterTypeMapper {

    @IgnoreBaseAttributes fun map(dto: MeterTypeDto): MeterType

    fun map(entity: MeterType): MeterTypeDto

    @IgnoreBaseAttributes
    @Mapping(target = "id", ignore = true)
    fun update(dto: MeterTypeDto, @MappingTarget entity: MeterType)

    @SimpleMapper fun mapSimple(entity: MeterType): SimpleMeterTypeDto
}
