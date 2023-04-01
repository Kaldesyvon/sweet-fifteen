package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.UnitSetSettingsApDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.unit_set_settings_ap.VUnitSetSettingsApTranslated
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [UnitSetMapper::class, UnitMapper::class, AnalysisParamMapper::class]
)
interface UnitSetSettingsApMapper {

    @Mappings(
        Mapping(
            source = "analysisParam",
            target = "analysisParam",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "unitSet",
            target = "unitSet",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "unit",
            target = "unit",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        )
    )
    @TranslatedLanguageMapper
    fun map(entity: VUnitSetSettingsApTranslated): UnitSetSettingsApDto

    @Mappings(
        Mapping(
            source = "analysisParam",
            target = "analysisParam",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "unitSet",
            target = "unitSet",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "unit",
            target = "unit",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        )
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VUnitSetSettingsApTranslated): UnitSetSettingsApDto

    @IterableMapping(qualifiedBy = [EnglishLanguageMapper::class])
    @EnglishLanguageMapper
    fun mapEn(entity: Set<VUnitSetSettingsApTranslated>): List<UnitSetSettingsApDto>

    @IterableMapping(qualifiedBy = [TranslatedLanguageMapper::class])
    @TranslatedLanguageMapper
    fun map(entity: Set<VUnitSetSettingsApTranslated>): List<UnitSetSettingsApDto>
}
