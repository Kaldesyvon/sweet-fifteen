package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.ScopeMaterialNodeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleScopeMaterialNodeDto
import sk.esten.uss.gbco2.mapper.annotation.CommonMapper
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.scope_material_node.VScopeMaterialNodeTranslated
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [MaterialNodeMapper::class]
)
interface ScopeMaterialNodeMapper {

    @Mappings(
        Mapping(
            source = "materialNode",
            target = "materialNode",
            qualifiedBy = [TranslatedLanguageMapper::class, CommonMapper::class]
        ),
        Mapping(target = "scope", ignore = true)
    )
    @TranslatedLanguageMapper
    fun map(entity: VScopeMaterialNodeTranslated): ScopeMaterialNodeDto

    @Mappings(
        Mapping(
            source = "materialNode",
            target = "materialNode",
            qualifiedBy = [EnglishLanguageMapper::class, CommonMapper::class]
        ),
        Mapping(target = "scope", ignore = true)
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VScopeMaterialNodeTranslated): ScopeMaterialNodeDto

    @SimpleMapper fun mapToSimple(entity: VScopeMaterialNodeTranslated): SimpleScopeMaterialNodeDto

    @IterableMapping(qualifiedBy = [TranslatedLanguageMapper::class])
    @TranslatedLanguageMapper
    fun map(entity: Set<VScopeMaterialNodeTranslated>): List<ScopeMaterialNodeDto>

    @IterableMapping(qualifiedBy = [EnglishLanguageMapper::class])
    @EnglishLanguageMapper
    fun mapEn(entity: Set<VScopeMaterialNodeTranslated>): List<ScopeMaterialNodeDto>

    @IterableMapping(qualifiedBy = [SimpleMapper::class])
    @SimpleMapper
    fun mapToSimple(entity: Set<VScopeMaterialNodeTranslated>): List<SimpleScopeMaterialNodeDto>
}
