package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.ScopeDenominatorFilter
import sk.esten.uss.gbco2.dto.response.ScopeDenominatorDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.ScopeDenominatorMapper
import sk.esten.uss.gbco2.model.entity.scope_denominator.ScopeDenominator
import sk.esten.uss.gbco2.model.entity.scope_denominator.VScopeDenominatorTranslated
import sk.esten.uss.gbco2.model.repository.scope_denominator.ScopeDenominatorERepository
import sk.esten.uss.gbco2.model.repository.scope_denominator.ScopeDenominatorVRepository
import sk.esten.uss.gbco2.model.specification.ScopeDenominatorSpecification

@Service
class ScopeDenominatorService(
    override val entityRepository: ScopeDenominatorERepository,
    override val viewRepository: ScopeDenominatorVRepository,
    private val mapper: ScopeDenominatorMapper
) :
    CrudServiceView<
        ScopeDenominator,
        VScopeDenominatorTranslated,
        ScopeDenominatorDto,
        ScopeDenominatorDto,
        ScopeDenominatorDto,
        ScopeDenominatorDto,
        Long,
        PageableParamsFilterDto,
        ScopeDenominatorFilter>() {

    override fun viewToDto(
        entity: VScopeDenominatorTranslated,
        translated: Boolean
    ): ScopeDenominatorDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun viewToDetailDto(
        entity: VScopeDenominatorTranslated,
        translated: Boolean
    ): ScopeDenominatorDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: ScopeDenominatorDto): ScopeDenominator {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun updateEntity(updateDto: ScopeDenominatorDto, entity: ScopeDenominator) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    override fun repositoryGetAllQuery(
        filter: ScopeDenominatorFilter?
    ): List<VScopeDenominatorTranslated> {
        val specification = ScopeDenominatorSpecification(filter)
        return viewRepository.findAll(
            specification,
            filter?.toSort() ?: Sort.by(Sort.Direction.ASC, "id")
        )
    }

    fun deleteAll(scopeDenominators: Set<ScopeDenominator>) {
        entityRepository.deleteAllInBatch(scopeDenominators)
    }

    fun saveAll(scopeDenominators: Set<ScopeDenominator>) {
        entityRepository.saveAllAndFlush(scopeDenominators)
    }
}
