package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateMeterConversionDto
import sk.esten.uss.gbco2.dto.request.update.UpdateMeterConversionDto
import sk.esten.uss.gbco2.dto.response.MeterConversionDto
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.meter_conversion.MeterConversion
import sk.esten.uss.gbco2.service.MeterService
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [MeterService::class, MeterMapper::class]
)
interface MeterConversionMapper {

    @Mapping(source = "entity.meter", target = "meter", qualifiedBy = [SimpleMapper::class])
    fun map(entity: MeterConversion): MeterConversionDto

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(source = "meterId", target = "meter"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "objVersion", ignore = true)
    )
    fun map(createDto: CreateMeterConversionDto): MeterConversion

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(source = "meterId", target = "meter"),
        Mapping(target = "id", ignore = true),
    )
    fun update(updateDto: UpdateMeterConversionDto, @MappingTarget entity: MeterConversion)
}
