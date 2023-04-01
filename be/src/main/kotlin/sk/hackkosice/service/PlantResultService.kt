package sk.esten.uss.gbco2.service

import java.math.BigDecimal
import java.math.RoundingMode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.filter.ParetoResultFilter
import sk.esten.uss.gbco2.dto.request.filter.PlantResultFilter
import sk.esten.uss.gbco2.dto.request.filter.PlantResultProductEnum
import sk.esten.uss.gbco2.dto.response.ParetoResultDto
import sk.esten.uss.gbco2.dto.response.PlantResultDto
import sk.esten.uss.gbco2.dto.response.PlantResultResponseDto
import sk.esten.uss.gbco2.mapper.PlantResultMapper
import sk.esten.uss.gbco2.model.repository.results_month.AdvResultsMonthRepository
import sk.esten.uss.gbco2.utils.principalUnitSetIdOrMetricId

@Service
class PlantResultService(
    private val quantityMonthService: QuantityMonthService,
    private val resultsMonthService: ResultsMonthService,
    private val mapper: PlantResultMapper,
    private val scopeDenominatorService: ScopeDenominatorService,
    private val advResultsMonthRepository: AdvResultsMonthRepository
) {
    fun findPlantResultsWithFilter(plantResultFilter: PlantResultFilter): PlantResultResponseDto {
        var results = mutableListOf<PlantResultDto>()
        val total: BigDecimal
        val principalUnitSetId = principalUnitSetIdOrMetricId()

        if (plantResultFilter.product == PlantResultProductEnum.TOTAL) {
            results.addAll(
                resultsMonthService.findPlantResultsByCriteria(
                    plantResultFilter.nodeId,
                    plantResultFilter.scopeId,
                    principalUnitSetId,
                    plantResultFilter.getDisplayToDate(),
                    plantResultFilter.analysisParamId,
                    plantResultFilter.withSubNodes
                )
            )
            total = results.map { it.monthValue }.toList().fold(BigDecimal.ZERO, BigDecimal::add)
        } else {
            val partialResults =
                resultsMonthService.findPlantResultsByCriteria(
                    plantResultFilter.nodeId,
                    plantResultFilter.scopeId,
                    principalUnitSetId,
                    plantResultFilter.getDisplayToDate(),
                    plantResultFilter.analysisParamId,
                    plantResultFilter.withSubNodes
                )
            val partialResultsSum =
                partialResults.map { it.monthValue }.toList().fold(BigDecimal.ZERO, BigDecimal::add)

            val quantityResults =
                quantityMonthService.getAllWithFilter(
                    mapper.mapFilter(plantResultFilter, scopeDenominatorService)
                )
            val quantityResultsSum =
                quantityResults
                    .map { it.monthQuantity }
                    .toList()
                    .fold(BigDecimal.ZERO, BigDecimal::add)

            total =
                if (quantityResultsSum == BigDecimal.ZERO) BigDecimal.ZERO
                else partialResultsSum.divide(quantityResultsSum, 10, RoundingMode.HALF_UP)

            partialResults.forEach { t ->
                val sum =
                    quantityResults
                        .filter { it.month == t.month }
                        .map { it.monthQuantity }
                        .toList()
                        .fold(BigDecimal.ZERO, BigDecimal::add)
                results.add(
                    PlantResultDto(
                        t.month,
                        if (sum == BigDecimal.ZERO) null
                        else t.monthValue?.divide(sum, 10, RoundingMode.HALF_UP),
                        t.nodeId,
                        t.unitAbbrAv +
                            "/" +
                            (if (quantityResults.isNotEmpty()) quantityResults[0].unitAbbr else "")
                    )
                )
            }
        }

        if (plantResultFilter.withSubNodes) {
            results =
                results
                    .groupBy { it.month }
                    .map { (month, objList) ->
                        PlantResultDto(
                            month,
                            objList.fold(BigDecimal.ZERO) { acc, obj ->
                                acc + (obj.monthValue ?: BigDecimal.ZERO)
                            },
                            objList[0].nodeId,
                            objList[0].unitAbbrAv
                        )
                    }
                    .toMutableList()
        }

        return PlantResultResponseDto().apply {
            totalValue = total
            totalResults = results
        }
    }

    @Transactional
    fun getParetoResults(filter: ParetoResultFilter): List<ParetoResultDto> {
        return advResultsMonthRepository.getResultsForPareto(filter)
    }
}
