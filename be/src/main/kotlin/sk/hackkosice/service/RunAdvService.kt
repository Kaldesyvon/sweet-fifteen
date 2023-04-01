package sk.esten.uss.gbco2.service

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.*
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.config.SecurityCoroutineContext
import sk.esten.uss.gbco2.dto.request.ExpressionValidationDto
import sk.esten.uss.gbco2.exceptions.ForbiddenException
import sk.esten.uss.gbco2.exceptions.NotFoundException
import sk.esten.uss.gbco2.exceptions.ValidationException
import sk.esten.uss.gbco2.mapper.AdvDetailsMapper
import sk.esten.uss.gbco2.mapper.ParamMapper
import sk.esten.uss.gbco2.model.entity.adv_details.AdvDetails
import sk.esten.uss.gbco2.model.entity.adv_expr_log.AdvExprLog
import sk.esten.uss.gbco2.model.entity.adv_status.AdvStatusSuper
import sk.esten.uss.gbco2.model.entity.analysis_param.AnalysisParam
import sk.esten.uss.gbco2.model.entity.event.Event
import sk.esten.uss.gbco2.model.entity.material.Material
import sk.esten.uss.gbco2.model.entity.material_node.MaterialNode
import sk.esten.uss.gbco2.model.entity.param.Param
import sk.esten.uss.gbco2.model.entity.quantity_month.QuantityMonth
import sk.esten.uss.gbco2.model.entity.results_month_adv.VResultsMonthAdv
import sk.esten.uss.gbco2.model.repository.node.NodeERepository
import sk.esten.uss.gbco2.utils.Constants
import sk.esten.uss.gbco2.utils.principal
import sk.esten.uss.gbco2.utils.rangeOfYears

@Service
@Transactional
class RunAdvService(
    private val paramService: ParamService,
    private val paramMapper: ParamMapper,
    private val eventService: EventService,
    private val nodeService: NodeService,
    private val advDetailsService: AdvDetailsService,
    private val materialNodeAnalysisParamService: MaterialNodeAnalysisParamService,
    private val quantityService: QuantityService,
    private val materialNodeService: MaterialNodeService,
    private val resultsMonthAdvService: ResultsMonthAdvService,
    private val quantityMonthService: QuantityMonthService,
    private val advExprLogService: AdvExprLogService,
    private val advStatusService: AdvStatusService,
    private val advDetailsMapper: AdvDetailsMapper,
    private val nodeERepository: NodeERepository
) {
    var lastMaterial: Material? = null
    var lastAnalysisParam: AnalysisParam? = null
    var currentBasisMaterialNode: MaterialNode? = null
    var lastMonth: LocalDate? = null

    var quantityMonth: MutableList<QuantityMonth> = mutableListOf()

    val stats = DescriptiveStatistics()

    fun validateNodeAndReturnDefaultYear(nodeId: Long): Int {
        if (!nodeERepository.existsById(nodeId)) {
            throw ValidationException(description = "Can't run Adv on not existing node")
        }

        return paramService.getParamByCode(Param.DEFAULT_YEAR).value?.toInt()
            ?: throw NotFoundException("missingParamInParamsTable")
    }

    fun validateParam(): Param {
        val param: Param = paramService.getParamByCode(Param.IS_AUTOMATIC_DATA_VALIDATION_RUNNING)
        val isAdvRunning = param.value?.toBoolean() == true

        if (isAdvRunning) {
            throw ForbiddenException(description = "Adv is already running")
        }

        val lastAdvExecutedByUserParam = paramService.getParamByCode(Param.LAST_ADV_USER_EXEC)
        if (!lastAdvExecutedByUserParam.value.toBoolean()) {
            lastAdvExecutedByUserParam.dateValue = LocalDate.now()
            lastAdvExecutedByUserParam.id?.let {
                paramService.update(it, paramMapper.map(lastAdvExecutedByUserParam, true))
            }
        }

        return param
    }

    fun logEvent(event: String, params: String) {
        eventService.logEvent(Constants.RUN_ADV_METHOD, event, params)
    }

    fun updateParam(param: Param) {
        param.value = "0"
        param.modified = LocalDateTime.now()
        param.id?.let { paramService.update(it, paramMapper.map(param, true)) }
    }

    fun runAdv(nodeId: Long, runByUser: Boolean = true): Boolean {
        try {
            val defaultYear = validateNodeAndReturnDefaultYear(nodeId)
            val param = validateParam()
            logEvent(Event.START_OF_ADV, "${defaultYear};${principal()?.loginAd ?: "admin"}")

            val rootNodeId = nodeService.getRootNodeId()
            val nodeIds: List<Long> = nodeService.getAllSubNodeIds(rootNodeId)

            val job = launchJob(nodeId, defaultYear, nodeIds)
            job.start()

            logEvent(
                Event.SUCCESSFUL_END_OF_ADV,
                "${defaultYear};${principal()?.loginAd ?: "admin"}"
            )
            updateParam(param)

            return true
        } catch (e: Exception) {
            throw ValidationException(description = "Analysis param expression is not valid")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun launchJob(nodeId: Long, year: Int, nodeIds: List<Long>): Job {
        val nodeIdForAdv = nodeIds.first { it == nodeId }

        return GlobalScope.launch(SecurityCoroutineContext()) {
            withContext(Dispatchers.IO) {
                getAdvLogic(nodeIdForAdv, year)
                getAdvExpressionLogic(nodeIdForAdv, year)
            }
        }
    }

    fun getAdvLogic(nodeId: Long, year: Int) {
        val originalNodeIds = nodeService.getAllSubNodeIds(listOf(nodeId), year)
        val nodeIds =
            nodeService.getAllNodesToLevel(0, year, nodeId).filter { it.id != nodeId }.mapNotNull {
                it.id
            }

        val advDetails = advDetailsService.findByNodeAndYear(originalNodeIds, year)
        logEvent(Event.ADV_DATA_FOUND, advDetails.size.toString())

        val distinctAdvDetails = mutableSetOf<String>()
        val duplicatedAdvDetails =
            advDetails
                .filter {
                    !distinctAdvDetails.add(
                        "${it.month}|${it.materialNode?.id}|${it.analysisParam?.id}"
                    )
                }
                .toSet()
        advDetailsService.deleteMultiple(duplicatedAdvDetails)

        val materialNodeAnalysisParams =
            materialNodeAnalysisParamService.getAllByNodesAndProcessAdv(nodeIds, true)
        if (materialNodeAnalysisParams.isEmpty()) return

        val materialIds = mutableListOf<Long>()
        val nodeBasisIds = mutableListOf<Long>()
        val materialAdvBasisIds = mutableListOf<Long>()
        for (materialNodeAnalysisParam in materialNodeAnalysisParams) {
            materialNodeAnalysisParam.materialNode?.material?.id?.let { materialIds.add(it) }
            materialNodeAnalysisParam.materialNodeAdvBasis?.node?.id?.let { nodeBasisIds.add(it) }
            materialNodeAnalysisParam.materialNodeAdvBasis?.material?.id?.let {
                materialAdvBasisIds.add(it)
            }
        }

        val resultsPerMonth =
            resultsMonthAdvService.getAllByNodesMaterialsAndYear(
                nodeIds,
                materialIds,
                year,
                Constants.ADV_MONTHS_BACK,
                Constants.METRIC_UNIT_SET_ID
            )

        logEvent(Event.ADV_RESULT_COUNT, resultsPerMonth.size.toString())

        val years = year.rangeOfYears(2)

        quantityMonth =
            quantityMonthService.getAllByNodesMaterialsAndYears(
                nodeBasisIds,
                materialAdvBasisIds,
                years,
                null,
                Constants.METRIC_UNIT_SET_ID
            )

        logEvent(Event.ADV_QUANTITY_COUNT, quantityMonth.size.toString())
        stats.clear()

        val advDetailsToProcess: MutableList<AdvDetails> = mutableListOf()

        for (resultPerMonth in resultsPerMonth) {
            if (lastMaterial == null) lastMaterial = resultPerMonth.material
            if (lastAnalysisParam == null) lastAnalysisParam = resultPerMonth.analysisParam
            if (lastMonth == null) {
                lastMonth = resultPerMonth.month
                lastMonth?.month?.minus(1)
            }

            val qMonth = resultPerMonth.month

            val currentMaterialAnalysisParam =
                materialNodeAnalysisParams.find {
                    resultPerMonth.material?.id == it.materialNode?.material?.id &&
                        resultPerMonth.analysisParam?.id == it.analysisParam?.id
                }
                    ?: continue

            if (currentMaterialAnalysisParam.materialNodeAdvBasis?.material == null)
                throw NotFoundException(
                    "No basis defined for material ${resultPerMonth.material?.name} to perform ADV"
                )
            currentBasisMaterialNode = currentMaterialAnalysisParam.materialNodeAdvBasis

            if (qMonth?.year != year) {
                addToStats(resultPerMonth, qMonth)
                continue
            }

            val materialNodes =
                materialNodeService.getAllByNodeAndMaterial(
                    resultPerMonth.node?.id,
                    resultPerMonth.material?.id
                )
            var advStatusId = AdvStatusSuper.INSUFFICIENT_DATA

            val advDetail = advDetailsMapper.map(resultPerMonth)

            if (materialNodes.isNotEmpty()) advDetail.materialNode = materialNodes.first()
            else continue // materialNode cant be null

            advDetail.materialBasis = currentBasisMaterialNode?.material
            advDetail.advValid = false

            if (resultPerMonth.quantity?.equals(BigDecimal.ZERO) == true ||
                    resultPerMonth.analyticalValueA?.equals(BigDecimal.ZERO) == true
            )
                advDetail.factorA = BigDecimal.ZERO
            else {
                advDetail.factorA =
                    if ((resultPerMonth.quantity ?: BigDecimal.ZERO) > BigDecimal.ZERO) {
                        (resultPerMonth.quantity?.let { resultPerMonth.analyticalValueA?.div(it) })
                            ?.abs()
                            ?: BigDecimal.ZERO
                    } else {
                        BigDecimal.ZERO
                    }
            }
            if (resultPerMonth.quantity?.equals(BigDecimal.ZERO) == true ||
                    resultPerMonth.analyticalValueB?.equals(BigDecimal.ZERO) == true
            )
                advDetail.factorB = BigDecimal.ZERO
            else {
                advDetail.factorB =
                    if ((resultPerMonth.quantity ?: BigDecimal.ZERO) > BigDecimal.ZERO) {
                        (resultPerMonth.quantity?.let { resultPerMonth.analyticalValueB?.div(it) })
                            ?.abs()
                            ?: BigDecimal.ZERO
                    } else {
                        BigDecimal.ZERO
                    }
            }
            if (stats.n == 12.toLong() && lastMaterial?.id == resultPerMonth.material?.id) {
                val stdDeviation =
                    currentMaterialAnalysisParam.stdDeviation
                        ?: throw NotFoundException(
                            "Not Std. Deviation defined for material ${resultPerMonth.material?.name}"
                        )

                val mean = stats.mean.toBigDecimal().abs()
                val deviation =
                    stats.standardDeviation.times(stdDeviation.toDouble()).toBigDecimal()

                val valIntensityMin =
                    if (mean.subtract(deviation) < BigDecimal.ZERO) BigDecimal.ZERO
                    else mean.subtract(deviation)
                val valIntensityMax = mean.add(deviation)

                advDetail.valIntensityMin = valIntensityMin
                advDetail.valIntensityMax = valIntensityMax

                advDetail.intensityStd = stats.standardDeviation.toBigDecimal()
                advDetail.intensityMean = mean

                val analyticalValue = resultPerMonth.analyticalValue
                val pm1 = getBasisMaterialQuantity(resultPerMonth)

                advStatusId =
                    if (advDetail.valIntensity in (valIntensityMin..valIntensityMax))
                        AdvStatusSuper.VALID
                    else AdvStatusSuper.INVALID

                advDetail.valIntensity = analyticalValue?.divide(pm1)?.abs()
                advDetail.advParams =
                    "${currentBasisMaterialNode?.name} (ID:${currentBasisMaterialNode?.id}): ${pm1.toString()}"
                advDetail.valIntensityMonths = BigDecimal.valueOf(Constants.ADV_MONTHS_BACK)
            }

            advDetail.advStatus = advStatusService.getById(advStatusId)

            addToStats(resultPerMonth, qMonth)
            advDetailsToProcess.add(advDetail)
        }

        val toInsert =
            advDetailsToProcess.filter {
                advDetails.find { mnap ->
                    it.month == mnap.month &&
                        it.materialNode == mnap.materialNode &&
                        it.analysisParam == mnap.analysisParam
                } == null
            }
        val toUpdate =
            advDetailsToProcess.filter {
                advDetails
                    .find { mnap ->
                        it.month == mnap.month &&
                            it.materialNode == mnap.materialNode &&
                            it.analysisParam == mnap.analysisParam
                    }
                    ?.advValid == true
            }
        val toDelete =
            advDetailsToProcess.filter { !toInsert.contains(it) && !toUpdate.contains(it) }

        eventService.logEvent(
            Constants.RUN_ADV_METHOD,
            Event.ADV_INSERT_UPDATE_DELETE,
            "${toInsert.size};${toUpdate.size};${toDelete.size}"
        )
        advDetailsService.syncWithDb(toInsert, toUpdate, toDelete)
    }

    fun getAdvExpressionLogic(nodeIdParam: Long, yearParam: Int) {
        val nodeIds = nodeService.getAllSubNodeIds(listOf(nodeIdParam), yearParam)
        val expressionValidationData =
            materialNodeAnalysisParamService.getAllByNodesAndYear(nodeIds, yearParam)

        if (expressionValidationData.isEmpty()) return

        val expressionParameters =
            expressionValidationData
                .map {
                    mapOf(
                        it.analysisParamExpression to
                            it.analysisParamExpression?.split(Constants.ADV_DELIMTER)
                    )
                }
                .reduce { x, y -> x + y }

        val codes =
            expressionValidationData.flatMap {
                it.analysisParamExpression?.split(Constants.ADV_DELIMTER) ?: listOf()
            }

        val expressionValidationVariables =
            materialNodeAnalysisParamService.getExpressionValidationVariables(
                nodeIds,
                yearParam,
                codes
            )

        val invalidExpressions = mutableListOf<ExpressionValidationDto>()
        for (evd in expressionValidationData) {
            for (code in expressionParameters[evd.analysisParamExpression].orEmpty()) {
                if (!expressionValidationVariables.contains(evd.quantityId.toString() + code)) {
                    evd.missingParameters?.add(code)
                    invalidExpressions.add(evd)
                }
            }
        }

        val advExprLogs = mutableListOf<AdvExprLog>()
        val expressionValidationLog = advExprLogService.getAllByNodesAndYear(nodeIds, yearParam)

        for (invalidExpression in invalidExpressions) {
            advExprLogs.add(
                AdvExprLog().apply {
                    missingParameters = invalidExpression.missingParameters?.joinToString(";")
                    advStatus = advStatusService.getById(AdvStatusSuper.MISSING_EXPR_PARAMETERS)
                    year = yearParam
                    nodeName = nodeService.getById(nodeIdParam, true).name
                    materialNodeAnalysisParam =
                        materialNodeAnalysisParamService.get(
                            invalidExpression.materialNodeAnalysisParamId
                        )
                    quantity =
                        quantityService.get(invalidExpression.quantityId)
                            ?: throw NotFoundException(
                                "No quantity found with id: ${invalidExpression.quantityId}"
                            )
                    expr = invalidExpression.analysisParamExpression
                }
            )
        }

        val toInsert =
            advExprLogs.filter {
                expressionValidationLog.find { ev ->
                    it.materialNodeAnalysisParam?.id == ev.materialNodeAnalysisParam?.id &&
                        it.quantity?.id == ev.quantity?.id &&
                        it.expr == ev.expr
                } == null
            }

        val toUpdate =
            advExprLogs.filter {
                expressionValidationLog.find { ev ->
                    it.materialNodeAnalysisParam?.id == ev.materialNodeAnalysisParam?.id &&
                        it.quantity?.id == ev.quantity?.id &&
                        it.expr == ev.expr
                } != null
            }
        val toDelete = advExprLogs.filter { !toInsert.contains(it) && !toUpdate.contains(it) }

        logEvent(
            Event.ADV_INSERT_UPDATE_DELETE,
            "${toInsert.size};${toUpdate.size};${toDelete.size}"
        )
        advExprLogService.syncWithDb(toInsert, toUpdate, toDelete)
    }

    private fun addToStats(resultPerMonth: VResultsMonthAdv, qMonth: LocalDate?) {
        if (lastAnalysisParam?.id != resultPerMonth.analysisParam?.id ||
                lastMaterial?.id != resultPerMonth.material?.id
        ) {
            stats.clear()
            lastAnalysisParam = resultPerMonth.analysisParam
            lastMaterial = resultPerMonth.material
        }
        if (lastMonth?.month?.plus(1) != qMonth?.month) {
            stats.clear()
            lastMonth = resultPerMonth.month
        }

        val analyticalValue = resultPerMonth.analyticalValue
        val pm1 = getBasisMaterialQuantity(resultPerMonth)

        if (analyticalValue == null || pm1 == null || pm1 == BigDecimal.ZERO) {
            stats.clear()
        } else {
            stats.addValue(analyticalValue.divide(pm1, RoundingMode.HALF_UP).toDouble())
        }
    }

    private fun getBasisMaterialQuantity(resultPerMonth: VResultsMonthAdv): BigDecimal? {
        for (q in quantityMonth) {
            if (currentBasisMaterialNode?.material?.id == q.material?.id &&
                    currentBasisMaterialNode?.node?.id == q.node?.id &&
                    resultPerMonth.month == q.month
            )
                return q.monthQuantity
        }
        return null
    }
}
