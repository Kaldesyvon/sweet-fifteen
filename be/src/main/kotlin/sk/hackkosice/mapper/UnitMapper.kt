package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.UnitDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleUnitDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslationKeyMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreNameTranslationsAndNameKeys
import sk.esten.uss.gbco2.model.entity.unit.GbcUnit
import sk.esten.uss.gbco2.model.entity.unit.GbcUnitSuper
import sk.esten.uss.gbco2.model.entity.unit.VGbcUnitTranslated
import sk.esten.uss.gbco2.service.UnitTypeService

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [UnitTypeMapper::class, UnitTypeService::class]
)
interface UnitMapper {

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(source = "unitTypeId", target = "unitType"),
        Mapping(
            target = "nameK",
            constant = "${GbcUnitSuper.translationPrefix}name",
            qualifiedBy = [TranslationKeyMapper::class]
        ),
        Mapping(target = "nameTranslations", ignore = true)
    )
    fun map(dto: UnitDto): GbcUnit

    @IgnoreBaseAttributes
    @IgnoreNameTranslationsAndNameKeys
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(source = "unitTypeId", target = "unitType"),
    )
    fun update(dto: UnitDto, @MappingTarget entity: GbcUnit)

    @Mappings(
        Mapping(source = "nameTranslated", target = "name"),
        Mapping(source = "unitType.id", target = "unitTypeId"),
    )
    @TranslatedLanguageMapper
    @SimpleMapper
    fun mapSimple(entity: VGbcUnitTranslated): SimpleUnitDto

    @Mappings(
        Mapping(source = "nameEn", target = "name"),
        Mapping(source = "unitType.id", target = "unitTypeId"),
    )
    @EnglishLanguageMapper
    @SimpleMapper
    fun mapSimpleEn(entity: VGbcUnitTranslated): SimpleUnitDto

    @Mappings(
        Mapping(source = "nameTranslated", target = "name"),
        Mapping(source = "unitType.id", target = "unitTypeId"),
        Mapping(
            source = "entity.unitType",
            target = "unitType",
            qualifiedBy = [TranslatedLanguageMapper::class]
        )
    )
    @TranslatedLanguageMapper
    fun map(entity: VGbcUnitTranslated): UnitDto

    @Mappings(
        Mapping(target = "name", source = "nameEn"),
        Mapping(source = "unitType.id", target = "unitTypeId"),
        Mapping(
            source = "entity.unitType",
            target = "unitType",
            qualifiedBy = [EnglishLanguageMapper::class]
        )
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VGbcUnitTranslated): UnitDto
}
