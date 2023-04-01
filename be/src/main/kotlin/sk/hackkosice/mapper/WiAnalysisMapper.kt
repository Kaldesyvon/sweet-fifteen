package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.detail.WiAnalysisDetailDto
import sk.esten.uss.gbco2.dto.response.wi.analysys.WiAnalysisDto
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.wi.analysis.VWiAnalysisTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses =
        [
            ScopeMapper::class,
            WiNodeMapper::class,
            NodeMapper::class,
            AnalysisParamMapper::class,
            MaterialMapper::class]
)
interface WiAnalysisMapper {

    @Mappings(
        Mapping(
            target = "scope",
            source = "scope",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
        Mapping(
            target = "analysisParam",
            source = "analysisParam",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
        Mapping(
            target = "materialProduced",
            source = "materialProduced",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
        Mapping(
            target = "nodes",
            source = "nodes",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
    )
    @TranslatedLanguageMapper
    fun map(entity: VWiAnalysisTranslated): WiAnalysisDto

    @Mappings(
        Mapping(
            target = "scope",
            source = "scope",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
        Mapping(
            target = "analysisParam",
            source = "analysisParam",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
        Mapping(
            target = "materialProduced",
            source = "materialProduced",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
        Mapping(
            target = "nodes",
            source = "nodes",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
    )
    @TranslatedLanguageMapper
    fun mapDetail(entity: VWiAnalysisTranslated): WiAnalysisDetailDto
}
