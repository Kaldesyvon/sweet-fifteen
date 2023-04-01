package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.AdvStatusDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.AdvStatusMapper
import sk.esten.uss.gbco2.model.entity.adv_status.AdvStatus
import sk.esten.uss.gbco2.model.entity.adv_status.VAdvStatusTranslated
import sk.esten.uss.gbco2.model.repository.adv_status.AdvStatusERepository
import sk.esten.uss.gbco2.model.repository.adv_status.AdvStatusVRepository

@Service
class AdvStatusService(
    override val entityRepository: AdvStatusERepository,
    override val viewRepository: AdvStatusVRepository,
    private val mapper: AdvStatusMapper
) :
    CrudServiceView<
        AdvStatus,
        VAdvStatusTranslated,
        AdvStatusDto,
        AdvStatusDto,
        AdvStatusDto,
        AdvStatusDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {
    fun getById(id: Long): AdvStatus {
        return entityRepository.getReferenceById(id)
    }

    override fun viewToDto(entity: VAdvStatusTranslated, translated: Boolean): AdvStatusDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun viewToDetailDto(entity: VAdvStatusTranslated, translated: Boolean): AdvStatusDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: AdvStatusDto): AdvStatus {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun updateEntity(updateDto: AdvStatusDto, entity: AdvStatus) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    fun getByCode(code: String): AdvStatus {
        return entityRepository.findByNameK(code)
    }
}
