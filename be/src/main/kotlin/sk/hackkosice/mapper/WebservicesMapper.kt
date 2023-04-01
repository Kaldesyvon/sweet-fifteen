package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.WebservicesDto
import sk.esten.uss.gbco2.model.entity.webservices.Webservices

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface WebservicesMapper {

    @Mappings(
        Mapping(
            target = "dataReceivedSize",
            expression = "java(Long.valueOf(entity.getDataReceived().length()))"
        ),
        Mapping(
            target = "dataSentSize",
            expression = "java(Long.valueOf(entity.getDataSent().length()))"
        )
    )
    fun map(entity: Webservices): WebservicesDto
}
