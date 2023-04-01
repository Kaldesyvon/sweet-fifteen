package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.FuelTypeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleFuelTypeDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslationKeyMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreAttributesWithTranslations
import sk.esten.uss.gbco2.model.entity.fuel_type.FuelType
import sk.esten.uss.gbco2.model.entity.fuel_type.FuelTypeSuper
import sk.esten.uss.gbco2.model.entity.fuel_type.VFuelTypeTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface FuelTypeMapper : TranslationMapper {

    @IgnoreAttributesWithTranslations
    @Mapping(
        target = "nameK",
        constant = "${FuelTypeSuper.translationPrefix}name",
        qualifiedBy = [TranslationKeyMapper::class]
    )
    fun map(dto: FuelTypeDto): FuelType

    @IgnoreAttributesWithTranslations
    @Mappings(Mapping(target = "id", ignore = true), Mapping(target = "nameK", ignore = true))
    fun update(dto: FuelTypeDto, @MappingTarget entity: FuelType)

    @Mapping(
        source = "nameTranslated",
        target = "name",
    )
    @TranslatedLanguageMapper
    fun map(entity: VFuelTypeTranslated): FuelTypeDto

    @Mapping(
        source = "nameEn",
        target = "name",
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VFuelTypeTranslated): FuelTypeDto

    @Mapping(source = "nameTranslated", target = "name")
    @TranslatedLanguageMapper
    @SimpleMapper
    fun mapToSimple(entity: VFuelTypeTranslated): SimpleFuelTypeDto

    @Mapping(
        source = "nameEn",
        target = "name",
    )
    @EnglishLanguageMapper
    @SimpleMapper
    fun mapToSimpleEn(entity: VFuelTypeTranslated): SimpleFuelTypeDto
}
