package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.ScopeProcessSpecDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslationKeyMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreTranslationsAndNameKeys
import sk.esten.uss.gbco2.model.entity.scope_process_spec.ScopeProcessSpec
import sk.esten.uss.gbco2.model.entity.scope_process_spec.ScopeProcessSpecSuper
import sk.esten.uss.gbco2.model.entity.scope_process_spec.VScopeProcessSpecTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface ScopeProcessSpecMapper : TranslationMapper {

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(
            target = "nameK",
            constant = "${ScopeProcessSpecSuper.translationPrefix}name",
            qualifiedBy = [TranslationKeyMapper::class]
        ),
        Mapping(target = "id", ignore = true),
        Mapping(target = "translations", ignore = true)
    )
    fun map(dto: ScopeProcessSpecDto): ScopeProcessSpec

    @IgnoreBaseAttributes
    @IgnoreTranslationsAndNameKeys
    @Mapping(target = "id", ignore = true)
    fun update(dto: ScopeProcessSpecDto, @MappingTarget entity: ScopeProcessSpec)

    @Mapping(source = "nameTranslated", target = "name")
    @TranslatedLanguageMapper
    fun map(entity: VScopeProcessSpecTranslated): ScopeProcessSpecDto

    @Mapping(source = "nameEn", target = "name")
    @EnglishLanguageMapper
    fun mapEn(entity: VScopeProcessSpecTranslated): ScopeProcessSpecDto
}
