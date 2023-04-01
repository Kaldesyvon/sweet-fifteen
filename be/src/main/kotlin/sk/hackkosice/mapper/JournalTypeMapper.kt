package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.JournalTypeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleJournalTypeDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.journal_type.VJournalTypeTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface JournalTypeMapper {

    @Mapping(
        source = "nameTranslated",
        target = "name",
    )
    @TranslatedLanguageMapper
    fun map(entity: VJournalTypeTranslated): JournalTypeDto

    @Mapping(
        source = "nameEn",
        target = "name",
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VJournalTypeTranslated): JournalTypeDto

    @Mapping(
        source = "nameTranslated",
        target = "name",
    )
    @TranslatedLanguageMapper
    @SimpleMapper
    fun mapSimple(entity: VJournalTypeTranslated): SimpleJournalTypeDto

    @Mapping(
        source = "nameEn",
        target = "name",
    )
    @EnglishLanguageMapper
    @SimpleMapper
    fun mapSimpleEn(entity: VJournalTypeTranslated): SimpleJournalTypeDto
}
