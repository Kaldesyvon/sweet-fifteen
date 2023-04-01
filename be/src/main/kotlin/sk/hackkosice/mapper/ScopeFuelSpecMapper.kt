package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.ScopeFuelSpecDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslationKeyMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreAttributesWithTranslations
import sk.esten.uss.gbco2.model.entity.scope_fuel_spec.ScopeFuelSpec
import sk.esten.uss.gbco2.model.entity.scope_fuel_spec.ScopeFuelSpecSuper
import sk.esten.uss.gbco2.model.entity.scope_fuel_spec.VScopeFuelSpecTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface ScopeFuelSpecMapper : TranslationMapper {

    @IgnoreAttributesWithTranslations
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(
            target = "nameK",
            constant = "${ScopeFuelSpecSuper.translationPrefix}name",
            qualifiedBy = [TranslationKeyMapper::class]
        )
    )
    fun map(dto: ScopeFuelSpecDto): ScopeFuelSpec

    @IgnoreAttributesWithTranslations
    @Mappings(Mapping(target = "id", ignore = true), Mapping(target = "nameK", ignore = true))
    fun update(dto: ScopeFuelSpecDto, @MappingTarget entity: ScopeFuelSpec)

    @Mapping(source = "nameTranslated", target = "name")
    @TranslatedLanguageMapper
    fun map(entity: VScopeFuelSpecTranslated): ScopeFuelSpecDto

    @Mapping(source = "nameEn", target = "name")
    @EnglishLanguageMapper
    fun mapEn(entity: VScopeFuelSpecTranslated): ScopeFuelSpecDto
}
