package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.BusinessUnitDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleBusinessUnitDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslationKeyMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreAttributesWithTranslations
import sk.esten.uss.gbco2.model.entity.business_unit.BusinessUnit
import sk.esten.uss.gbco2.model.entity.business_unit.BusinessUnitSuper
import sk.esten.uss.gbco2.model.entity.business_unit.VBusinessUnitTranslated
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface BusinessUnitMapper : TranslationMapper {

    @IgnoreAttributesWithTranslations
    @Mappings(
        Mapping(
            target = "nameK",
            constant = "${BusinessUnitSuper.translationPrefix}name",
            qualifiedBy = [TranslationKeyMapper::class]
        ),
        Mapping(target = "id", ignore = true)
    )
    fun map(dto: BusinessUnitDto): BusinessUnit

    @IgnoreAttributesWithTranslations
    @Mappings(Mapping(target = "id", ignore = true), Mapping(target = "nameK", ignore = true))
    fun update(dto: BusinessUnitDto, @MappingTarget entity: BusinessUnit)

    @Mapping(source = "nameTranslated", target = "name")
    @TranslatedLanguageMapper
    fun map(entity: VBusinessUnitTranslated): BusinessUnitDto

    @Mapping(source = "nameEn", target = "name")
    @EnglishLanguageMapper
    fun mapEn(entity: VBusinessUnitTranslated): BusinessUnitDto

    @Mapping(source = "nameTranslated", target = "name")
    @TranslatedLanguageMapper
    @SimpleMapper
    fun mapToSimple(entity: VBusinessUnitTranslated): SimpleBusinessUnitDto

    @Mapping(source = "nameEn", target = "name")
    @EnglishLanguageMapper
    @SimpleMapper
    fun mapToSimpleEn(entity: VBusinessUnitTranslated): SimpleBusinessUnitDto
}
