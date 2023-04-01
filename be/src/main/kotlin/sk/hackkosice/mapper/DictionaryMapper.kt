package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateDictionaryDto
import sk.esten.uss.gbco2.dto.response.DictionaryDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.dictionary.DictionaryFullyMapped
import sk.esten.uss.gbco2.model.entity.dictionary.VDictionaryTranslated
import sk.esten.uss.gbco2.service.LanguageService

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [LanguageMapper::class, LanguageService::class]
)
interface DictionaryMapper {

    @Mapping(
        source = "language",
        target = "language",
        qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
    )
    fun map(entity: VDictionaryTranslated): DictionaryDto

    @Mapping(
        source = "language",
        target = "language",
        qualifiedBy = [SimpleMapper::class, EnglishLanguageMapper::class]
    )
    fun mapEn(entity: VDictionaryTranslated): DictionaryDto

    @Mappings(
        Mapping(source = "languageId", target = "language"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "dictionaryTable", ignore = true),
    )
    @IgnoreBaseAttributes
    fun map(createDto: CreateDictionaryDto): DictionaryFullyMapped

    @Mappings(
        Mapping(source = "languageId", target = "language"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "dictionaryTable", ignore = true),
        Mapping(target = "key", ignore = true),
    )
    @IgnoreBaseAttributes
    fun update(updateDto: CreateDictionaryDto, @MappingTarget entity: DictionaryFullyMapped)
}
