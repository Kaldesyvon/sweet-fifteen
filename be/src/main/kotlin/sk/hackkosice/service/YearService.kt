package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.YearDto
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.mapper.YearMapper
import sk.esten.uss.gbco2.model.entity.year.Year
import sk.esten.uss.gbco2.model.repository.year.YearRepository

@Service
class YearService(
    override val entityRepository: YearRepository,
    private val mapper: YearMapper,
) :
    CrudService<
        Year,
        YearDto,
        YearDto,
        YearDto,
        YearDto,
        Short,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun entityToDto(entity: Year, translated: Boolean): YearDto {
        return mapper.map(entity)
    }

    override fun createEntity(createDto: YearDto): Year {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun updateEntity(updateDto: YearDto, entity: Year) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    fun findYear(id: Short): Year? {
        return findInternal(id)
    }

    override fun entityToDetailDto(entity: Year, translated: Boolean): YearDto =
        entityToDto(entity, translated)
}
