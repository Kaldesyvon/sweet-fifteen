package sk.esten.uss.gbco2.service

import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateMeterCalibrationDto
import sk.esten.uss.gbco2.dto.request.filter.MeterCalibrationFilter
import sk.esten.uss.gbco2.dto.response.MeterCalibrationDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.MeterCalibrationMapper
import sk.esten.uss.gbco2.model.entity.meter_calibration.MeterCalibration
import sk.esten.uss.gbco2.model.entity.meter_calibration.VMeterCalibrationTranslated
import sk.esten.uss.gbco2.model.repository.meter_calibration.MeterCalibrationERepository
import sk.esten.uss.gbco2.model.repository.meter_calibration.MeterCalibrationVRepository
import sk.esten.uss.gbco2.model.specification.MeterCalibrationSpecification

@Service
class MeterCalibrationService(
    private val mapper: MeterCalibrationMapper,
    override val entityRepository: MeterCalibrationERepository,
    override val viewRepository: MeterCalibrationVRepository
) :
    CrudServiceView<
        MeterCalibration,
        VMeterCalibrationTranslated,
        MeterCalibrationDto,
        MeterCalibrationDto,
        CreateMeterCalibrationDto,
        CreateMeterCalibrationDto,
        Long,
        MeterCalibrationFilter,
        ReadAllParamsFilterDto>() {

    override fun createEntity(createDto: CreateMeterCalibrationDto): MeterCalibration {
        return mapper.map(createDto)
    }

    override fun updateEntity(updateDto: CreateMeterCalibrationDto, entity: MeterCalibration) {
        mapper.update(updateDto, entity)
    }

    override fun viewToDto(
        entity: VMeterCalibrationTranslated,
        translated: Boolean
    ): MeterCalibrationDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun viewToDetailDto(
        entity: VMeterCalibrationTranslated,
        translated: Boolean
    ): MeterCalibrationDto = viewToDto(entity, translated)

    override fun repositoryPageQuery(
        filter: MeterCalibrationFilter
    ): Page<VMeterCalibrationTranslated> {
        return viewRepository.findAll(MeterCalibrationSpecification(filter), filter.toPageable())
    }
}
