package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateMaterialDto
import sk.esten.uss.gbco2.dto.request.update.UpdateMaterialDto
import sk.esten.uss.gbco2.dto.response.MaterialDto
import sk.esten.uss.gbco2.dto.response.detail.MaterialDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialDto
import sk.esten.uss.gbco2.dto.response.tree.MaterialTreeDto
import sk.esten.uss.gbco2.mapper.annotation.CommonMapper
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.IgnoreMaterialAttributes
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreTranslationsAndNameKeys
import sk.esten.uss.gbco2.model.entity.material.Material
import sk.esten.uss.gbco2.model.entity.material.MaterialSuper
import sk.esten.uss.gbco2.model.entity.material.VMaterialTranslated
import sk.esten.uss.gbco2.service.MaterialService
import sk.esten.uss.gbco2.service.UnitTypeService
import sk.esten.uss.gbco2.service.UsepaMaterialTypeService
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses =
        [
            UsepaMaterialTypeMapper::class,
            UnitTypeMapper::class,
            UnitSetSettingsMapper::class,
            UnitTypeService::class,
            UsepaMaterialTypeService::class,
            MaterialService::class]
)
interface MaterialMapper : TranslationMapper {

    @IgnoreBaseAttributes
    @IgnoreMaterialAttributes
    @Mappings(
        Mapping(
            target = "nameK",
            constant = "${MaterialSuper.translationPrefix}name",
            qualifiedByName = ["material-name-key"]
        ),
        Mapping(source = "dto.unitTypeId", target = "unitType"),
        Mapping(source = "dto.parentMaterialId", target = "parentMaterial"),
        Mapping(source = "dto.usepaMaterialTypeId", target = "usepaMaterialType"),
        Mapping(source = "dto.unitSetSettings", target = "unitSetSettings"),
        Mapping(target = "translations", ignore = true),
    )
    fun map(dto: CreateMaterialDto): Material

    @IgnoreBaseAttributes
    @IgnoreTranslationsAndNameKeys
    @IgnoreMaterialAttributes
    @Mappings(
        Mapping(source = "dto.parentMaterialId", target = "parentMaterial"),
        Mapping(source = "dto.usepaMaterialTypeId", target = "usepaMaterialType"),
        Mapping(target = "unitSetSettings", ignore = true),
        Mapping(target = "unitType", ignore = true)
    )
    fun update(dto: UpdateMaterialDto, @MappingTarget entity: Material)

    @SimpleMapper
    @TranslatedLanguageMapper
    @Mappings(
        Mapping(source = "nameTranslated", target = "name"),
        Mapping(source = "entity.unitType.id", target = "unitTypeId"),
        Mapping(source = "entity.parentMaterial.id", target = "materialGroupId")
    )
    fun mapToSimple(entity: VMaterialTranslated): SimpleMaterialDto

    @SimpleMapper
    @EnglishLanguageMapper
    @Mappings(
        Mapping(source = "nameEn", target = "name"),
        Mapping(source = "entity.unitType.id", target = "unitTypeId"),
        Mapping(source = "entity.parentMaterial.id", target = "materialGroupId")
    )
    fun mapToSimpleEn(entity: VMaterialTranslated): SimpleMaterialDto

    @Mappings(
        Mapping(
            source = "entity.parentMaterial",
            target = "parentMaterial",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.unitType",
            target = "unitType",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "entity.usepaMaterialType",
            target = "usepaMaterialType",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(source = "nameTranslated", target = "name"),
        Mapping(target = "unitTypeId", ignore = true),
        Mapping(target = "materialGroupId", ignore = true)
    )
    @CommonMapper
    @IgnoreBaseAttributes
    @TranslatedLanguageMapper
    fun map(entity: VMaterialTranslated): MaterialDto

    @Mappings(
        Mapping(
            source = "entity.parentMaterial",
            target = "parentMaterial",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.unitType",
            target = "unitType",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "entity.usepaMaterialType",
            target = "usepaMaterialType",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(source = "nameEn", target = "name"),
        Mapping(target = "unitTypeId", ignore = true),
        Mapping(target = "materialGroupId", ignore = true)
    )
    @CommonMapper
    @IgnoreBaseAttributes
    @EnglishLanguageMapper
    fun mapEn(entity: VMaterialTranslated): MaterialDto

    @Mappings(
        Mapping(
            source = "entity.parentMaterial",
            target = "parentMaterial",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.unitType",
            target = "unitType",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "entity.usepaMaterialType",
            target = "usepaMaterialType",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "nameTranslated",
            target = "name",
        ),
        Mapping(
            source = "unitSetSettings",
            target = "unitSetSettings",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(target = "unitTypeId", ignore = true),
        Mapping(target = "materialGroupId", ignore = true)
    )
    @TranslatedLanguageMapper
    fun mapToDetail(entity: VMaterialTranslated): MaterialDetailDto

    @Mappings(
        Mapping(
            source = "entity.parentMaterial",
            target = "parentMaterial",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.unitType",
            target = "unitType",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "entity.usepaMaterialType",
            target = "usepaMaterialType",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "nameEn",
            target = "name",
        ),
        Mapping(
            source = "unitSetSettings",
            target = "unitSetSettings",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(target = "unitTypeId", ignore = true),
        Mapping(target = "materialGroupId", ignore = true)
    )
    @EnglishLanguageMapper
    fun mapToDetailEn(entity: VMaterialTranslated): MaterialDetailDto

    @Mappings(
        Mapping(
            source = "entity.parentMaterial",
            target = "parentMaterial",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.unitType",
            target = "unitType",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "entity.subMaterial",
            target = "children",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "entity.usepaMaterialType",
            target = "usepaMaterialType",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "nameTranslated",
            target = "name",
        ),
        Mapping(target = "unitTypeId", ignore = true),
        Mapping(target = "materialGroupId", ignore = true)
    )
    @TranslatedLanguageMapper
    fun mapToTree(entity: VMaterialTranslated): MaterialTreeDto

    @Mappings(
        Mapping(
            source = "entity.parentMaterial",
            target = "parentMaterial",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.unitType",
            target = "unitType",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "entity.subMaterial",
            target = "children",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "entity.usepaMaterialType",
            target = "usepaMaterialType",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "nameEn",
            target = "name",
        ),
        Mapping(target = "unitTypeId", ignore = true),
        Mapping(target = "materialGroupId", ignore = true)
    )
    @EnglishLanguageMapper
    fun mapToTreeEn(entity: VMaterialTranslated): MaterialTreeDto

    @IterableMapping(qualifiedBy = [TranslatedLanguageMapper::class])
    @TranslatedLanguageMapper
    fun mapToTree(entity: Set<VMaterialTranslated>): List<MaterialTreeDto>

    @IterableMapping(qualifiedBy = [EnglishLanguageMapper::class])
    @EnglishLanguageMapper
    fun mapToTreeEn(entity: Set<VMaterialTranslated>): List<MaterialTreeDto>

    @Named("material-name-key")
    fun generateKeyCustom(prefix: String): String {
        return this.generateKey(prefix)
    }
}
