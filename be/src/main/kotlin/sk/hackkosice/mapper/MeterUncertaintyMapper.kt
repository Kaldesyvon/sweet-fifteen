package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateMeterUncertaintyDto
import sk.esten.uss.gbco2.dto.response.MeterUncertaintyDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.meter_uncertainty.MeterUncertainty
import sk.esten.uss.gbco2.model.entity.meter_uncertainty.VMeterUncertaintyTranslated
import sk.esten.uss.gbco2.service.UnitService

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [UnitMapper::class, UnitService::class]
)
interface MeterUncertaintyMapper {

    @Mapping(
        source = "entity.unit",
        target = "unit",
        qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
    )
    @TranslatedLanguageMapper
    fun map(entity: VMeterUncertaintyTranslated): MeterUncertaintyDto

    @Mapping(
        source = "entity.unit",
        target = "unit",
        qualifiedBy = [SimpleMapper::class, EnglishLanguageMapper::class]
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VMeterUncertaintyTranslated): MeterUncertaintyDto

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(source = "dto.unitId", target = "unit"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "meter", ignore = true)
    )
    fun map(dto: CreateMeterUncertaintyDto): MeterUncertainty

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(source = "dto.unitId", target = "unit"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "meter", ignore = true)
    )
    fun update(dto: CreateMeterUncertaintyDto, @MappingTarget entity: MeterUncertainty)

    @Mapping(target = "unit", ignore = true) fun map(entity: MeterUncertainty): MeterUncertaintyDto
}
