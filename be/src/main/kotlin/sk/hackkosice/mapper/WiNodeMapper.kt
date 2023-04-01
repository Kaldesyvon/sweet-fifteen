package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.node.VNodeTranslated
import sk.esten.uss.gbco2.model.entity.wi.node.VWiAnalysisNodeTranslated

@Mapper(
    componentModel = "springlazy",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface WiNodeMapper {

    @IterableMapping(qualifiedBy = [TranslatedLanguageMapper::class])
    @TranslatedLanguageMapper
    @SimpleMapper
    fun map(nodes: Set<VWiAnalysisNodeTranslated>): Set<VNodeTranslated>

    @TranslatedLanguageMapper
    @EnglishLanguageMapper
    @SimpleMapper
    fun analysisNodeToNode(entity: VWiAnalysisNodeTranslated): VNodeTranslated? {
        return entity.node
    }
}
