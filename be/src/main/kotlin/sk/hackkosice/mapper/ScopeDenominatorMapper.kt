package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.ScopeDenominatorDto
import sk.esten.uss.gbco2.mapper.annotation.CommonMapper
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.scope_denominator.VScopeDenominatorTranslated
import sk.esten.uss.gbco2.utils.Constants.Companion.MAPPING_COMPONENT_MODEL_SPRING_LAZY

@Mapper(
    componentModel = MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [MaterialMapper::class]
)
interface ScopeDenominatorMapper {
    @Mappings(
        Mapping(
            source = "material",
            target = "material",
            qualifiedBy = [TranslatedLanguageMapper::class, CommonMapper::class]
        ),
        Mapping(target = "scope", ignore = true)
    )
    @TranslatedLanguageMapper
    fun map(entity: VScopeDenominatorTranslated): ScopeDenominatorDto

    @Mappings(
        Mapping(
            source = "material",
            target = "material",
            qualifiedBy = [EnglishLanguageMapper::class, CommonMapper::class]
        ),
        Mapping(target = "scope", ignore = true)
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VScopeDenominatorTranslated): ScopeDenominatorDto

    @IterableMapping(qualifiedBy = [TranslatedLanguageMapper::class])
    @TranslatedLanguageMapper
    fun map(entity: Set<VScopeDenominatorTranslated>): Set<ScopeDenominatorDto>

    @IterableMapping(qualifiedBy = [EnglishLanguageMapper::class])
    @EnglishLanguageMapper
    fun mapEn(entity: Set<VScopeDenominatorTranslated>): Set<ScopeDenominatorDto>
}
