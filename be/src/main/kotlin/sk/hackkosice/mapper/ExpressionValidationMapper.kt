package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.ExpressionValidationDto
import sk.esten.uss.gbco2.model.entity.expression_validation.ExpressionValidationProjection

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface ExpressionValidationMapper {
    @Mappings(Mapping(target = "missingParameters", ignore = true))
    fun mapProjection(projection: ExpressionValidationProjection): ExpressionValidationDto
}
