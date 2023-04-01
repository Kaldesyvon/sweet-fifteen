package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.AdvDetailsFilter
import sk.esten.uss.gbco2.dto.response.AdvDetailDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.AdvDetailsMapper
import sk.esten.uss.gbco2.model.entity.adv_details.AdvDetails
import sk.esten.uss.gbco2.model.entity.adv_details.VAdvDetails
import sk.esten.uss.gbco2.model.entity.adv_details.VAdvDetailsTranslated
import sk.esten.uss.gbco2.model.entity.quantity.IOEnum
import sk.esten.uss.gbco2.model.repository.adv_details.AdvAdvDetailsERepository
import sk.esten.uss.gbco2.model.repository.adv_details.AdvDetailsERepository
import sk.esten.uss.gbco2.model.repository.adv_details.AdvDetailsVRepository
import sk.esten.uss.gbco2.model.repository.adv_details.VAdvDetailsERepository
import sk.esten.uss.gbco2.model.specification.AdvDetailsSpecification
import sk.esten.uss.gbco2.utils.atEndOfYear
import sk.esten.uss.gbco2.utils.atStartOfYear

@Service
class AdvDetailsService(
    override val entityRepository: AdvDetailsERepository,
    override val viewRepository: AdvDetailsVRepository,
    private val vAdvDetailsRepository: VAdvDetailsERepository,
    private val advEntityRepository: AdvAdvDetailsERepository,
    private val mapper: AdvDetailsMapper,
) :
    CrudServiceView<
        AdvDetails,
        VAdvDetailsTranslated,
        AdvDetailDto,
        AdvDetailDto,
        AdvDetailDto,
        AdvDetailDto,
        Long,
        AdvDetailsFilter,
        ReadAllParamsFilterDto>() {
    fun deleteMultiple(advDetailsList: Set<AdvDetails>) {
        entityRepository.deleteAll(advDetailsList)
    }

    fun findByNodeAndYear(nodeIds: List<Long>, year: Int): List<AdvDetails> {
        return advEntityRepository.findByNodeAndYear(nodeIds, year)
    }

    fun syncWithDb(
        toInsert: List<AdvDetails>,
        toUpdate: List<AdvDetails>,
        toDelete: List<AdvDetails>
    ) {
        entityRepository.saveAll(toInsert)
        entityRepository.saveAll(toUpdate)
        entityRepository.deleteAll(toDelete)
    }

    override fun viewToDto(entity: VAdvDetailsTranslated, translated: Boolean): AdvDetailDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun viewToDetailDto(entity: VAdvDetailsTranslated, translated: Boolean): AdvDetailDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: AdvDetailDto): AdvDetails {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun updateEntity(updateDto: AdvDetailDto, entity: AdvDetails) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    override fun repositoryPageQuery(filter: AdvDetailsFilter): Page<VAdvDetailsTranslated> {
        val specification = AdvDetailsSpecification(filter)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    fun findAllByCriteria(
        unitSetId: Long?,
        nodeId: Long?,
        materialId: Long?,
        io: IOEnum?,
        year: Int,
        advStatusId: Long?,
        analysisParamId: Long?
    ): List<VAdvDetails> {
        val dateFrom = year.atStartOfYear()
        val dateTo = year.atEndOfYear()
        return vAdvDetailsRepository.findAll(
            unitSetId,
            nodeId,
            materialId,
            io,
            dateFrom,
            dateTo,
            advStatusId,
            analysisParamId
        )
    }
}
