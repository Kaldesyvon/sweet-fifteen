package sk.esten.uss.gbco2.service

import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.config.SecurityCoroutineContext
import sk.esten.uss.gbco2.exceptions.ForbiddenException
import sk.esten.uss.gbco2.model.entity.analysis.Analysis
import sk.esten.uss.gbco2.model.entity.analysis_param.AnalysisParam
import sk.esten.uss.gbco2.model.entity.event.Event
import sk.esten.uss.gbco2.model.entity.material_node.MaterialNode
import sk.esten.uss.gbco2.model.entity.param.Param
import sk.esten.uss.gbco2.model.entity.quantity.Quantity
import sk.esten.uss.gbco2.model.entity.quantity_analysis.QuantityAnalysis
import sk.esten.uss.gbco2.model.entity.quantity_analysis.QuantityAnalysisUpdate
import sk.esten.uss.gbco2.utils.logger
import sk.esten.uss.gbco2.utils.principal

@Service
@Transactional
class AutomaticAssignmentService(
    private val paramService: ParamService,
    private val eventService: EventService,
    private val nodeService: NodeService,
    private val materialNodeService: MaterialNodeService,
    private val dictionaryService: DictionaryService,
    private val materialService: MaterialService,
    private val analysisService: AnalysisService,
    private val quantityService: QuantityService
) {
    private var quantityList: MutableList<Quantity> = mutableListOf()
    private var analyses: MutableList<Analysis> = mutableListOf()

    fun checkAndSetRunningState(executedByUser: Boolean) {
        val param: Param = paramService.getParamByCode(Param.IS_AUTOMATIC_ASSIGNMENT_RUNNING)

        if (param.value != Param.NOT_RUNNING) {
            throw ForbiddenException(description = "automaticAssignmentIsAlreadyRunning")
        }

        param.value = Param.RUNNING
        paramService.updateParam(param)

        if (!executedByUser) {
            val lastExecParam: Param =
                paramService.getParamByCode(Param.LAST_AUTOMATIC_ASSIGNMENT_JOB_EXEC)
            lastExecParam.dateValue = LocalDate.now()
            paramService.updateParam(lastExecParam)
        }
    }

    private fun transformToParam(stringList: MutableList<String>): String {
        return stringList.map { it.split(";").take(3).joinToString(";") }.toString()
    }

    fun runAAFromEP(nodeId: Long, year: Int, materialId: Long): Job {
        val nodeIds = nodeService.getAllowedNodeIds(nodeId, year)

        checkAndSetRunningState(true)
        return runAAJob(nodeId, nodeIds, year, listOf(materialId), true)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun runAAJob(
        selectedNodeId: Long? = null,
        nodeIdsParam: List<Long>? = null,
        year: Int? = null,
        materialIds: List<Long>?,
        runByUser: Boolean = true
    ): Job {
        return GlobalScope.launch(SecurityCoroutineContext()) {
            withContext(Dispatchers.IO) {
                val selectedNodeForParam = selectedNodeId ?: nodeService.getRootNodeId()
                val params: String?
                //        try {
                params = generateEventLogParams(year, selectedNodeForParam, materialIds)
                eventService.logEvent(
                    Event.RUN_AUTO_ASSIGNMENT_METHOD,
                    Event.START_OF_AUTOMATIC_ASSIGNMENT,
                    "$params;${principal()?.loginAd ?: "admin"}"
                )

                val nodeIds =
                    if (nodeIdsParam.isNullOrEmpty())
                        (year?.let { nodeService.getAllNodesByYear(it) }?.mapNotNull { it?.id })
                    else nodeIdsParam

                val sizesOfSync: MutableList<String> = mutableListOf()

                val count: Int? = nodeIds?.size
                var event: Event? = null

                if (nodeIds != null && year != null && materialIds != null) {
                    for ((index, nodeId) in nodeIds.withIndex()) {
                        val eventParams = "$index;$count;${nodeService.getTranslatedName(nodeId)}"
                        if (event == null) {
                            event =
                                eventService.logEvent(
                                    Event.RUN_AUTO_ASSIGNMENT_METHOD,
                                    Event.AAA_PROCESSING_NODE,
                                    eventParams
                                )
                        } else {
                            event.created = LocalDateTime.now()
                            event.params = eventParams
                            eventService.updateEvent(event)
                        }

                        val result: String =
                            getAutomaticAssignmentLogic(
                                listOf(nodeId),
                                year,
                                materialIds,
                                runByUser
                            )
                        sizesOfSync.add(result)
                    }
                }

                if (event != null) {
                    eventService.delete(event.id)
                }

                val paramsOfAllSizes: String = transformToParam(sizesOfSync)
                eventService.logEvent(
                    Event.RUN_AUTO_ASSIGNMENT_METHOD,
                    Event.SUCCESSFUL_END_OF_AUTOMATIC_ASSIGNMENT,
                    "$params;$paramsOfAllSizes"
                )
            }
        }
    }

    private fun generateEventLogParams(
        year: Int?,
        nodeId: Long?,
        materialIds: List<Long>?
    ): String {
        val nodeName = nodeId?.let { nodeService.getTranslatedName(it) }
        val materialName =
            if (materialIds.isNullOrEmpty())
                dictionaryService.findTranslationByKeyAndLanguage("all2")
            else materialIds[0].let { materialService.getTranslatedName(it) }

        return "$year;$nodeName;$materialName"
    }

    fun getAutomaticAssignmentLogic(
        nodeIdsParam: List<Long>,
        year: Int,
        materialIds: List<Long>,
        runByUser: Boolean
    ): String {
        var param: Param? = null
        var autoAnalysesStarted = false
        var params = ""
        val nodeIds: List<Long>
        return try {
            if (!runByUser) {
                param = paramService.getParamByCode(Param.IS_AUTOMATIC_ASSIGNMENT_RUNNING)

                if (param.value != Param.NOT_RUNNING) {
                    throw ForbiddenException(description = "automaticAssignmentIsAlreadyRunning")
                }

                param.value = Param.RUNNING
                paramService.updateParam(param)
                val lastExecParam =
                    paramService.getParamByCode(Param.LAST_AUTOMATIC_ASSIGNMENT_JOB_EXEC)
                lastExecParam.dateValue = LocalDate.now()
                paramService.updateParam(lastExecParam)
                autoAnalysesStarted = true
            } else {
                param = paramService.getParamByCode(Param.IS_AUTOMATIC_ASSIGNMENT_RUNNING)
                autoAnalysesStarted = true
            }

            if (!runByUser) {
                nodeIds = nodeService.getAllNodesByYear(year).mapNotNull { it?.id }

                if (nodeIdsParam.size == nodeIds.size)
                    params = "$year;all2;" // (all2 je prekladane)
                else {
                    val sameNodeIds = nodeIds.filter { nodeIdsParam.contains(it) }.toMutableList()
                    params += sameNodeIds.firstOrNull()?.let { nodeService.getTranslatedName(it) }
                    if (sameNodeIds.size > 1) {
                        params +=
                            sameNodeIds.subList(0, 1).map {
                                ", " + nodeService.getTranslatedName(it)
                            }
                    }
                }

                params +=
                    if (materialIds.size != 1) {
                        ";all2" // all2 je prekladane
                    } else {
                        ";${materialService.getTranslatedName(materialIds[0])}"
                    }
                eventService.logEvent(
                    Event.RUN_AUTO_ASSIGNMENT_METHOD,
                    Event.START_OF_AUTOMATIC_ASSIGNMENT,
                    "$params;${principal()?.loginAd ?: "admin"}"
                )
            } else {
                nodeIds = nodeIdsParam
            }

            val params2 = this.validateQuantityAnalysis(nodeIds, year, materialIds)

            if (!runByUser) {
                eventService.logEvent(
                    Event.RUN_AUTO_ASSIGNMENT_METHOD,
                    Event.SUCCESSFUL_END_OF_AUTOMATIC_ASSIGNMENT,
                    "$params;$params2"
                )
            }
            params2
        } catch (e: Exception) {
            logger().error(e.message)
            if (autoAnalysesStarted && !runByUser) {
                eventService.logEvent(
                    Event.RUN_AUTO_ASSIGNMENT_METHOD,
                    params,
                    Event.UNSUCCESSFUL_END_OF_AUTOMATIC_ASSIGNMENT
                )
            }

            throw ForbiddenException("Unsuccessful end of AA, check logs")
        } finally {
            if (autoAnalysesStarted && !runByUser) {
                try {
                    if (param != null) {
                        param.value = Param.NOT_RUNNING
                        paramService.updateParam(param)
                    }
                } catch (e: ForbiddenException) {
                    throw ForbiddenException(description = "Can't save param")
                }
            }
        }
    }

    private fun validateQuantityAnalysis(
        nodeIds: List<Long>,
        year: Int,
        materialIds: List<Long>
    ): String {
        val closeDateUSS = paramService.getParamByCode("param.closeDateUSS").dateValue
        quantityList =
            quantityService
                .getAllEligibleQuantitiesForAutoAssignment(year, nodeIds, materialIds, closeDateUSS)
                .toMutableList()

        val materialNodeIds = quantityList.mapNotNull { it.materialNode?.id }
        val remoteMaterialCodeIds = quantityList.mapNotNull { it.remoteMaterialCode?.id }

        analyses =
            analysisService
                .getAllEligibleAnalysisForAutomaticAssignment(
                    year,
                    materialNodeIds,
                    remoteMaterialCodeIds
                )
                .toMutableList()

        val insertQuantityAnalyses: MutableList<QuantityAnalysis> = mutableListOf()
        val updateQuantityAnalyses: MutableList<QuantityAnalysisUpdate> = mutableListOf()
        val deleteQuantityAnalyses: MutableList<QuantityAnalysis> = mutableListOf()

        var currentMaterialNode: MaterialNode? = null
        for (quantityFromList in quantityList) {
            val materialNode =
                quantityFromList.materialNode?.id?.let { materialNodeService.getById(it) }
            val materialNodeAnalysisParams = materialNode?.materialNodeAnalysisParams
            val quantityAnalyses =
                quantityFromList.id?.let {
                    quantityService.getQuantityAnalysesBasedOnQuantityId(it)
                }

            if (currentMaterialNode == null) currentMaterialNode = materialNode
            if (currentMaterialNode?.id == materialNode?.id) {
                currentMaterialNode = materialNode
            }

            if (quantityAnalyses != null && materialNodeAnalysisParams != null) {
                for (materialNodeAnalysisParam in materialNodeAnalysisParams) {
                    var foundQuantityAnalysis: QuantityAnalysis? = null
                    val correctAnalysis: Analysis? =
                        materialNodeAnalysisParam.analysisParam?.let {
                            findAnalysis(quantityFromList, it)
                        }
                    val currentAnalysisParam = materialNodeAnalysisParam.analysisParam
                    for (quantityAnalysis in quantityAnalyses) {
                        val analysisCalculated = quantityAnalysis.analysis?.calculated
                        if ((currentAnalysisParam?.id == quantityAnalysis.analysisParam?.id) &&
                                (analysisCalculated == false)
                        ) {
                            foundQuantityAnalysis = quantityAnalysis
                            break
                        }
                    }
                    if (foundQuantityAnalysis == null && correctAnalysis == null) continue

                    if (foundQuantityAnalysis == null &&
                            correctAnalysis != null &&
                            correctAnalysis.calculated == false
                    ) {
                        insertQuantityAnalyses.add(
                            QuantityAnalysis().apply {
                                quantity = quantityFromList
                                analysis = correctAnalysis
                                analysisParam = currentAnalysisParam
                            }
                        )
                    }
                    if (foundQuantityAnalysis != null &&
                            correctAnalysis != null &&
                            correctAnalysis.calculated == false
                    ) {
                        if (foundQuantityAnalysis.analysis?.id != correctAnalysis.id) {
                            updateQuantityAnalyses.add(
                                QuantityAnalysisUpdate().apply {
                                    analysisParam = currentAnalysisParam
                                    analysis = correctAnalysis
                                    quantityAnalysis = foundQuantityAnalysis
                                }
                            )
                        }
                    }
                    if (foundQuantityAnalysis != null && correctAnalysis == null) {
                        deleteQuantityAnalyses.add(foundQuantityAnalysis)
                    }
                }
            }
        }

        quantityService.syncWithDb(
            insertQuantityAnalyses,
            updateQuantityAnalyses,
            deleteQuantityAnalyses
        )

        quantityList.clear()
        analyses.clear()

        return "${insertQuantityAnalyses.size};${updateQuantityAnalyses.size};${deleteQuantityAnalyses.size}"
    }

    private fun findAnalysis(quantity: Quantity, analysisParam: AnalysisParam): Analysis? {
        return analyses.firstOrNull {
            quantity.materialNode?.id == it.materialNode?.id &&
                (analysisParam.id == it.analysisParam?.id && quantity.remoteMaterialCode == null ||
                    quantity.remoteMaterialCode?.id != null &&
                        it.materialConversion?.id != null &&
                        quantity.remoteMaterialCode?.id == it.materialConversion?.id) &&
                (quantity.dateFrom == it.validFrom ||
                    quantity.dateFrom?.isAfter(it.validFrom) == true)
        }
    }
}
