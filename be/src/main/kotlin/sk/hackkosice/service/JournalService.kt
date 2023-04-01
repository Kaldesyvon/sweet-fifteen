package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.JournalFilter
import sk.esten.uss.gbco2.dto.response.JournalDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.JournalMapper
import sk.esten.uss.gbco2.model.entity.journal.Journal
import sk.esten.uss.gbco2.model.entity.journal.VJournalTranslated
import sk.esten.uss.gbco2.model.repository.journal.JournalERepository
import sk.esten.uss.gbco2.model.repository.journal.JournalVRepository
import sk.esten.uss.gbco2.model.specification.JournalSpecification

@Service
class JournalService(
    override val entityRepository: JournalERepository,
    override val viewRepository: JournalVRepository,
    private val mapper: JournalMapper,
) :
    CrudServiceView<
        Journal,
        VJournalTranslated,
        JournalDto,
        JournalDto,
        JournalDto,
        JournalDto,
        Long,
        JournalFilter,
        ReadAllParamsFilterDto>() {
    override fun viewToDto(entity: VJournalTranslated, translated: Boolean): JournalDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: JournalDto): Journal {
        throw NotImplementedException("Not yet implemented")
    }

    override fun updateEntity(updateDto: JournalDto, entity: Journal) {
        throw NotImplementedException("Not yet implemented")
    }

    override fun repositoryPageQuery(filter: JournalFilter): Page<VJournalTranslated> {
        val specification = JournalSpecification(filter)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    override fun viewToDetailDto(entity: VJournalTranslated, translated: Boolean): JournalDto =
        viewToDto(entity, translated)
}
