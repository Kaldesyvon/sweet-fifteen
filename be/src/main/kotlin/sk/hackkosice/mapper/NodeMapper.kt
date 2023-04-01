package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateNodeDto
import sk.esten.uss.gbco2.dto.request.update.UpdateNodeDto
import sk.esten.uss.gbco2.dto.response.NodeDto
import sk.esten.uss.gbco2.dto.response.detail.NodeDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeTreeDto
import sk.esten.uss.gbco2.dto.response.tree.NodeTreeDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.IgnoreNodeAttributes
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.node.Node
import sk.esten.uss.gbco2.model.entity.node.NodeSuper
import sk.esten.uss.gbco2.model.entity.node.VNodeTranslated
import sk.esten.uss.gbco2.model.entity.node_type.VNodeTypeTranslated
import sk.esten.uss.gbco2.model.entity.node_types.VNodeTypesTranslated
import sk.esten.uss.gbco2.service.*
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses =
        [
            FuelTypeMapper::class,
            NodeLevelMapper::class,
            BusinessUnitMapper::class,
            CountryMapper::class,
            LandfillMapper::class,
            NaicsCodeMapper::class,
            NodeTypeMapper::class,
            StatePostCodeMapper::class,
            RegionMapper::class,
            NodeLevelService::class,
            NodeTypeService::class,
            LandfillService::class,
            CountryService::class,
            BusinessUnitService::class,
            NaicsCodeService::class,
            NodeService::class,
            StatePostCodeService::class]
)
interface NodeMapper : TranslationMapper {

    @IgnoreBaseAttributes
    @IgnoreNodeAttributes
    @Mappings(
        Mapping(
            target = "nameK",
            constant = "${NodeSuper.translationPrefix}name",
            qualifiedByName = ["node-name-key"]
        ),
        Mapping(source = "parentNodeId", target = "parentNode"),
        Mapping(source = "nodeLevelId", target = "nodeLevel"),
        Mapping(source = "businessUnitId", target = "businessUnit"),
        Mapping(source = "landfillId", target = "landfill"),
        Mapping(source = "countryId", target = "country"),
        Mapping(source = "prNaicsCodeId", target = "prNaicsCode"),
        Mapping(source = "seNaicsCodeId", target = "seNaicsCode"),
        Mapping(source = "nodeTypeId", target = "nodeType"),
        Mapping(source = "stateProvincePostCodeId", target = "stateProvincePostCode")
    )
    fun map(dto: CreateNodeDto): Node

    @IgnoreBaseAttributes
    @IgnoreNodeAttributes
    @Mappings(
        Mapping(source = "parentNodeId", target = "parentNode"),
        Mapping(source = "businessUnitId", target = "businessUnit"),
        Mapping(source = "landfillId", target = "landfill"),
        Mapping(source = "countryId", target = "country"),
        Mapping(source = "prNaicsCodeId", target = "prNaicsCode"),
        Mapping(source = "seNaicsCodeId", target = "seNaicsCode"),
        Mapping(source = "nodeTypeId", target = "nodeType"),
        Mapping(source = "stateProvincePostCodeId", target = "stateProvincePostCode"),
        Mapping(target = "nameK", ignore = true),
        Mapping(target = "nodeLevel", ignore = true)
    )
    fun update(dto: UpdateNodeDto, @MappingTarget entity: Node)

    @Mappings(
        Mapping(source = "entity.nameTranslated", target = "name"),
        Mapping(
            source = "entity.nodeLevel",
            target = "nodeLevel",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(source = "country.region.id", target = "regionId"),
    )
    @SimpleMapper
    @TranslatedLanguageMapper
    fun mapSimple(entity: VNodeTranslated): SimpleNodeDto

    @Mappings(
        Mapping(source = "entity.nameEn", target = "name"),
        Mapping(
            source = "entity.nodeLevel",
            target = "nodeLevel",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(source = "country.region.id", target = "regionId"),
    )
    @SimpleMapper
    @EnglishLanguageMapper
    fun mapSimpleEn(entity: VNodeTranslated): SimpleNodeDto

    @Mappings(
        Mapping(source = "entity.nameTranslated", target = "name"),
        Mapping(
            source = "entity.nodeTypesSet",
            target = "nodeType",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.fuelType",
            target = "fuelType",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.parentNode",
            target = "parentNode",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.businessUnit",
            target = "businessUnit",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.landfill",
            target = "landfill",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "entity.nodeLevel",
            target = "nodeLevel",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.country",
            target = "country",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "entity.prNaicsCode",
            target = "prNaicsCode",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "entity.seNaicsCode",
            target = "seNaicsCode",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "entity.stateProvincePostCode",
            target = "stateProvincePostCode",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(target = "regionId", ignore = true)
    )
    @TranslatedLanguageMapper
    fun mapDetail(entity: VNodeTranslated): NodeDetailDto

    @Mappings(
        Mapping(source = "entity.nameEn", target = "name"),
        Mapping(
            source = "entity.nodeTypesSet",
            target = "nodeType",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.fuelType",
            target = "fuelType",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.parentNode",
            target = "parentNode",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.businessUnit",
            target = "businessUnit",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.landfill",
            target = "landfill",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "entity.nodeLevel",
            target = "nodeLevel",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.country",
            target = "country",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "entity.prNaicsCode",
            target = "prNaicsCode",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "entity.seNaicsCode",
            target = "seNaicsCode",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "entity.stateProvincePostCode",
            target = "stateProvincePostCode",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(target = "regionId", ignore = true)
    )
    @EnglishLanguageMapper
    fun mapDetailEn(entity: VNodeTranslated): NodeDetailDto

    @Mappings(
        Mapping(source = "entity.nameTranslated", target = "name"),
        Mapping(
            source = "entity.parentNode",
            target = "parentNode",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.country",
            target = "country",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "entity.nodeLevel",
            target = "nodeLevel",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(target = "regionId", ignore = true)
    )
    @TranslatedLanguageMapper
    fun map(entity: VNodeTranslated): NodeDto

    @Mappings(
        Mapping(source = "entity.nameTranslated", target = "name"),
        Mapping(
            source = "entity.parentNode",
            target = "parentNode",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.country",
            target = "country",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "entity.nodeLevel",
            target = "nodeLevel",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(target = "regionId", ignore = true)
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VNodeTranslated): NodeDto

    @Mappings(
        Mapping(source = "entity.nameTranslated", target = "name"),
        Mapping(
            source = "entity.parentNode",
            target = "parentNode",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.country",
            target = "country",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "entity.subNode",
            target = "children",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "entity.nodeLevel",
            target = "nodeLevel",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(target = "regionId", ignore = true)
    )
    @TranslatedLanguageMapper
    fun mapTree(entity: VNodeTranslated): NodeTreeDto

    @Mappings(
        Mapping(source = "entity.nameTranslated", target = "name"),
        Mapping(
            source = "entity.parentNode",
            target = "parentNode",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.subNode",
            target = "children",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "entity.nodeLevel",
            target = "nodeLevel",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.country.region",
            target = "region",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(target = "regionId", ignore = true)
    )
    @TranslatedLanguageMapper
    fun mapSimpleTree(entity: VNodeTranslated): SimpleNodeTreeDto

    @IterableMapping(qualifiedBy = [TranslatedLanguageMapper::class])
    @TranslatedLanguageMapper
    fun mapTree(entity: MutableSet<VNodeTranslated>): List<NodeTreeDto>

    @TranslatedLanguageMapper
    @EnglishLanguageMapper
    @SimpleMapper
    fun firstNodeType(nodeTypesSet: MutableSet<VNodeTypesTranslated>?): VNodeTypeTranslated? {
        return nodeTypesSet?.firstOrNull()?.nodeType
    }

    @IterableMapping(qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class])
    @TranslatedLanguageMapper
    @SimpleMapper
    fun map(nodes: Set<VNodeTranslated>): Set<SimpleNodeDto>

    @Named("node-name-key")
    fun generateKeyCustom(prefix: String): String {
        return this.generateKey(prefix)
    }
}
