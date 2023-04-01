package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.StatePostCodeDto
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.mapper.StatePostCodeMapper
import sk.esten.uss.gbco2.model.entity.state_post_code.StatePostCode
import sk.esten.uss.gbco2.model.repository.state_post_code.StatePostCodeRepository

@Service
class StatePostCodeService(
    override val entityRepository: StatePostCodeRepository,
    private val mapper: StatePostCodeMapper,
) :
    CrudService<
        StatePostCode,
        StatePostCodeDto,
        StatePostCodeDto,
        StatePostCodeDto,
        StatePostCodeDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun entityToDto(entity: StatePostCode, translated: Boolean): StatePostCodeDto =
        mapper.map(entity)

    override fun createEntity(createDto: StatePostCodeDto): StatePostCode {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun updateEntity(updateDto: StatePostCodeDto, entity: StatePostCode) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    override fun entityToDetailDto(entity: StatePostCode, translated: Boolean): StatePostCodeDto =
        entityToDto(entity, translated)
}
