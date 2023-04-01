package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.LanguageDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleLanguageDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslationKeyMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreTranslationsAndNameKeys
import sk.esten.uss.gbco2.model.entity.language.Language
import sk.esten.uss.gbco2.model.entity.language.LanguageSuper
import sk.esten.uss.gbco2.model.entity.language.VLanguageTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface LanguageMapper : TranslationMapper {

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(
            target = "nameK",
            constant = "${LanguageSuper.translationPrefix}name",
            qualifiedBy = [TranslationKeyMapper::class]
        ),
        Mapping(target = "translations", ignore = true)
    )
    fun map(dto: LanguageDto): Language

    @IgnoreBaseAttributes
    @IgnoreTranslationsAndNameKeys
    @Mapping(target = "id", ignore = true)
    fun update(dto: LanguageDto, @MappingTarget entity: Language)

    @Mapping(
        source = "nameTranslated",
        target = "name",
    )
    @TranslatedLanguageMapper
    fun map(entity: VLanguageTranslated): LanguageDto

    @Mapping(
        source = "nameEn",
        target = "name",
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VLanguageTranslated): LanguageDto

    @SimpleMapper
    @TranslatedLanguageMapper
    @Mapping(
        source = "nameTranslated",
        target = "name",
    )
    fun mapToSimple(entity: VLanguageTranslated): SimpleLanguageDto

    @SimpleMapper
    @EnglishLanguageMapper
    @Mapping(
        source = "nameEn",
        target = "name",
    )
    fun mapToSimpleEn(entity: VLanguageTranslated): SimpleLanguageDto
}
