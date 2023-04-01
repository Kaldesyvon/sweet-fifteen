package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.EventTypeDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.EventTypeMapper
import sk.esten.uss.gbco2.model.entity.event_type.EventType
import sk.esten.uss.gbco2.model.entity.event_type.VEventTypeTranslated
import sk.esten.uss.gbco2.model.repository.event_type.EventTypeERepository
import sk.esten.uss.gbco2.model.repository.event_type.EventTypeVRepository

@Service
class EventTypeService(
    override val entityRepository: EventTypeERepository,
    override val viewRepository: EventTypeVRepository,
    private val mapper: EventTypeMapper,
) :
    CrudServiceView<
        EventType,
        VEventTypeTranslated,
        EventTypeDto,
        EventTypeDto,
        EventTypeDto,
        EventTypeDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun viewToDto(entity: VEventTypeTranslated, translated: Boolean): EventTypeDto {
        return mapper.map(entity)
    }

    override fun createEntity(createDto: EventTypeDto): EventType {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun updateEntity(updateDto: EventTypeDto, entity: EventType) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    override fun viewToDetailDto(entity: VEventTypeTranslated, translated: Boolean): EventTypeDto =
        viewToDto(entity, translated)

    fun getByTextK(textK: String): EventType {
        return entityRepository.findByTextK(textK)
    }
}
