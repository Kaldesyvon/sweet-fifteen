package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateLandfillDto
import sk.esten.uss.gbco2.dto.response.LandfillDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleLandfillDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.landfill.Landfill
import sk.esten.uss.gbco2.model.entity.landfill.VLandfillTranslated
import sk.esten.uss.gbco2.model.entity.node.VNodeTranslated
import sk.esten.uss.gbco2.service.UnitService
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [UnitMapper::class, NodeMapper::class, UnitService::class]
)
interface LandfillMapper {

    @Mappings(
        Mapping(
            source = "unit",
            target = "unit",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "nodes",
            target = "node",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        )
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VLandfillTranslated): LandfillDto

    @Mappings(
        Mapping(
            source = "unit",
            target = "unit",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "nodes",
            target = "node",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        )
    )
    @TranslatedLanguageMapper
    fun map(entity: VLandfillTranslated): LandfillDto

    @SimpleMapper fun mapToSimple(entity: VLandfillTranslated): SimpleLandfillDto

    /** Get first associated node */
    @EnglishLanguageMapper
    @TranslatedLanguageMapper
    @SimpleMapper
    fun getFirstNode(nodes: List<VNodeTranslated>?): VNodeTranslated? {
        return nodes?.firstOrNull()
    }

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(source = "unitId", target = "unit"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "nodes", ignore = true)
    )
    fun map(createDto: CreateLandfillDto): Landfill

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(source = "unitId", target = "unit"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "nodes", ignore = true)
    )
    fun update(updateDto: CreateLandfillDto, @MappingTarget entity: Landfill)
}
