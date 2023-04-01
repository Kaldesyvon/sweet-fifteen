package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.BalanceResultDto
import sk.esten.uss.gbco2.model.entity.balance_result.VResultsMonthBco2
import sk.esten.uss.gbco2.model.entity.balance_result.VResultsMonthCorp

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface BalanceResultsMapper {

    @Mapping(target = "month", expression = "java(entity.getMonth().getMonthValue())")
    @Mapping(target = "year", expression = "java(entity.getMonth().getYear())")
    fun map(entity: VResultsMonthBco2): BalanceResultDto

    @Mappings(
        Mapping(target = "month", expression = "java(entity.getMonth().getMonthValue())"),
        Mapping(target = "year", expression = "java(entity.getMonth().getYear())"),
        Mapping(target = "uncertaintyInput", ignore = true),
        Mapping(target = "uncertaintyOutput", ignore = true)
    )
    fun map(entity: VResultsMonthCorp): BalanceResultDto
}
