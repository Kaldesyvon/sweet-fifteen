package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.FormulaCalculationDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleFormulaCalculationDto
import sk.esten.uss.gbco2.mapper.annotation.CommonMapper
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.formula_calculation.VFormulaCalculationTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [AnalysisMapper::class]
)
interface FormulaCalculationMapper {

    @Mappings(
        Mapping(
            source = "analysisCalculated",
            target = "analysisCalculated",
            qualifiedBy = [TranslatedLanguageMapper::class, CommonMapper::class]
        ),
        Mapping(
            source = "analysisInput",
            target = "analysisInput",
            qualifiedBy = [TranslatedLanguageMapper::class, CommonMapper::class]
        )
    )
    @TranslatedLanguageMapper
    fun map(entity: VFormulaCalculationTranslated): FormulaCalculationDto

    @Mappings(
        Mapping(
            source = "analysisInput",
            target = "analysisInput",
            qualifiedBy = [EnglishLanguageMapper::class, CommonMapper::class]
        ),
        Mapping(
            source = "analysisCalculated",
            target = "analysisCalculated",
            qualifiedBy = [EnglishLanguageMapper::class, CommonMapper::class]
        )
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VFormulaCalculationTranslated): FormulaCalculationDto

    @Mappings(
        Mapping(
            source = "analysisCalculated.id",
            target = "analysisCalculatedId",
        ),
        Mapping(
            source = "analysisInput.id",
            target = "analysisInputId",
        ),
        Mapping(
            source = "analysisInput.materialNodeNameTranslated",
            target = "analysisInputMaterialNodeName",
        ),
        Mapping(
            source = "analysisInput.analysisParamTranslated",
            target = "analysisInputAnalysisParamName",
        ),
        Mapping(
            source = "analysisInput.factorA",
            target = "analysisInputFactorA",
        ),
        Mapping(
            source = "analysisInput.factorB",
            target = "analysisInputFactorB",
        ),
        Mapping(
            source = "analysisInput.factorC",
            target = "analysisInputFactorC",
        ),
        Mapping(
            source = "analysisInput.validFrom",
            target = "analysisInputValidFrom",
        )
    )
    @SimpleMapper
    fun mapSimple(entity: VFormulaCalculationTranslated): SimpleFormulaCalculationDto
}
