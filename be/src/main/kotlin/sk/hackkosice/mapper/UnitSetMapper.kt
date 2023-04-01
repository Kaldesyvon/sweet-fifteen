package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateUnitSetDto
import sk.esten.uss.gbco2.dto.response.UnitSetDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.NullableTranslationMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslationKeyMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreMemoTranslationsAndMemoKeys
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreNameTranslationsAndNameKeys
import sk.esten.uss.gbco2.model.entity.unit_set.UnitSet
import sk.esten.uss.gbco2.model.entity.unit_set.UnitSetSuper
import sk.esten.uss.gbco2.model.entity.unit_set.VUnitSetTranslated
import sk.esten.uss.gbco2.service.UnitSetService

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [UnitSetService::class]
)
interface UnitSetMapper : TranslationMapper {

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(target = "nameTranslations", ignore = true),
        Mapping(
            target = "memoTranslations",
            ignore = true,
            qualifiedBy = [NullableTranslationMapper::class]
        ),
        Mapping(
            target = "nameK",
            constant = "${UnitSetSuper.translationPrefix}name",
            qualifiedBy = [TranslationKeyMapper::class]
        ),
        Mapping(
            target = "memoK",
            constant = "${UnitSetSuper.translationPrefix}memo",
            qualifiedBy = [TranslationKeyMapper::class]
        )
    )
    fun map(dto: CreateUnitSetDto): UnitSet

    @Mappings(
        Mapping(source = "nameEn", target = "name"),
        Mapping(
            source = "memoEn",
            target = "memo",
            qualifiedBy = [NullableTranslationMapper::class]
        )
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VUnitSetTranslated): UnitSetDto

    @Mappings(
        Mapping(source = "nameTranslated", target = "name"),
        Mapping(
            source = "memoTranslated",
            target = "memo",
            qualifiedBy = [NullableTranslationMapper::class]
        )
    )
    @TranslatedLanguageMapper
    fun map(entity: VUnitSetTranslated): UnitSetDto

    @IgnoreBaseAttributes
    @IgnoreNameTranslationsAndNameKeys
    @IgnoreMemoTranslationsAndMemoKeys
    @Mappings(Mapping(target = "id", ignore = true))
    fun update(dto: UnitSetDto, @MappingTarget entity: UnitSet)
}
