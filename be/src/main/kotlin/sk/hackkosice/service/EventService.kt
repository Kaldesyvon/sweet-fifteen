package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateEventDto
import sk.esten.uss.gbco2.dto.request.filter.EventFilter
import sk.esten.uss.gbco2.dto.response.EventDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.EventMapper
import sk.esten.uss.gbco2.model.entity.event.Event
import sk.esten.uss.gbco2.model.entity.event.VEventTranslated
import sk.esten.uss.gbco2.model.repository.event.EventERepository
import sk.esten.uss.gbco2.model.repository.event.EventVRepository
import sk.esten.uss.gbco2.model.specification.EventSpecification
import sk.esten.uss.gbco2.utils.principal

@Service
class EventService(
    override val entityRepository: EventERepository,
    override val viewRepository: EventVRepository,
    private val mapper: EventMapper,
) :
    CrudServiceView<
        Event,
        VEventTranslated,
        EventDto,
        EventDto,
        CreateEventDto,
        CreateEventDto,
        Long,
        EventFilter,
        ReadAllParamsFilterDto>() {

    override fun viewToDto(entity: VEventTranslated, translated: Boolean): EventDto {
        val dto: EventDto = mapper.map(entity)

        val params = entity.params?.split(";")
        if (!params.isNullOrEmpty()) {
            var detailText = dto.eventType?.detailTranslated

            params.forEachIndexed { index, param ->
                detailText = detailText?.replace("%${index + 1}\$s", param)
            }
            dto.eventType?.detailTranslated = detailText
        }
        return dto
    }

    override fun repositoryPageQuery(filter: EventFilter): Page<VEventTranslated> {
        val specification = EventSpecification(filter)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    override fun viewToDetailDto(entity: VEventTranslated, translated: Boolean): EventDto =
        viewToDto(entity, translated)

    override fun createEntity(createDto: CreateEventDto): Event {
        return mapper.map(createDto)
    }

    override fun updateEntity(updateDto: CreateEventDto, entity: Event) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    @Transactional
    fun logEvent(method: String, eventTypeTextK: String, params: String): Event {
        return entityRepository.save(
            createEntity(
                CreateEventDto().apply {
                    this.method = method
                    this.eventTypeTextK = eventTypeTextK
                    this.params = params
                    this.createdBy = principal()?.loginAd ?: "admin"
                }
            )
        )
    }

    fun updateEvent(event: Event) {
        entityRepository.save(event)
    }
}
