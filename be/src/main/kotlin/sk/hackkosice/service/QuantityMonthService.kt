package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.QuantityMonthFilter
import sk.esten.uss.gbco2.dto.response.QuantityMonthDto
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.model.entity.quantity_month.QuantityMonth
import sk.esten.uss.gbco2.model.repository.quantity_month.AdvQuantityMonthERepository
import sk.esten.uss.gbco2.model.repository.quantity_month.QuantityMonthERepository
import sk.esten.uss.gbco2.model.specification.QuantityMonthSpecification

@Service
class QuantityMonthService(
    override val entityRepository: QuantityMonthERepository,
    private val advEntityRepository: AdvQuantityMonthERepository
) :
    CrudService<
        QuantityMonth,
        QuantityMonthDto,
        QuantityMonthDto,
        QuantityMonthDto,
        QuantityMonthDto,
        String,
        PageableParamsFilterDto,
        QuantityMonthFilter>() {

    override fun updateEntity(updateDto: QuantityMonthDto, entity: QuantityMonth) {
        throw NotImplementedException("This type of entity cannot be uodated")
    }

    override fun createEntity(createDto: QuantityMonthDto): QuantityMonth {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun entityToDto(entity: QuantityMonth, translated: Boolean): QuantityMonthDto {
        throw NotImplementedException("This is only helper view")
    }

    override fun entityToDetailDto(entity: QuantityMonth, translated: Boolean): QuantityMonthDto {
        throw NotImplementedException("This is only helper view")
    }

    override fun repositoryGetAllQuery(filter: QuantityMonthFilter?): List<QuantityMonth> {
        return if (filter != null) {
            entityRepository.findAll(QuantityMonthSpecification(filter), filter.toSort())
        } else super.repositoryGetAllQuery(null)
    }

    fun getAllByNodesMaterialsAndYears(
        nodeIds: List<Long?>,
        materialIds: List<Long?>,
        years: List<Int>,
        ytd: Int?,
        unitSetId: Long
    ): MutableList<QuantityMonth> {
        return advEntityRepository
            .findAllByNodesMaterialsAndYears(
                nodeIds,
                materialIds,
                years,
                //            ytd, // TODO: Add when needed - used is null 30.1. erik
                unitSetId
            )
            .toMutableList()
    }
}
