package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.AdvDetailDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.adv_details.AdvDetails
import sk.esten.uss.gbco2.model.entity.adv_details.VAdvDetailsTranslated
import sk.esten.uss.gbco2.model.entity.results_month_adv.VResultsMonthAdv
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses =
        [
            AdvStatusMapper::class,
            MaterialMapper::class,
            AnalysisParamMapper::class,
            NodeMapper::class]
)
interface AdvDetailsMapper {
    @IgnoreBaseAttributes
    @Mappings(
        Mapping(source = "unitNumen", target = "unitDenominator"),
        Mapping(source = "unit", target = "unitNumenator"),
        Mapping(target = "factorA", ignore = true),
        Mapping(target = "factorB", ignore = true),
        Mapping(target = "valIntensity", ignore = true),
        Mapping(target = "valIntensityMin", ignore = true),
        Mapping(target = "valIntensityMax", ignore = true),
        Mapping(target = "valIntensityMonths", ignore = true),
        Mapping(target = "advStatus", ignore = true),
        Mapping(target = "materialBasis", ignore = true),
        Mapping(target = "intensityStd", ignore = true),
        Mapping(target = "advParams", ignore = true),
        Mapping(target = "intensityMean", ignore = true),
        Mapping(target = "advValid", ignore = true),
        Mapping(target = "markedAsValidBy", ignore = true),
        Mapping(target = "markedAsValid", ignore = true),
        Mapping(target = "materialNode", ignore = true),
    )
    fun update(entity: VResultsMonthAdv): AdvDetails

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(source = "unitNumen", target = "unitDenominator"),
        Mapping(source = "unit", target = "unitNumenator"),
        Mapping(target = "factorA", ignore = true),
        Mapping(target = "factorB", ignore = true),
        Mapping(target = "valIntensity", ignore = true),
        Mapping(target = "valIntensityMin", ignore = true),
        Mapping(target = "valIntensityMax", ignore = true),
        Mapping(target = "valIntensityMonths", ignore = true),
        Mapping(target = "advStatus", ignore = true),
        Mapping(target = "materialBasis", ignore = true),
        Mapping(target = "intensityStd", ignore = true),
        Mapping(target = "advParams", ignore = true),
        Mapping(target = "intensityMean", ignore = true),
        Mapping(target = "advValid", ignore = true),
        Mapping(target = "markedAsValidBy", ignore = true),
        Mapping(target = "markedAsValid", ignore = true),
        Mapping(target = "materialNode", ignore = true),
    )
    fun map(entity: VResultsMonthAdv): AdvDetails

    @TranslatedLanguageMapper
    @Mappings(
        Mapping(
            target = "status",
            source = "status",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            target = "node",
            source = "node",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            target = "material",
            source = "material",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            target = "analysisParam",
            source = "analysisParam",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            target = "basisMaterial",
            source = "basisMaterial",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(target = "unitAbbr", source = "unit.abbr"),
    )
    fun map(entity: VAdvDetailsTranslated): AdvDetailDto

    @EnglishLanguageMapper
    @Mappings(
        Mapping(target = "status", source = "status", qualifiedBy = [EnglishLanguageMapper::class]),
        Mapping(
            target = "node",
            source = "node",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            target = "material",
            source = "material",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            target = "analysisParam",
            source = "analysisParam",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            target = "basisMaterial",
            source = "basisMaterial",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(target = "unitAbbr", source = "unit.abbr"),
    )
    fun mapEn(entity: VAdvDetailsTranslated): AdvDetailDto
}
