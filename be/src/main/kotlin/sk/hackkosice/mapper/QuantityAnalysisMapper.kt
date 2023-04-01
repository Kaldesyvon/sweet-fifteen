package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.QuantityAnalysisParamDto
import sk.esten.uss.gbco2.model.entity.quantity_analysis.QuantityAnalysisProjection

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
)
interface QuantityAnalysisMapper {

    @Mappings(
        Mapping(source = "analysisParamId", target = "analysisParam.id"),
        Mapping(source = "analysisParamName", target = "analysisParam.name"),
        Mapping(source = "analysisId", target = "analysis.id"),
        Mapping(source = "analysisValidFrom", target = "analysis.validFrom"),
        Mapping(source = "analysisFormattedFactorA", target = "analysis.formattedFactorA"),
        Mapping(source = "analysisFormattedFactorB", target = "analysis.formattedFactorB"),
        Mapping(source = "analysisFormattedUnitAbbrTo", target = "analysis.formattedUnitAbbrTo"),
    )
    fun map(entity: QuantityAnalysisProjection): QuantityAnalysisParamDto
}
