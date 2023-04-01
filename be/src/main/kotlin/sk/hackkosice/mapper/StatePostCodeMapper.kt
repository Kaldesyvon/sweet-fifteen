package sk.esten.uss.gbco2.mapper

import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import org.mapstruct.ReportingPolicy
import sk.esten.uss.gbco2.dto.response.StatePostCodeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleStatePostCodeDto
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.model.entity.state_post_code.StatePostCode

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface StatePostCodeMapper {

    @SimpleMapper fun mapToSimple(entity: StatePostCode): SimpleStatePostCodeDto

    fun map(entity: StatePostCode): StatePostCodeDto
}
