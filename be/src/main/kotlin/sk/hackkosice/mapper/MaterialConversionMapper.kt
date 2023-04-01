package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import sk.esten.uss.gbco2.dto.request.create.CreateMaterialConversionDto
import sk.esten.uss.gbco2.dto.response.detail.MaterialConversionDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialConversionDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.material_conversion.MaterialConversion
import sk.esten.uss.gbco2.model.entity.material_conversion.VMaterialConversionTranslated
import sk.esten.uss.gbco2.service.MaterialService
import sk.esten.uss.gbco2.service.NodeService
import sk.esten.uss.gbco2.service.UnitService

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses =
        [
            MaterialMapper::class,
            UnitTypeMapper::class,
            UnitMapper::class,
            UnitService::class,
            NodeMapper::class,
            NodeService::class,
            MaterialMapper::class,
            MaterialService::class]
)
interface MaterialConversionMapper {
    @SimpleMapper
    @TranslatedLanguageMapper
    fun mapSimple(entity: VMaterialConversionTranslated): SimpleMaterialConversionDto

    @EnglishLanguageMapper
    @SimpleMapper
    fun mapSimpleEn(entity: VMaterialConversionTranslated): SimpleMaterialConversionDto

    @Mappings(
        Mapping(target = "material.unitTypeId", ignore = true),
        Mapping(target = "material.materialGroupId", ignore = true),
        Mapping(source = "autoAnalysis", target = "autoAnalysis"),
        Mapping(
            source = "node",
            target = "node",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "unit",
            target = "unit",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        )
    )
    @TranslatedLanguageMapper
    fun map(entity: VMaterialConversionTranslated): MaterialConversionDetailDto

    @Mappings(
        Mapping(target = "material.unitTypeId", ignore = true),
        Mapping(target = "material.materialGroupId", ignore = true),
        Mapping(source = "autoAnalysis", target = "autoAnalysis"),
        Mapping(
            source = "node",
            target = "node",
            qualifiedBy = [SimpleMapper::class, EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "unit",
            target = "unit",
            qualifiedBy = [SimpleMapper::class, EnglishLanguageMapper::class]
        )
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VMaterialConversionTranslated): MaterialConversionDetailDto

    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(source = "nodeId", target = "node"),
        Mapping(source = "materialId", target = "material"),
        Mapping(source = "unitId", target = "unit"),
    )
    @IgnoreBaseAttributes
    fun map(dto: CreateMaterialConversionDto): MaterialConversion

    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(source = "nodeId", target = "node"),
        Mapping(source = "materialId", target = "material"),
        Mapping(source = "unitId", target = "unit"),
    )
    @IgnoreBaseAttributes
    fun update(
        dto: CreateMaterialConversionDto,
        @MappingTarget entity: MaterialConversion
    ): MaterialConversion
}
