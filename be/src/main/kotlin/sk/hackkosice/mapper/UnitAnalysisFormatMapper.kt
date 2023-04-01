package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.UnitAnalysisFormatDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleUnitAnalysisFormatDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslationKeyMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.unit_analysis_format.UnitAnalysisFormat
import sk.esten.uss.gbco2.model.entity.unit_analysis_format.UnitAnalysisFormatSuper
import sk.esten.uss.gbco2.model.entity.unit_analysis_format.VUnitAnalysisFormatTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface UnitAnalysisFormatMapper : TranslationMapper {

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(
            target = "nameK",
            constant = "${UnitAnalysisFormatSuper.translationPrefix}name",
            qualifiedBy = [TranslationKeyMapper::class]
        )
    )
    fun map(dto: UnitAnalysisFormatDto): UnitAnalysisFormat

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(target = "nameK", ignore = true),
    )
    fun update(dto: UnitAnalysisFormatDto, @MappingTarget entity: UnitAnalysisFormat)

    @Mappings(
        Mapping(target = "name", source = "nameTranslated"),
    )
    @TranslatedLanguageMapper
    @SimpleMapper
    fun mapSimple(entity: VUnitAnalysisFormatTranslated): SimpleUnitAnalysisFormatDto

    @Mappings(
        Mapping(source = "nameEn", target = "name"),
    )
    @EnglishLanguageMapper
    @SimpleMapper
    fun mapSimpleEn(entity: VUnitAnalysisFormatTranslated): SimpleUnitAnalysisFormatDto

    @Mappings(
        Mapping(source = "nameTranslated", target = "name"),
    )
    @TranslatedLanguageMapper
    fun map(entity: VUnitAnalysisFormatTranslated): UnitAnalysisFormatDto

    @Mappings(
        Mapping(source = "nameEn", target = "name"),
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VUnitAnalysisFormatTranslated): UnitAnalysisFormatDto
}
