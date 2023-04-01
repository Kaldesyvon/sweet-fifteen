package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.ScopeTypeSpecDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleScopeTypeSpecDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslationKeyMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreTranslationsAndNameKeys
import sk.esten.uss.gbco2.model.entity.scope_type_spec.ScopeTypeSpec
import sk.esten.uss.gbco2.model.entity.scope_type_spec.ScopeTypeSpecSuper
import sk.esten.uss.gbco2.model.entity.scope_type_spec.VScopeTypeSpecTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface ScopeTypeSpecMapper : TranslationMapper {

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(
            target = "nameK",
            constant = "${ScopeTypeSpecSuper.translationPrefix}name",
            qualifiedBy = [TranslationKeyMapper::class]
        ),
        Mapping(target = "id", ignore = true),
        Mapping(target = "translations", ignore = true)
    )
    fun map(dto: ScopeTypeSpecDto): ScopeTypeSpec

    @IgnoreBaseAttributes
    @IgnoreTranslationsAndNameKeys
    @Mapping(target = "id", ignore = true)
    fun update(dto: ScopeTypeSpecDto, @MappingTarget entity: ScopeTypeSpec)

    @Mapping(
        source = "nameTranslated",
        target = "name",
    )
    @TranslatedLanguageMapper
    fun map(entity: VScopeTypeSpecTranslated): ScopeTypeSpecDto

    @Mapping(
        source = "nameEn",
        target = "name",
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VScopeTypeSpecTranslated): ScopeTypeSpecDto

    @Mapping(
        source = "nameTranslated",
        target = "name",
    )
    @TranslatedLanguageMapper
    @SimpleMapper
    fun mapSimple(entity: VScopeTypeSpecTranslated): SimpleScopeTypeSpecDto

    @Mapping(
        source = "nameEn",
        target = "name",
    )
    @EnglishLanguageMapper
    @SimpleMapper
    fun mapSimpleEn(entity: VScopeTypeSpecTranslated): SimpleScopeTypeSpecDto
}
