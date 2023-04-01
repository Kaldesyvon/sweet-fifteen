package sk.esten.uss.gbco2.service

import java.util.*
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.model.entity.results_month_adv.VResultsMonthAdv
import sk.esten.uss.gbco2.model.repository.results_month_adv.AdvResultsMonthAdvERepository
import sk.esten.uss.gbco2.utils.atEndOfYear
import sk.esten.uss.gbco2.utils.atStartOfYear

@Service
class ResultsMonthAdvService(
    private val entityRepository: AdvResultsMonthAdvERepository,
) {

    fun getAllByNodesMaterialsAndYear(
        nodeIds: List<Long>,
        materialIds: List<Long>,
        year: Int,
        advMonthsBack: Long,
        unitSetId: Long
    ): List<VResultsMonthAdv> {
        val dateFrom = year.atStartOfYear().minusMonths(advMonthsBack)
        val dateTo = year.atEndOfYear()

        return entityRepository.findByNodeAndYear(nodeIds, materialIds, unitSetId, dateFrom, dateTo)
    }
}
