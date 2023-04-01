package sk.esten.uss.gbco2.service

import java.time.LocalDate
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.create.CreateMeterDto
import sk.esten.uss.gbco2.dto.request.filter.MeterFilter
import sk.esten.uss.gbco2.dto.request.filter.MeterReadAllFilter
import sk.esten.uss.gbco2.dto.response.MeterDto
import sk.esten.uss.gbco2.dto.response.MeterUncertaintyDto
import sk.esten.uss.gbco2.exceptions.DatabaseException
import sk.esten.uss.gbco2.exceptions.ValidationException
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.MeterMapper
import sk.esten.uss.gbco2.mapper.MeterUncertaintyMapper
import sk.esten.uss.gbco2.model.entity.meter.Meter
import sk.esten.uss.gbco2.model.entity.meter.VMeterTranslated
import sk.esten.uss.gbco2.model.entity.meter_uncertainty.MeterUncertainty
import sk.esten.uss.gbco2.model.repository.meter.MeterERepository
import sk.esten.uss.gbco2.model.repository.meter.MeterVRepository
import sk.esten.uss.gbco2.model.repository.meter_uncertainty.MeterUncertaintyERepository
import sk.esten.uss.gbco2.model.specification.MeterAllSpecification
import sk.esten.uss.gbco2.model.specification.MeterPageSpecification

@Service
class MeterService(
    override val entityRepository: MeterERepository,
    override val viewRepository: MeterVRepository,
    private val mapper: MeterMapper,
    private val meterUncertaintyMapper: MeterUncertaintyMapper,
    private val meterUncertaintyERepository: MeterUncertaintyERepository,
    private val nodeService: NodeService,
) :
    CrudServiceView<
        Meter,
        VMeterTranslated,
        MeterDto,
        MeterDto,
        CreateMeterDto,
        CreateMeterDto,
        Long,
        MeterFilter,
        MeterReadAllFilter>() {

    override fun viewToDto(entity: VMeterTranslated, translated: Boolean): MeterDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: CreateMeterDto): Meter {
        return mapper.map(createDto)
    }

    override fun updateEntity(updateDto: CreateMeterDto, entity: Meter) {
        mapper.update(updateDto, entity)
        entity.meterUncertainty?.let { meterUncertainty ->
            updateDto.meterUncertainty?.let { dto ->
                meterUncertaintyMapper.update(dto, meterUncertainty)
            }
        }
    }

    override fun repositoryPageQuery(filter: MeterFilter): Page<VMeterTranslated> {
        val specification = MeterPageSpecification(filter, nodeService)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    @Transactional
    override fun create(createDto: CreateMeterDto): MeterDto {
        try {
            val entity: Meter = createEntity(createDto)
            val meterUncertainty: MeterUncertainty? = entity.meterUncertainty

            if (meterUncertainty == null) {
                return persistInternal(entity)
            }
            entityRepository.save(entity)
            meterUncertaintyERepository.saveAndFlush(meterUncertainty)
            return getMeterDto(entity)
        } catch (e: DataIntegrityViolationException) {
            throw DatabaseException(e)
        }
    }

    @Transactional
    override fun update(id: Long, updateDto: CreateMeterDto): MeterDto {
        try {
            val entity: Meter = getInternal(id)
            updateEntity(updateDto, entity)
            val meterUncertainty: MeterUncertainty? = entity.meterUncertainty

            if (meterUncertainty == null) {
                meterUncertaintyERepository.deleteAllByMeter(entity)
                return persistInternal(entity)
            }
            meterUncertaintyERepository.save(meterUncertainty)
            entityRepository.saveAndFlush(entity)
            return getMeterDto(entity)
        } catch (e: DataIntegrityViolationException) {
            throw DatabaseException(e)
        }
    }

    private fun getMeterDto(entity: Meter): MeterDto {
        val pk: Long? = entity.getPk()
        return if (pk == null) {
            throw ValidationException(description = "Meter's primary key is NULL!")
        } else {
            viewToDto(getView(pk))
        }
    }

    override fun viewToDetailDto(entity: VMeterTranslated, translated: Boolean): MeterDto =
        viewToDto(entity, translated)

    fun getMeterUncertaintyValidFrom(meterId: Long, validFrom: LocalDate): MeterUncertaintyDto {
        return meterUncertaintyERepository
            .findByMeterIdAndValidFromLessThanEqual(meterId, validFrom)
            .firstOrNull()
            ?.let { meterUncertaintyMapper.map(it) }
            ?: MeterUncertaintyDto()
    }

    override fun repositoryGetAllQuery(filter: MeterReadAllFilter?): List<VMeterTranslated> {
        return filter?.let { viewRepository.findAll(MeterAllSpecification(it), it.toSort()) }
            ?: throw IllegalStateException("MeterReadAllFilter can not be null")
    }
}
