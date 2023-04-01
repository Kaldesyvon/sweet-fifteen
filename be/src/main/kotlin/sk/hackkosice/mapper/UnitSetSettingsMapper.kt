package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateUnitSetSettingsDto
import sk.esten.uss.gbco2.dto.response.UnitSetSettingsDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.unit_set_settings.UnitSetSettings
import sk.esten.uss.gbco2.model.entity.unit_set_settings.VUnitSetSettingsTranslated
import sk.esten.uss.gbco2.service.UnitService
import sk.esten.uss.gbco2.service.UnitSetService
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses =
        [
            UnitSetMapper::class,
            UnitMapper::class,
            MaterialMapper::class,
            UnitSetService::class,
            UnitService::class]
)
interface UnitSetSettingsMapper {

    @Mappings(
        Mapping(
            source = "material",
            target = "material",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "unitSet",
            target = "unitSet",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "unit",
            target = "unit",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
    )
    @TranslatedLanguageMapper
    fun map(entity: VUnitSetSettingsTranslated): UnitSetSettingsDto

    @Mappings(
        Mapping(
            source = "material",
            target = "material",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "unitSet",
            target = "unitSet",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "unit",
            target = "unit",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        )
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VUnitSetSettingsTranslated): UnitSetSettingsDto

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(source = "unitSetId", target = "unitSet"),
        Mapping(source = "unitId", target = "unit"),
        Mapping(target = "material", ignore = true)
    )
    @Named(value = "dto-to-entity")
    fun map(dto: CreateUnitSetSettingsDto): UnitSetSettings

    @IterableMapping(qualifiedByName = ["dto-to-entity"])
    fun map(dto: List<CreateUnitSetSettingsDto>): Set<UnitSetSettings>

    @IterableMapping(qualifiedBy = [TranslatedLanguageMapper::class])
    @TranslatedLanguageMapper
    fun map(entity: Set<VUnitSetSettingsTranslated>): List<UnitSetSettingsDto>

    @IterableMapping(qualifiedBy = [EnglishLanguageMapper::class])
    @EnglishLanguageMapper
    fun mapEn(entity: Set<VUnitSetSettingsTranslated>): List<UnitSetSettingsDto>

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(source = "unitSetId", target = "unitSet"),
        Mapping(source = "unitId", target = "unit"),
        Mapping(target = "material", ignore = true)
    )
    fun update(dto: CreateUnitSetSettingsDto, @MappingTarget entity: UnitSetSettings)
}
