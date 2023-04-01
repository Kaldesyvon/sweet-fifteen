package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateNodeTypeDto
import sk.esten.uss.gbco2.dto.response.NodeTypeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeTypeDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.node_type.NodeType
import sk.esten.uss.gbco2.model.entity.node_type.NodeTypeSuper
import sk.esten.uss.gbco2.model.entity.node_type.VNodeTypeTranslated
import sk.esten.uss.gbco2.service.MaterialService
import sk.esten.uss.gbco2.service.NodeLevelService

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses =
        [
            NodeLevelService::class,
            MaterialService::class,
            NodeLevelMapper::class,
            MaterialMapper::class]
)
interface NodeTypeMapper : TranslationMapper {

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(
            target = "nameK",
            constant = "${NodeTypeSuper.translationPrefix}name",
            qualifiedByName = ["nodeType-name-key"]
        ),
        Mapping(target = "id", ignore = true),
        Mapping(source = "nodeLevelId", target = "nodeLevel"),
        Mapping(source = "materialReportId", target = "materialReport"),
        Mapping(target = "translations", ignore = true)
    )
    fun map(dto: CreateNodeTypeDto): NodeType

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(source = "nodeLevelId", target = "nodeLevel"),
        Mapping(source = "materialReportId", target = "materialReport"),
        Mapping(target = "translations", ignore = true),
        Mapping(target = "nameK", ignore = true)
    )
    fun update(dto: CreateNodeTypeDto, @MappingTarget entity: NodeType)

    @Mappings(
        Mapping(source = "nameTranslated", target = "name"),
        Mapping(
            source = "entity.nodeLevel",
            target = "nodeLevel",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.materialReport",
            target = "materialReport",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        )
    )
    @TranslatedLanguageMapper
    fun map(entity: VNodeTypeTranslated): NodeTypeDto

    @Mappings(
        Mapping(source = "nameEn", target = "name"),
        Mapping(
            source = "entity.nodeLevel",
            target = "nodeLevel",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.materialReport",
            target = "materialReport",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        )
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VNodeTypeTranslated): NodeTypeDto

    @Mappings(
        Mapping(source = "nameTranslated", target = "name"),
        Mapping(
            source = "entity.nodeLevel",
            target = "nodeLevel",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        )
    )
    @TranslatedLanguageMapper
    @SimpleMapper
    fun mapSimple(entity: VNodeTypeTranslated): SimpleNodeTypeDto

    @Mappings(
        Mapping(source = "nameEn", target = "name"),
        Mapping(
            source = "entity.nodeLevel",
            target = "nodeLevel",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        )
    )
    @EnglishLanguageMapper
    @SimpleMapper
    fun mapSimpleEn(entity: VNodeTypeTranslated): SimpleNodeTypeDto

    @Named("nodeType-name-key")
    fun generateKeyCustom(prefix: String): String {
        return this.generateKey(prefix)
    }
}
