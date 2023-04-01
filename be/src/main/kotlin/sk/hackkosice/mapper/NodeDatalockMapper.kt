package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.NodeDatalockDto
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.node_datalock.VNodeDatalockTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [NodeMapper::class]
)
interface NodeDatalockMapper {

    @Mapping(
        source = "node",
        target = "node",
        qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
    )
    fun map(entity: VNodeDatalockTranslated): NodeDatalockDto
}
