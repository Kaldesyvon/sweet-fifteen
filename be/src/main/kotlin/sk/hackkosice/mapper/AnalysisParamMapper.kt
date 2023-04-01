package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateAnalysisParamDto
import sk.esten.uss.gbco2.dto.request.update.UpdateAnalysisParamDto
import sk.esten.uss.gbco2.dto.response.AnalysisParamDto
import sk.esten.uss.gbco2.dto.response.detail.AnalysisParamDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisParamDto
import sk.esten.uss.gbco2.mapper.annotation.*
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.IgnoreAnalysisParamAttributes
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreNameTranslationsAndNameKeys
import sk.esten.uss.gbco2.model.entity.analysis_param.AnalysisParam
import sk.esten.uss.gbco2.model.entity.analysis_param.AnalysisParamSuper
import sk.esten.uss.gbco2.model.entity.analysis_param.VAnalysisParamTranslated
import sk.esten.uss.gbco2.service.AnalysisParamTypeService
import sk.esten.uss.gbco2.service.UnitAnalysisFormatService
import sk.esten.uss.gbco2.service.UnitTypeService
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses =
        [
            UnitAnalysisFormatMapper::class,
            AnalysisParamTypeMapper::class,
            UnitTypeMapper::class,
            UnitSetSettingsApMapper::class,
            AnalysisParamExprMapper::class,
            AnalysisParamTypeService::class,
            UnitAnalysisFormatService::class,
            UnitTypeService::class]
)
interface AnalysisParamMapper : TranslationMapper {

    @Mappings(
        Mapping(
            source = "nameTranslated",
            target = "name",
        ),
        Mapping(
            source = "unitType",
            target = "unitType",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "analysisParamType",
            target = "analysisParamType",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "unitAnalysisFormat",
            target = "unitAnalysisFormat",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            target = "parent",
            source = "parent",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.unitType.id",
            target = "unitTypeId",
        )
    )
    @CommonMapper
    @TranslatedLanguageMapper
    fun map(entity: VAnalysisParamTranslated): AnalysisParamDto

    @IgnoreBaseAttributes
    @IgnoreAnalysisParamAttributes
    @Mappings(
        Mapping(
            target = "nameK",
            constant = "${AnalysisParamSuper.translationPrefix}name",
            qualifiedByName = ["name-key"]
        ),
        Mapping(source = "analysisParamTypeId", target = "analysisParamType"),
        Mapping(source = "unitAnalysisFormatId", target = "unitAnalysisFormat"),
        Mapping(source = "unitTypeId", target = "unitType"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "nameTranslations", ignore = true)
    )
    fun map(createDto: CreateAnalysisParamDto): AnalysisParam

    @IgnoreBaseAttributes
    @IgnoreAnalysisParamAttributes
    @IgnoreNameTranslationsAndNameKeys
    @Mappings(
        Mapping(source = "analysisParamTypeId", target = "analysisParamType"),
        Mapping(source = "unitAnalysisFormatId", target = "unitAnalysisFormat"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "unitType", ignore = true)
    )
    fun update(dto: UpdateAnalysisParamDto, @MappingTarget entity: AnalysisParam)

    @Mappings(
        Mapping(
            source = "nameEn",
            target = "name",
        ),
        Mapping(
            source = "unitType",
            target = "unitType",
            qualifiedBy = [SimpleMapper::class, EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "analysisParamType",
            target = "analysisParamType",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "unitAnalysisFormat",
            target = "unitAnalysisFormat",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "parent",
            target = "parent",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.unitType.id",
            target = "unitTypeId",
        )
    )
    @CommonMapper
    @EnglishLanguageMapper
    fun mapEn(entity: VAnalysisParamTranslated): AnalysisParamDto

    @Mappings(
        Mapping(
            source = "nameTranslated",
            target = "name",
        ),
        Mapping(
            source = "unitType",
            target = "unitType",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "unitSetSettingsAps",
            target = "unitSetSettingsAps",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "analysisParamExpressions",
            target = "analysisParamExpressions",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "analysisParamType",
            target = "analysisParamType",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "unitAnalysisFormat",
            target = "unitAnalysisFormat",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "parent",
            target = "parent",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.unitType.id",
            target = "unitTypeId",
        )
    )
    @TranslatedLanguageMapper
    fun mapToDetail(entity: VAnalysisParamTranslated): AnalysisParamDetailDto

    @Mappings(
        Mapping(
            source = "nameEn",
            target = "name",
        ),
        Mapping(
            source = "unitType",
            target = "unitType",
            qualifiedBy = [SimpleMapper::class, EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "unitSetSettingsAps",
            target = "unitSetSettingsAps",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "analysisParamExpressions",
            target = "analysisParamExpressions",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "analysisParamType",
            target = "analysisParamType",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "unitAnalysisFormat",
            target = "unitAnalysisFormat",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "parent",
            target = "parent",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "entity.unitType.id",
            target = "unitTypeId",
        )
    )
    @EnglishLanguageMapper
    fun mapToDetailEn(entity: VAnalysisParamTranslated): AnalysisParamDetailDto

    @Mappings(
        Mapping(
            source = "nameTranslated",
            target = "name",
        ),
        Mapping(
            source = "entity.unitType.id",
            target = "unitTypeId",
        )
    )
    @SimpleMapper
    @TranslatedLanguageMapper
    fun mapSimple(entity: VAnalysisParamTranslated): SimpleAnalysisParamDto

    @Mappings(
        Mapping(
            source = "nameEn",
            target = "name",
        ),
        Mapping(
            source = "entity.unitType.id",
            target = "unitTypeId",
        )
    )
    @SimpleMapper
    @EnglishLanguageMapper
    fun mapSimpleEn(entity: VAnalysisParamTranslated): SimpleAnalysisParamDto

    @Named("name-key")
    fun generateKeyCustom(prefix: String): String {
        return this.generateKey(prefix)
    }
}
