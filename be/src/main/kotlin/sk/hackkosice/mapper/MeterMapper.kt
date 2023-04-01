package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateMeterDto
import sk.esten.uss.gbco2.dto.response.MeterDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMeterDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.meter.Meter
import sk.esten.uss.gbco2.model.entity.meter.VMeterTranslated
import sk.esten.uss.gbco2.service.FuelTypeService
import sk.esten.uss.gbco2.service.MeterTypeService
import sk.esten.uss.gbco2.service.NodeService
import sk.esten.uss.gbco2.service.UnitTypeService

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses =
        [
            UnitTypeMapper::class,
            FuelTypeMapper::class,
            MeterTypeMapper::class,
            MeterUncertaintyMapper::class,
            NodeMapper::class,
            UnitTypeService::class,
            FuelTypeService::class,
            MeterTypeService::class,
            NodeService::class]
)
interface MeterMapper {

    @Mappings(
        Mapping(
            source = "entity.unitType",
            target = "unitType",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "entity.meterType",
            target = "meterType",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "entity.fuelType",
            target = "fuelType",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "entity.meterUncertainty",
            target = "meterUncertainty",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "entity.nodeLocation",
            target = "nodeLocation",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        )
    )
    @TranslatedLanguageMapper
    fun map(entity: VMeterTranslated): MeterDto

    @Mappings(
        Mapping(
            source = "entity.unitType",
            target = "unitType",
            qualifiedBy = [SimpleMapper::class, EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "entity.meterType",
            target = "meterType",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "entity.fuelType",
            target = "fuelType",
            qualifiedBy = [SimpleMapper::class, EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "entity.meterUncertainty",
            target = "meterUncertainty",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "entity.nodeLocation",
            target = "nodeLocation",
            qualifiedBy = [SimpleMapper::class, EnglishLanguageMapper::class]
        )
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VMeterTranslated): MeterDto

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(source = "dto.unitTypeId", target = "unitType"),
        Mapping(source = "dto.meterTypeId", target = "meterType"),
        Mapping(source = "dto.fuelTypeId", target = "fuelType"),
        Mapping(source = "dto.nodeLocationId", target = "nodeLocation"),
        Mapping(target = "meterCertificates", ignore = true)
    )
    fun map(dto: CreateMeterDto): Meter

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(source = "dto.unitTypeId", target = "unitType"),
        Mapping(source = "dto.meterTypeId", target = "meterType"),
        Mapping(source = "dto.fuelTypeId", target = "fuelType"),
        Mapping(source = "dto.nodeLocationId", target = "nodeLocation"),
        Mapping(target = "meterCertificates", ignore = true)
    )
    fun update(dto: CreateMeterDto, @MappingTarget entity: Meter)

    @SimpleMapper fun mapSimple(entity: VMeterTranslated): SimpleMeterDto
    @SimpleMapper fun mapSimple(entity: Meter): SimpleMeterDto
}
