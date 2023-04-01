package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.DictionaryUiDto
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.dictionary_ui.VDictionaryUiTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface DictionaryUiMapper {

    @Mapping(
        source = "valueTranslated",
        target = "translation",
    )
    @TranslatedLanguageMapper
    fun map(entity: VDictionaryUiTranslated): DictionaryUiDto
}
