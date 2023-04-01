package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateMeterCalibrationDto
import sk.esten.uss.gbco2.dto.response.MeterCalibrationDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.meter_calibration.MeterCalibration
import sk.esten.uss.gbco2.model.entity.meter_calibration.VMeterCalibrationTranslated
import sk.esten.uss.gbco2.service.MeterService

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [MeterMapper::class, MeterService::class]
)
interface MeterCalibrationMapper {

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(source = "dto.meterId", target = "meter"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "calibrationBy", ignore = true)
    )
    fun map(dto: CreateMeterCalibrationDto): MeterCalibration

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(source = "dto.meterId", target = "meter"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "calibrationBy", ignore = true)
    )
    fun update(dto: CreateMeterCalibrationDto, @MappingTarget entity: MeterCalibration)

    @Mapping(source = "entity.meter", target = "meter", qualifiedBy = [SimpleMapper::class])
    @TranslatedLanguageMapper
    fun map(entity: VMeterCalibrationTranslated): MeterCalibrationDto

    @Mapping(source = "entity.meter", target = "meter", qualifiedBy = [SimpleMapper::class])
    @EnglishLanguageMapper
    fun mapEn(entity: VMeterCalibrationTranslated): MeterCalibrationDto
}
