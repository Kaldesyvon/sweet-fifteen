package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.AnalysisParamExprDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisParamExprDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.analysis_param_expr.VAnalysisParamExprTranslated
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [AnalysisParamMapper::class]
)
interface AnalysisParamExprMapper {

    @Mappings(
        Mapping(
            source = "analysisParam",
            target = "analysisParam",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        )
    )
    @TranslatedLanguageMapper
    fun map(entity: VAnalysisParamExprTranslated): AnalysisParamExprDto

    @Mappings(
        Mapping(
            source = "analysisParam",
            target = "analysisParam",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        )
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VAnalysisParamExprTranslated): AnalysisParamExprDto

    @SimpleMapper fun mapSimple(entity: VAnalysisParamExprTranslated): SimpleAnalysisParamExprDto

    @IterableMapping(qualifiedBy = [SimpleMapper::class])
    @SimpleMapper
    fun mapToSimple(entity: Set<VAnalysisParamExprTranslated>): List<SimpleAnalysisParamExprDto>
}
