package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateMaterialNodeDto
import sk.esten.uss.gbco2.dto.request.update.UpdateMaterialNodeDto
import sk.esten.uss.gbco2.dto.response.MaterialNodeDto
import sk.esten.uss.gbco2.dto.response.detail.MaterialNodeDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialNodeDto
import sk.esten.uss.gbco2.mapper.annotation.*
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.material_node.MaterialNode
import sk.esten.uss.gbco2.model.entity.material_node.VMaterialNodeTranslated
import sk.esten.uss.gbco2.model.entity.quantity.IOEnum
import sk.esten.uss.gbco2.service.MaterialService
import sk.esten.uss.gbco2.service.NodeService
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses =
        [
            MaterialMapper::class,
            NodeMapper::class,
            MaterialNodeAnalysisParamMapper::class,
            MaterialTypeMapper::class,
            MaterialService::class,
            NodeService::class]
)
interface MaterialNodeMapper {
    @Mappings(
        Mapping(
            source = "material",
            target = "material",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "node",
            target = "node",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        )
    )
    @CommonMapper
    @TranslatedLanguageMapper
    fun map(view: VMaterialNodeTranslated): MaterialNodeDto

    @Mappings(
        Mapping(
            source = "material",
            target = "material",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "node",
            target = "node",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        )
    )
    @CommonMapper
    @EnglishLanguageMapper
    fun mapEn(view: VMaterialNodeTranslated): MaterialNodeDto

    @Mappings(
        Mapping(
            source = "materialNodeAnalysisParams",
            target = "materialNodeAnalysisParams",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "materialNodeTypes",
            target = "materialNodeTypes",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "material",
            target = "material",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "node",
            target = "node",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        )
    )
    @TranslationKeyMapper
    fun mapToDetail(view: VMaterialNodeTranslated): MaterialNodeDetailDto

    @Mappings(
        Mapping(
            source = "materialNodeAnalysisParams",
            target = "materialNodeAnalysisParams",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "materialNodeTypes",
            target = "materialNodeTypes",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "material",
            target = "material",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "node",
            target = "node",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        )
    )
    @EnglishLanguageMapper
    fun mapToDetailEn(view: VMaterialNodeTranslated): MaterialNodeDetailDto

    @SimpleMapper fun mapToSimple(view: VMaterialNodeTranslated): SimpleMaterialNodeDto

    @IterableMapping(qualifiedBy = [TranslatedLanguageMapper::class, CommonMapper::class])
    @CommonMapper
    @TranslatedLanguageMapper
    fun map(views: Set<VMaterialNodeTranslated>): List<MaterialNodeDto>

    @IterableMapping(qualifiedBy = [EnglishLanguageMapper::class, CommonMapper::class])
    @CommonMapper
    @EnglishLanguageMapper
    fun mapEn(views: Set<VMaterialNodeTranslated>): List<MaterialNodeDto>

    @IterableMapping(qualifiedBy = [SimpleMapper::class])
    @SimpleMapper
    fun mapToSimple(views: Set<VMaterialNodeTranslated>): List<SimpleMaterialNodeDto>

    @Named("input") fun setInput(io: IOEnum): Boolean = io == IOEnum.INPUT

    @Named("output") fun setOutput(io: IOEnum): Boolean = io == IOEnum.OUTPUT

    @Mappings(
        Mapping(source = "nodeId", target = "node"),
        Mapping(
            source = "materialId",
            target = "material",
        ),
        Mapping(source = "io", target = "input", qualifiedByName = ["input"]),
        Mapping(source = "io", target = "output", qualifiedByName = ["output"]),
        Mapping(target = "scopeMaterialNode", ignore = true),
        Mapping(target = "id", ignore = true),
        Mapping(target = "materialNodeTypes", ignore = true),
        Mapping(target = "materialNodeAnalysisParams", ignore = true),
    )
    @IgnoreBaseAttributes
    fun map(createDto: CreateMaterialNodeDto): MaterialNode

    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(source = "io", target = "input", qualifiedByName = ["input"]),
        Mapping(source = "io", target = "output", qualifiedByName = ["output"]),
        Mapping(target = "node", ignore = true),
        Mapping(target = "material", ignore = true),
        Mapping(target = "product", ignore = true),
        Mapping(target = "scopeMaterialNode", ignore = true),
        Mapping(target = "materialNodeAnalysisParams", ignore = true),
        Mapping(target = "materialNodeTypes", ignore = true)
    )
    @IgnoreBaseAttributes
    fun update(updateDto: UpdateMaterialNodeDto, @MappingTarget entity: MaterialNode)
}
