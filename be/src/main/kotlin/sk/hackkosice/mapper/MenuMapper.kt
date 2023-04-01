package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.MenuDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.menu.VMenuTranslated
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface MenuMapper {

    @Mappings(
        Mapping(
            source = "nameTranslated",
            target = "name",
        ),
        Mapping(
            source = "memoTranslated",
            target = "memo",
            qualifiedByName = ["menu-nullable-trans-mapper"]
        ),
        Mapping(source = "parentMenu.id", target = "parentId"),
        Mapping(source = "itemUrl", target = "url"),
        Mapping(
            source = "subMenu",
            target = "subMenu",
            qualifiedBy = [TranslatedLanguageMapper::class]
        )
    )
    @TranslatedLanguageMapper
    fun map(entity: VMenuTranslated): MenuDto

    @IterableMapping(qualifiedBy = [TranslatedLanguageMapper::class])
    @TranslatedLanguageMapper
    fun mapSet(menu: Set<VMenuTranslated>): List<MenuDto>

    @TranslatedLanguageMapper
    fun menuTreeList(menu: VMenuTranslated?): List<String?> {
        val retVal = mutableListOf<String?>()
        var parent = menu
        while (parent != null) {
            retVal.add(parent.nameTranslated)
            parent = parent.parentMenu
        }
        return retVal.reversed()
    }

    @EnglishLanguageMapper
    fun menuTreeListEn(menu: VMenuTranslated?): List<String?> {
        val retVal = mutableListOf<String?>()
        var parent = menu
        while (parent != null) {
            retVal.add(parent.nameEn)
            parent = parent.parentMenu
        }
        return retVal.reversed()
    }

    @Named("menu-nullable-trans-mapper")
    fun mapNullableTranslation(translation: String?): String? =
        if (translation != Constants.EMPTY_DB_STRING_VALUE) translation else null
}
