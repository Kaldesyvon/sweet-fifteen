package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateAnalysisDto
import sk.esten.uss.gbco2.dto.request.update.UpdateAnalysisDto
import sk.esten.uss.gbco2.dto.response.AnalysisDto
import sk.esten.uss.gbco2.dto.response.detail.AnalysisDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisDto
import sk.esten.uss.gbco2.mapper.annotation.CommonMapper
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.analysis.Analysis
import sk.esten.uss.gbco2.model.entity.analysis.VAnalysisTranslated
import sk.esten.uss.gbco2.service.*
import sk.esten.uss.gbco2.service.MaterialConversionService

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses =
        [
            AnalysisParamMapper::class,
            UnitMapper::class,
            MaterialNodeMapper::class,
            MaterialConversionMapper::class,
            MaterialMapper::class,
            NodeMapper::class,
            QuantityMapper::class,
            UnitService::class,
            MaterialConversionService::class,
            MaterialService::class,
            NodeService::class,
            AnalysisParamService::class]
)
interface AnalysisMapper {

    @Mappings(
        Mapping(
            source = "unitDenominator",
            target = "unitDenominator",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "unitNumenator",
            target = "unitNumenator",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "materialNode",
            target = "materialNode",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "materialNode.material",
            target = "material",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "materialNode.node",
            target = "node",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "analysisParam",
            target = "analysisParam",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        )
    )
    @EnglishLanguageMapper
    @CommonMapper
    fun mapEn(entity: VAnalysisTranslated): AnalysisDto

    @Mappings(
        Mapping(
            source = "unitDenominator",
            target = "unitDenominator",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "unitNumenator",
            target = "unitNumenator",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "materialNode",
            target = "materialNode",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "materialNode.material",
            target = "material",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "materialNode.node",
            target = "node",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "analysisParam",
            target = "analysisParam",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        )
    )
    @TranslatedLanguageMapper
    @CommonMapper
    fun map(entity: VAnalysisTranslated): AnalysisDto

    @Mappings(
        Mapping(
            source = "unitDenominator",
            target = "unitDenominator",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "unitNumenator",
            target = "unitNumenator",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "materialNode",
            target = "materialNode",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "materialNode.material",
            target = "material",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "materialNode.node",
            target = "node",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "analysisParam",
            target = "analysisParam",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "materialConversion",
            target = "materialConversion",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.analysisParam.unitAnalysisFormat.abbr",
            target = "unitAnalysisFormatAbbr",
        ),
        Mapping(
            target = "factorA",
            expression =
                "java(entity.getFactorA().multiply(entity.getAnalysisParam().getUnitAnalysisFormat().getKoef()))"
        ),
        Mapping(
            target = "factorB",
            expression =
                "java(entity.getFactorB() == null ? null : entity.getFactorB().multiply(entity.getAnalysisParam().getUnitAnalysisFormat().getKoef()))"
        ),
        Mapping(
            target = "factorC",
            expression =
                "java(entity.getFactorC() == null ? null : entity.getFactorC().multiply(entity.getAnalysisParam().getUnitAnalysisFormat().getKoef()))"
        ),
        Mapping(
            target = "formulaCalculationInputAnalyses",
            expression =
                "java(entity.getCalculated() == false ? java.util.Collections.emptySet() : mapAnalysesToAnalysesDto(entity.getFormulaCalculations().stream().map(sk.esten.uss.gbco2.model.entity.formula_calculation.VFormulaCalculationTranslated::getAnalysisInput).collect(java.util.stream.Collectors.toList())))"
        ),
        Mapping(
            target = "calcExpr",
            expression =
                "java(entity.getFormulaCalculations().stream().map(vFormulaCalculationTranslated -> vFormulaCalculationTranslated.getExpr()).filter(java.util.Objects::nonNull).findFirst().orElse(null))"
        )
    )
    @TranslatedLanguageMapper
    @Mapping(target = "quantities", ignore = true)
    fun mapDetail(entity: VAnalysisTranslated): AnalysisDetailDto

    @Mappings(
        Mapping(
            source = "unitDenominator",
            target = "unitDenominator",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "unitNumenator",
            target = "unitNumenator",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "materialNode",
            target = "materialNode",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "materialNode.material",
            target = "material",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "materialNode.node",
            target = "node",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "analysisParam",
            target = "analysisParam",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "materialConversion",
            target = "materialConversion",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.analysisParam.unitAnalysisFormat.abbr",
            target = "unitAnalysisFormatAbbr",
        ),
        Mapping(
            target = "factorA",
            expression =
                "java(entity.getFactorA().multiply(entity.getAnalysisParam().getUnitAnalysisFormat().getKoef()))"
        ),
        Mapping(
            target = "factorB",
            expression =
                "java(entity.getFactorB() == null ? null : entity.getFactorB().multiply(entity.getAnalysisParam().getUnitAnalysisFormat().getKoef()))"
        ),
        Mapping(
            target = "factorC",
            expression =
                "java(entity.getFactorC() == null ? null : entity.getFactorC().multiply(entity.getAnalysisParam().getUnitAnalysisFormat().getKoef()))"
        ),
        Mapping(
            target = "formulaCalculationInputAnalyses",
            expression =
                "java(entity.getCalculated() == false ? java.util.Collections.emptySet() : mapAnalysesToAnalysesDtoEn(entity.getFormulaCalculations().stream().map(sk.esten.uss.gbco2.model.entity.formula_calculation.VFormulaCalculationTranslated::getAnalysisInput).collect(java.util.stream.Collectors.toList())))"
        ),
        Mapping(
            target = "calcExpr",
            expression =
                "java(entity.getFormulaCalculations().stream().map(vFormulaCalculationTranslated -> vFormulaCalculationTranslated.getExpr()).filter(java.util.Objects::nonNull).findFirst().orElse(null))"
        )
    )
    @EnglishLanguageMapper
    @Mapping(target = "quantities", ignore = true)
    fun mapDetailEn(entity: VAnalysisTranslated): AnalysisDetailDto

    @IterableMapping(qualifiedBy = [TranslatedLanguageMapper::class, CommonMapper::class])
    @TranslatedLanguageMapper
    fun mapAnalysesToAnalysesDto(entity: List<VAnalysisTranslated?>): MutableSet<AnalysisDto>

    @IterableMapping(qualifiedBy = [EnglishLanguageMapper::class, CommonMapper::class])
    @EnglishLanguageMapper
    fun mapAnalysesToAnalysesDtoEn(entity: List<VAnalysisTranslated?>): MutableSet<AnalysisDto>

    @SimpleMapper fun mapSimple(entity: VAnalysisTranslated): SimpleAnalysisDto

    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(
            source = "materialConversionId",
            target = "materialConversion",
        ),
        Mapping(
            source = "unitDenominatorId",
            target = "unitDenominator",
        ),
        Mapping(
            source = "unitNumenatorId",
            target = "unitNumenator",
        ),
        Mapping(
            source = "analysisParamId",
            target = "analysisParam",
        ),
        Mapping(target = "created", ignore = true),
        Mapping(target = "createdBy", ignore = true),
        Mapping(target = "modified", ignore = true),
        Mapping(target = "modifiedBy", ignore = true),
        Mapping(target = "validTo", ignore = true),
        Mapping(target = "editable", ignore = true),
        Mapping(target = "calculated", ignore = true),
        Mapping(target = "remoteCode", ignore = true),
        Mapping(target = "materialNode", ignore = true),
    )
    fun map(dto: CreateAnalysisDto): Analysis

    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(
            source = "unitDenominatorId",
            target = "unitDenominator",
        ),
        Mapping(
            source = "unitNumenatorId",
            target = "unitNumenator",
        ),
        Mapping(target = "validTo", ignore = true),
        Mapping(target = "editable", ignore = true),
        Mapping(target = "calculated", ignore = true),
        Mapping(target = "remoteCode", ignore = true),
        Mapping(target = "materialConversion", ignore = true),
        Mapping(target = "materialNode", ignore = true),
        Mapping(target = "analysisParam", ignore = true),
    )
    @IgnoreBaseAttributes
    fun update(updateDto: UpdateAnalysisDto, @MappingTarget entity: Analysis): Analysis
}
