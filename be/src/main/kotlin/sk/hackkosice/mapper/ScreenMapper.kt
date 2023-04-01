package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.ScreenDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.screen.VScreenTranslated
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    uses = [RoleMapper::class, MenuMapper::class],
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
)
interface ScreenMapper {

    @Mappings(
        Mapping(
            source = "titleTranslated",
            target = "title",
            qualifiedByName = ["screen-nullable-trans-mapper"]
        ),
        Mapping(
            source = "helpLabelTranslated",
            target = "helpLabel",
            qualifiedByName = ["screen-nullable-trans-mapper"]
        ),
        Mapping(
            source = "menu",
            target = "menuItemsOrder",
            qualifiedBy = [TranslatedLanguageMapper::class],
        ),
        Mapping(
            source = "menu.menuRoles",
            target = "roles",
            qualifiedBy = [TranslatedLanguageMapper::class],
        )
    )
    @TranslatedLanguageMapper
    fun map(entity: VScreenTranslated): ScreenDto

    @Mappings(
        Mapping(
            source = "titleEn",
            target = "title",
            qualifiedByName = ["screen-nullable-trans-mapper"]
        ),
        Mapping(
            source = "helpLabelEn",
            target = "helpLabel",
            qualifiedByName = ["screen-nullable-trans-mapper"]
        ),
        Mapping(
            source = "menu",
            target = "menuItemsOrder",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "menu.menuRoles",
            target = "roles",
            qualifiedBy = [EnglishLanguageMapper::class]
        )
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VScreenTranslated): ScreenDto

    @Named("screen-nullable-trans-mapper")
    fun mapNullableTranslation(translation: String?): String? =
        if (translation != Constants.EMPTY_DB_STRING_VALUE) translation else null
}
