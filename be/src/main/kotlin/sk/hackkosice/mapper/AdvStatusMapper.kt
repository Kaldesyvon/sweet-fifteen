package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.AdvStatusDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.adv_status.VAdvStatusTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
)
interface AdvStatusMapper {

    @Mappings(
        Mapping(source = "nameEn", target = "name"),
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VAdvStatusTranslated): AdvStatusDto

    @Mappings(
        Mapping(source = "nameTranslated", target = "name"),
    )
    @TranslatedLanguageMapper
    fun map(entity: VAdvStatusTranslated): AdvStatusDto
}
