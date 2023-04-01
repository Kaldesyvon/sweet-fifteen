package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.filter.PlantResultFilter
import sk.esten.uss.gbco2.dto.request.filter.PlantResultProductEnum
import sk.esten.uss.gbco2.dto.request.filter.QuantityMonthFilter
import sk.esten.uss.gbco2.dto.request.filter.ScopeDenominatorFilter
import sk.esten.uss.gbco2.service.ScopeDenominatorService
import sk.esten.uss.gbco2.utils.principal

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface PlantResultMapper {

    @Mappings(
        Mapping(target = "materialIds", ignore = true),
        Mapping(target = "unitSetId", ignore = true)
    )
    fun mapFilter(
        plantResultFilter: PlantResultFilter,
        @Context scopeDenominatorService: ScopeDenominatorService
    ): QuantityMonthFilter

    @AfterMapping
    fun mapFilter(
        plantResultFilter: PlantResultFilter,
        @MappingTarget target: QuantityMonthFilter,
        @Context scopeDenominatorService: ScopeDenominatorService
    ) {
        target.unitSetId = principal()?.unitSetId
        val materialIds = mutableListOf<Long>()
        if (plantResultFilter.product == PlantResultProductEnum.DEFAULT) {
            materialIds.addAll(
                scopeDenominatorService
                    .getAllWithFilter(ScopeDenominatorFilter().apply { plantResultFilter.scopeId })
                    .asSequence()
                    .mapNotNull { it.id }
            )
        } else {
            plantResultFilter.materialProductId?.let { materialIds.add(it) }
        }

        target.materialIds = materialIds
    }
}
