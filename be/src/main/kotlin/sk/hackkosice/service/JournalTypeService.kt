package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.JournalTypeDto
import sk.esten.uss.gbco2.exceptions.ForbiddenException
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.JournalTypeMapper
import sk.esten.uss.gbco2.model.entity.journal_type.JournalType
import sk.esten.uss.gbco2.model.entity.journal_type.VJournalTypeTranslated
import sk.esten.uss.gbco2.model.repository.journal_type.JournalTypeERepository
import sk.esten.uss.gbco2.model.repository.journal_type.JournalTypeVRepository

@Service
class JournalTypeService(
    override val entityRepository: JournalTypeERepository,
    override val viewRepository: JournalTypeVRepository,
    private val mapper: JournalTypeMapper,
) :
    CrudServiceView<
        JournalType,
        VJournalTypeTranslated,
        JournalTypeDto,
        JournalTypeDto,
        JournalTypeDto,
        JournalTypeDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun viewToDto(entity: VJournalTypeTranslated, translated: Boolean): JournalTypeDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: JournalTypeDto): JournalType {
        throw ForbiddenException("Create method for entity JournalType is not allowed")
    }

    override fun updateEntity(updateDto: JournalTypeDto, entity: JournalType) {
        throw ForbiddenException("Update method for entity JournalType is not allowed")
    }

    override fun viewToDetailDto(
        entity: VJournalTypeTranslated,
        translated: Boolean
    ): JournalTypeDto = viewToDto(entity, translated)
}
