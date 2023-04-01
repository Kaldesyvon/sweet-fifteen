package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.model.entity.adv_expr_log.AdvExprLog
import sk.esten.uss.gbco2.model.repository.adv_expr_log.AdvAdvExprLogERepository
import sk.esten.uss.gbco2.model.repository.adv_expr_log.AdvExprLogERepository

@Service
class AdvExprLogService(
    private val entityRepository: AdvExprLogERepository,
    private val advEntityRepository: AdvAdvExprLogERepository,
) {
    fun getAllByNodesAndYear(nodeIds: List<Long>, year: Int): List<AdvExprLog> =
        advEntityRepository.findByNodeAndYear(nodeIds, year)

    fun syncWithDb(
        toInsert: List<AdvExprLog>,
        toUpdate: List<AdvExprLog>,
        toDelete: List<AdvExprLog>
    ) {
        entityRepository.saveAll(toInsert)
        entityRepository.saveAll(toUpdate)
        entityRepository.deleteAll(toDelete)
    }

    fun findAllNotValidByNodesAndYear(nodeIds: List<Long>, year: Int): List<AdvExprLog> {
        return advEntityRepository.findAllByNodeIdsAndYear(nodeIds, year)
    }
}
