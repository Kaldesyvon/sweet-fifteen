package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.JournalDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.journal.VJournalTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [JournalTypeMapper::class]
)
interface JournalMapper {

    @Mapping(
        source = "journalType",
        target = "journalType",
        qualifiedBy = [TranslatedLanguageMapper::class]
    )
    @TranslatedLanguageMapper
    fun map(entity: VJournalTranslated): JournalDto

    @Mapping(
        source = "journalType",
        target = "journalType",
        qualifiedBy = [EnglishLanguageMapper::class]
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VJournalTranslated): JournalDto
}
