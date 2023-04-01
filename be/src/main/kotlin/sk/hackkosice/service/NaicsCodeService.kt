package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.NaicsCodeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNaicsCodeDto
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.mapper.NaicsCodeMapper
import sk.esten.uss.gbco2.model.entity.naics_codes.NaicsCode
import sk.esten.uss.gbco2.model.repository.naics_code.NaicsCodeERepository

@Service
class NaicsCodeService(
    private val mapper: NaicsCodeMapper,
    override val entityRepository: NaicsCodeERepository
) :
    CrudService<
        NaicsCode,
        SimpleNaicsCodeDto,
        SimpleNaicsCodeDto,
        NaicsCodeDto,
        NaicsCodeDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {
    override fun entityToDto(entity: NaicsCode, translated: Boolean): SimpleNaicsCodeDto {
        return mapper.mapToSimple(entity)
    }

    override fun createEntity(createDto: NaicsCodeDto): NaicsCode {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun updateEntity(updateDto: NaicsCodeDto, entity: NaicsCode) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    override fun entityToDetailDto(entity: NaicsCode, translated: Boolean): SimpleNaicsCodeDto {
        return mapper.mapToSimple(entity)
    }
}
