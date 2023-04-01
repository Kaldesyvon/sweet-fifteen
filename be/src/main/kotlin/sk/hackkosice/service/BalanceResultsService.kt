package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.filter.CorpBalanceResultsFilterDto
import sk.esten.uss.gbco2.dto.response.BalanceResultDto
import sk.esten.uss.gbco2.mapper.BalanceResultsMapper
import sk.esten.uss.gbco2.model.repository.balance_result.AdvVResultsMonthCorpRepository
import sk.esten.uss.gbco2.model.repository.balance_result.VResultsMonthBco2Repository
import sk.esten.uss.gbco2.utils.principalUnitSetIdOrMetricId

@Service
class BalanceResultsService(
    private val bco2Repository: VResultsMonthBco2Repository,
    private val corpRepository: AdvVResultsMonthCorpRepository,
    private val mapper: BalanceResultsMapper,
) {
    fun getKosiceBalanceResults(year: Int): List<BalanceResultDto> {
        return bco2Repository.findAllByYearAndIdUnitSet(year, principalUnitSetIdOrMetricId()).map {
            mapper.map(it)
        }
    }

    fun getCorporationBalanceResults(filter: CorpBalanceResultsFilterDto?): List<BalanceResultDto> {
        return filter?.let {
            return corpRepository.getCorpBalanceResultsByFilter(it).map { monthResult ->
                mapper.map(monthResult)
            }
        }
            ?: throw IllegalStateException("CorpBalanceResultsFilterDto can not be null")
    }
}
