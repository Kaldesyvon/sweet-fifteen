package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.RoleDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleRoleDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.menu.VMenuRole
import sk.esten.uss.gbco2.model.entity.role.VRoleTranslated
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface RoleMapper {

    @Mappings(
        Mapping(source = "nameTranslated", target = "name"),
        Mapping(
            source = "memoTranslated",
            target = "memo",
            qualifiedByName = ["role-nullable-trans-mapper"]
        )
    )
    @TranslatedLanguageMapper
    fun map(entity: VRoleTranslated): RoleDto

    @Mappings(
        Mapping(source = "nameEn", target = "name"),
        Mapping(
            source = "memoEn",
            target = "memo",
            qualifiedByName = ["role-nullable-trans-mapper"]
        )
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VRoleTranslated): RoleDto

    @Mapping(source = "nameTranslated", target = "name")
    @TranslatedLanguageMapper
    @SimpleMapper
    fun mapSimple(entity: VRoleTranslated): SimpleRoleDto

    @Mapping(source = "nameEn", target = "name")
    @EnglishLanguageMapper
    @SimpleMapper
    fun mapSimpleEn(entity: VRoleTranslated): SimpleRoleDto

    @IterableMapping(qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class])
    @EnglishLanguageMapper
    fun mapEn(entity: Set<VMenuRole>): List<SimpleRoleDto>

    @IterableMapping(qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class])
    @TranslatedLanguageMapper
    fun map(entity: Set<VMenuRole>): List<SimpleRoleDto>

    @TranslatedLanguageMapper
    @EnglishLanguageMapper
    @SimpleMapper
    fun fromMenuRole(menuRole: VMenuRole): VRoleTranslated? {
        return menuRole.role
    }

    @Named("role-nullable-trans-mapper")
    fun mapNullableTranslation(translation: String?): String? =
        if (translation != Constants.EMPTY_DB_STRING_VALUE) translation else null
}
