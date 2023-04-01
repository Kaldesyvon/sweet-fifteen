package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.CountryDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleCountryDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslationKeyMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreNameTranslationsAndNameKeys
import sk.esten.uss.gbco2.model.entity.country.Country
import sk.esten.uss.gbco2.model.entity.country.CountrySuper
import sk.esten.uss.gbco2.model.entity.country.VCountryTranslated
import sk.esten.uss.gbco2.service.RegionService

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [RegionMapper::class, RegionService::class]
)
interface CountryMapper {

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(
            target = "nameK",
            constant = "${CountrySuper.translationPrefix}name",
            qualifiedBy = [TranslationKeyMapper::class]
        ),
        Mapping(target = "id", ignore = true),
        Mapping(source = "regionId", target = "region"),
        Mapping(target = "nameTranslations", ignore = true)
    )
    fun map(dto: CountryDto): Country

    @IgnoreBaseAttributes
    @IgnoreNameTranslationsAndNameKeys
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(source = "regionId", target = "region")
    )
    fun update(dto: CountryDto, @MappingTarget entity: Country)

    @Mappings(
        Mapping(source = "nameEn", target = "name"),
        Mapping(source = "entity.region.id", target = "regionId"),
    )
    @EnglishLanguageMapper
    @SimpleMapper
    fun mapSimpleEn(entity: VCountryTranslated): SimpleCountryDto

    @Mappings(
        Mapping(source = "nameTranslated", target = "name"),
        Mapping(source = "entity.region.id", target = "regionId"),
    )
    @TranslatedLanguageMapper
    @SimpleMapper
    fun mapSimple(entity: VCountryTranslated): SimpleCountryDto

    @Mappings(
        Mapping(source = "nameTranslated", target = "name"),
        Mapping(source = "region.id", target = "regionId"),
        Mapping(
            source = "entity.region",
            target = "region",
            qualifiedBy = [TranslatedLanguageMapper::class]
        )
    )
    @TranslatedLanguageMapper
    fun map(entity: VCountryTranslated): CountryDto

    @Mappings(
        Mapping(source = "nameEn", target = "name"),
        Mapping(source = "region.id", target = "regionId"),
        Mapping(
            source = "entity.region",
            target = "region",
            qualifiedBy = [EnglishLanguageMapper::class]
        )
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VCountryTranslated): CountryDto
}
