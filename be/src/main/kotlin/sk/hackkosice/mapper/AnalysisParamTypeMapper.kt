package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.AnalysisParamTypeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisParamTypeDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.analysis_param_type.VAnalysisParamTypeTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface AnalysisParamTypeMapper {

    @Mappings(Mapping(source = "nameTranslated", target = "name"))
    @TranslatedLanguageMapper
    fun map(entity: VAnalysisParamTypeTranslated): AnalysisParamTypeDto

    @Mappings(Mapping(source = "nameEn", target = "name"))
    @EnglishLanguageMapper
    fun mapEn(entity: VAnalysisParamTypeTranslated): AnalysisParamTypeDto

    @Mappings(
        Mapping(source = "nameEn", target = "name"),
    )
    @EnglishLanguageMapper
    @SimpleMapper
    fun mapSimpleEn(entity: VAnalysisParamTypeTranslated): SimpleAnalysisParamTypeDto

    @Mappings(
        Mapping(source = "nameTranslated", target = "name"),
    )
    @TranslatedLanguageMapper
    @SimpleMapper
    fun mapSimple(entity: VAnalysisParamTypeTranslated): SimpleAnalysisParamTypeDto
}
