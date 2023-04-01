package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateEventDto
import sk.esten.uss.gbco2.dto.response.EventDto
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.model.entity.event.Event
import sk.esten.uss.gbco2.model.entity.event.VEventTranslated
import sk.esten.uss.gbco2.service.EventTypeService

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [EventTypeMapper::class, EventTypeService::class]
)
interface EventMapper {

    @Mapping(
        source = "eventType",
        target = "eventType",
        qualifiedBy = [TranslatedLanguageMapper::class]
    )
    @TranslatedLanguageMapper
    fun map(entity: VEventTranslated): EventDto

    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(target = "eventDate", ignore = true),
        Mapping(target = "created", ignore = true),
        Mapping(source = "eventTypeTextK", target = "eventType")
    )
    fun map(dto: CreateEventDto): Event
}
