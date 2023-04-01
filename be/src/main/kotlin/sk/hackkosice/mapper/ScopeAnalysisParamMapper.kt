package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.ScopeAnalysisParamDto
import sk.esten.uss.gbco2.mapper.annotation.CommonMapper
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.scope_analysis_param.VScopeAnalysisParamTranslated
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [AnalysisParamMapper::class]
)
interface ScopeAnalysisParamMapper {

    @Mappings(
        Mapping(
            source = "analysisParam",
            target = "analysisParam",
            qualifiedBy = [TranslatedLanguageMapper::class, CommonMapper::class]
        ),
        Mapping(target = "scope", ignore = true)
    )
    @TranslatedLanguageMapper
    fun map(entity: VScopeAnalysisParamTranslated): ScopeAnalysisParamDto

    @Mappings(
        Mapping(
            source = "analysisParam",
            target = "analysisParam",
            qualifiedBy = [EnglishLanguageMapper::class, CommonMapper::class]
        ),
        Mapping(target = "scope", ignore = true)
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VScopeAnalysisParamTranslated): ScopeAnalysisParamDto

    @IterableMapping(qualifiedBy = [TranslatedLanguageMapper::class])
    @TranslatedLanguageMapper
    fun map(entity: Set<VScopeAnalysisParamTranslated>): Set<ScopeAnalysisParamDto>

    @IterableMapping(qualifiedBy = [EnglishLanguageMapper::class])
    @EnglishLanguageMapper
    fun mapEn(entity: Set<VScopeAnalysisParamTranslated>): Set<ScopeAnalysisParamDto>
}
