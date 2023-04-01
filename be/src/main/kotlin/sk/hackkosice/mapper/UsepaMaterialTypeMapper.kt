package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateUsepaMaterialTypeDto
import sk.esten.uss.gbco2.dto.response.UsepaMaterialTypeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleUsepaMaterialTypeDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslationKeyMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.usepa_material_type.UsepaMaterialType
import sk.esten.uss.gbco2.model.entity.usepa_material_type.UsepaMaterialTypeSuper
import sk.esten.uss.gbco2.model.entity.usepa_material_type.VUsepaMaterialTypeTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface UsepaMaterialTypeMapper : TranslationMapper {

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(
            target = "nameK",
            constant = "${UsepaMaterialTypeSuper.translationPrefix}name",
            qualifiedBy = [TranslationKeyMapper::class]
        ),
        Mapping(target = "id", ignore = true),
        Mapping(target = "nameTranslations", ignore = true),
        Mapping(target = "objVersion", ignore = true)
    )
    fun map(dto: CreateUsepaMaterialTypeDto): UsepaMaterialType

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(target = "nameK", ignore = true),
        Mapping(target = "nameTranslations", ignore = true)
    )
    fun update(dto: CreateUsepaMaterialTypeDto, @MappingTarget entity: UsepaMaterialType)

    @Mapping(source = "nameTranslated", target = "name")
    @TranslatedLanguageMapper
    fun map(entity: VUsepaMaterialTypeTranslated): UsepaMaterialTypeDto

    @Mapping(source = "nameEn", target = "name")
    @EnglishLanguageMapper
    fun mapEn(entity: VUsepaMaterialTypeTranslated): UsepaMaterialTypeDto

    @Mapping(source = "nameTranslated", target = "name")
    @TranslatedLanguageMapper
    @SimpleMapper
    fun mapSimple(entity: VUsepaMaterialTypeTranslated): SimpleUsepaMaterialTypeDto

    @Mapping(source = "nameEn", target = "name")
    @EnglishLanguageMapper
    @SimpleMapper
    fun mapSimpleEn(entity: VUsepaMaterialTypeTranslated): SimpleUsepaMaterialTypeDto
}
