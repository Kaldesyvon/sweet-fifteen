package sk.esten.uss.gbco2.mapper

import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import org.mapstruct.ReportingPolicy
import sk.esten.uss.gbco2.dto.response.YearDto
import sk.esten.uss.gbco2.model.entity.year.Year

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface YearMapper {

    fun map(entity: Year): YearDto
}
