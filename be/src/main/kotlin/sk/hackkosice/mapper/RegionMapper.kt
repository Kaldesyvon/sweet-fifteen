package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateRegionDto
import sk.esten.uss.gbco2.dto.response.RegionDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleRegionDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslationKeyMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreTranslationsAndNameKeys
import sk.esten.uss.gbco2.model.entity.region.Region
import sk.esten.uss.gbco2.model.entity.region.RegionSuper
import sk.esten.uss.gbco2.model.entity.region.VRegionTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface RegionMapper : TranslationMapper {

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(
            target = "nameK",
            constant = "${RegionSuper.translationPrefix}name",
            qualifiedBy = [TranslationKeyMapper::class]
        ),
        Mapping(target = "translations", ignore = true),
        Mapping(target = "id", ignore = true)
    )
    fun map(regionDto: RegionDto): Region

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(
            target = "nameK",
            constant = "${RegionSuper.translationPrefix}name",
            qualifiedBy = [TranslationKeyMapper::class]
        ),
        Mapping(target = "translations", ignore = true),
        Mapping(target = "id", ignore = true)
    )
    fun map(createRegionDto: CreateRegionDto): Region

    @IgnoreBaseAttributes
    @IgnoreTranslationsAndNameKeys
    @Mapping(target = "id", ignore = true)
    fun update(dto: CreateRegionDto, @MappingTarget entity: Region)

    @Mapping(source = "nameTranslated", target = "name")
    @TranslatedLanguageMapper
    fun map(entity: VRegionTranslated): RegionDto

    @Mapping(source = "nameEn", target = "name")
    @EnglishLanguageMapper
    fun mapEn(entity: VRegionTranslated): RegionDto

    @Mapping(source = "nameTranslated", target = "name")
    @TranslatedLanguageMapper
    @SimpleMapper
    fun mapSimple(entity: VRegionTranslated): SimpleRegionDto

    @Mapping(source = "nameEn", target = "name")
    @EnglishLanguageMapper
    @SimpleMapper
    fun mapSimpleEn(entity: VRegionTranslated): SimpleRegionDto
}
