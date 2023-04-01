package sk.esten.uss.gbco2.mapper

import java.util.*
import org.mapstruct.*
import sk.esten.uss.gbco2.mapper.annotation.NullableTranslationMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslationKeyMapper
import sk.esten.uss.gbco2.model.entity.dictionary.Dictionary
import sk.esten.uss.gbco2.utils.Constants
import sk.esten.uss.gbco2.utils.mapTranslation

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface TranslationMapper {

    @TranslationKeyMapper
    fun generateKey(prefix: String): String {
        return "$prefix.${UUID.randomUUID()}"
    }

    fun translate(key: String?, trans: MutableMap<Long, Dictionary>?, translate: Boolean): String? {
        return mapTranslation(key, trans, translate)
    }

    @NullableTranslationMapper
    fun mapNullableTranslation(translation: String?): String? =
        if (translation != Constants.EMPTY_DB_STRING_VALUE) translation else null
}
