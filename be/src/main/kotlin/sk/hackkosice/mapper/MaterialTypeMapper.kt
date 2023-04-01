package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.MaterialTypeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialTypeDto
import sk.esten.uss.gbco2.mapper.annotation.*
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreTranslationsAndNameKeys
import sk.esten.uss.gbco2.model.entity.material_type.MaterialType
import sk.esten.uss.gbco2.model.entity.material_type.MaterialTypeProjection
import sk.esten.uss.gbco2.model.entity.material_type.MaterialTypeSuper
import sk.esten.uss.gbco2.model.entity.material_type.VMaterialTypeTranslated
import sk.esten.uss.gbco2.service.MaterialTypeService

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [MaterialTypeService::class]
)
interface MaterialTypeMapper : TranslationMapper {

    @IgnoreBaseAttributes
    @CommonMapper
    @Mappings(
        Mapping(
            target = "nameK",
            constant = "${MaterialTypeSuper.translationPrefix}name",
            qualifiedBy = [TranslationKeyMapper::class]
        ),
        Mapping(target = "id", ignore = true),
        Mapping(target = "translations", ignore = true)
    )
    fun map(dto: MaterialTypeDto): MaterialType

    @IgnoreBaseAttributes
    @IgnoreTranslationsAndNameKeys
    @Mappings(Mapping(target = "id", ignore = true))
    fun update(dto: MaterialTypeDto, @MappingTarget entity: MaterialType)

    @Mappings(Mapping(source = "nameTranslated", target = "name"))
    @TranslatedLanguageMapper
    fun map(entity: VMaterialTypeTranslated): MaterialTypeDto

    @Mappings(Mapping(source = "nameEn", target = "name"))
    @EnglishLanguageMapper
    fun mapEn(entity: VMaterialTypeTranslated): MaterialTypeDto

    @Mappings(Mapping(source = "nameTranslated", target = "name"))
    @TranslatedLanguageMapper
    @SimpleMapper
    fun mapSimple(entity: VMaterialTypeTranslated): SimpleMaterialTypeDto

    @Mappings(Mapping(source = "nameEn", target = "name"))
    @EnglishLanguageMapper
    @SimpleMapper
    fun mapSimpleEn(entity: VMaterialTypeTranslated): SimpleMaterialTypeDto

    @CommonMapper
    @IterableMapping(qualifiedBy = [CommonMapper::class])
    fun map(dto: Set<MaterialTypeDto>): List<MaterialType>

    @Mappings(
        Mapping(target = "memo", ignore = true),
        Mapping(target = "code", ignore = true),
    )
    fun mapProjection(projection: MaterialTypeProjection): SimpleMaterialTypeDto
}
