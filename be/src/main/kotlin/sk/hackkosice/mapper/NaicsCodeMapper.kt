package sk.esten.uss.gbco2.mapper

import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import org.mapstruct.ReportingPolicy
import sk.esten.uss.gbco2.dto.response.simple.SimpleNaicsCodeDto
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.model.entity.naics_codes.NaicsCode

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface NaicsCodeMapper {
    @SimpleMapper fun mapToSimple(entity: NaicsCode): SimpleNaicsCodeDto
}