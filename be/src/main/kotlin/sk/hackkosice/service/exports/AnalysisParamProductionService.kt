package sk.esten.uss.gbco2.service.exports

import java.math.BigDecimal
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.config.enums.EnvEnum
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.exports.MaterialIntensityReportFilter
import sk.esten.uss.gbco2.dto.response.ErrorCode
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisParamDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeDto
import sk.esten.uss.gbco2.exceptions.ValidationException
import sk.esten.uss.gbco2.generics.service.ReportServiceView
import sk.esten.uss.gbco2.mapper.NodeMapper
import sk.esten.uss.gbco2.model.repository.material.AdvMaterialVRepository
import sk.esten.uss.gbco2.model.repository.results_month.AdvResultsMonthRepository
import sk.esten.uss.gbco2.service.*
import sk.esten.uss.gbco2.service.exports.data.MaterialQuantityReportData
import sk.esten.uss.gbco2.service.exports.data.MaterialQuantityReportEnum
import sk.esten.uss.gbco2.service.exports.data.SheetData
import sk.esten.uss.gbco2.service.exports.data.SummationResult
import sk.esten.uss.gbco2.utils.*

@Service
class AnalysisParamProductionService(
    override val dictionaryUiService: DictionaryUiService,
    override val fopService: FOPService,
    private val paramService: ParamService,
    private val nodeDataLockService: NodeDataLockService,
    private val analysisParamService: AnalysisParamService,
    private val scopeService: ScopeService,
    private val nodeService: NodeService,
    private val unitService: UnitService,
    private val nodeMapper: NodeMapper,
    private val materialVRepository: AdvMaterialVRepository,
    private val resultsMonthRepository: AdvResultsMonthRepository,
    @Value("\${uss.gbco2.environment:}") val env: EnvEnum,
    @Value("\${application.version:unknown}") val version: String,
    @Value("\${application.build-time:unknown}") val buildTime: String
) :
    ReportServiceView<
        MaterialQuantityReportData,
        String,
        MaterialIntensityReportFilter,
        SheetData<MaterialQuantityReportData>>() {

    private var selectedAnalysisParam: List<SimpleAnalysisParamDto> = listOf()
    private var prodUnitAbbr: String? = null

    override fun getUIKeysForTranslations(): Map<String?, String?> {
        val toTranslate = ExportConstants.analysisParamProdExcelTranslations
        toTranslate.add(env.key)
        toTranslate.addAll(getMonthTranslationKeys())
        return dictionaryUiService.getAllTranslatedLabels(toTranslate)
    }

    override fun getDataForReport(
        filter: MaterialIntensityReportFilter?,
        translations: Map<String?, String?>,
        dataGroupNames: List<String>?
    ): MutableList<SheetData<MaterialQuantityReportData>> {

        filter?.validate() ?: throw IllegalArgumentException("Filter is null!")

        val parsedSelectedAPIds =
            filter.allSelectedAPs().ifEmpty {
                throw IllegalArgumentException("Select at least one analysis param!")
            }

        selectedAnalysisParam =
            analysisParamService
                .getAllSimpleByIds(parsedSelectedAPIds)
                .sortBySelectionItemsOrder(parsedSelectedAPIds)

        // get subNodes of plants and superPlants
        val superPlantWithSubNodeIds =
            filter.superPlantIds?.let { nodeService.getAllSubNodeIds(it, filter.year) }
        val plantWithSubNodeIds =
            filter.plantIds?.let { nodeService.getAllSubNodeIds(it, filter.year) }

        val allNodeWithSubNodes = mutableListOf<Long>()
        superPlantWithSubNodeIds?.let { allNodeWithSubNodes.addAll(it) }
        plantWithSubNodeIds?.let { allNodeWithSubNodes.addAll(it) }

        val years = filter.year.rangeOfYears(4)

        val inputMaterials =
            materialVRepository.getAllByIOAndNodesAndAP(true, filter.scopeId, allNodeWithSubNodes)
        val outputMaterials =
            materialVRepository.getAllByIOAndNodesAndAP(false, filter.scopeId, allNodeWithSubNodes)

        val materialsId = (inputMaterials.keys + outputMaterials.keys).toList()
        val data: MutableList<SheetData<MaterialQuantityReportData>> = mutableListOf()

        val resultsViewsPerMonth =
            resultsMonthRepository.getByNodesAndMaterialPerMonth(
                filter,
                superPlantWithSubNodeIds,
                plantWithSubNodeIds,
                materialsId,
                parsedSelectedAPIds
            )

        val resultsViewsPerYear =
            resultsMonthRepository.getByNodesAndMaterialPerYear(
                filter,
                superPlantWithSubNodeIds,
                plantWithSubNodeIds,
                materialsId,
                parsedSelectedAPIds
            )

        val inputDataOverall =
            inputMaterials
                .map {
                    it.key to
                        MaterialQuantityReportData(
                            SimpleMaterialDto().apply {
                                id = it.key
                                name = it.value
                            },
                            sectionType = MaterialQuantityReportEnum.INPUT_SECTION
                        )
                }
                .toMap()
        val outputDataOverall =
            outputMaterials
                .map {
                    it.key to
                        MaterialQuantityReportData(
                            SimpleMaterialDto().apply {
                                id = it.key
                                name = it.value
                            },
                            sectionType = MaterialQuantityReportEnum.OUTPUT_SECTION,
                            scale = BigDecimal(-1)
                        )
                }
                .toMap()

        // object for computing I/O summations
        val inputDataOverallSum =
            MaterialQuantityReportData(
                SimpleMaterialDto().apply { name = translations["totalInput"] },
                sectionType = MaterialQuantityReportEnum.TOTAL_IN_SECTION
            )
        val outputDataOverallSum =
            MaterialQuantityReportData(
                SimpleMaterialDto().apply { name = translations["totalOutput"] },
                sectionType = MaterialQuantityReportEnum.TOTAL_OUT_SECTION,
                scale = BigDecimal(-1)
            )

        // select all nodes for generate sheets
        val nodeFilter =
            ReadAllParamsFilterDto().apply {
                sortColumn = "nameTranslated"
                direction = Sort.Direction.ASC
            }
        val nodes =
            nodeService.getAllNodesByNodeIds(filter.allNodes(), nodeFilter.toSort()).map {
                nodeMapper.mapSimple(it)
            }

        // parse computations for materials and nodes
        nodes.forEach {
            val inputSumForNode =
                MaterialQuantityReportData(
                    SimpleMaterialDto().apply { name = translations["totalInput"] },
                    sectionType = MaterialQuantityReportEnum.TOTAL_IN_SECTION
                )
            val outputSumForNode =
                MaterialQuantityReportData(
                    SimpleMaterialDto().apply { name = translations["totalOutput"] },
                    sectionType = MaterialQuantityReportEnum.TOTAL_OUT_SECTION,
                    scale = BigDecimal(-1)
                )

            val inputDataForNode =
                assignAnalyticalFactorToMaterials(
                    inputMaterials,
                    resultsViewsPerMonth,
                    resultsViewsPerYear,
                    years,
                    it.id,
                    MaterialQuantityReportEnum.INPUT_SECTION,
                    inputSumForNode,
                    inputDataOverall,
                    inputDataOverallSum,
                )
            val outputDataForNode =
                assignAnalyticalFactorToMaterials(
                    outputMaterials,
                    resultsViewsPerMonth,
                    resultsViewsPerYear,
                    years,
                    it.id,
                    MaterialQuantityReportEnum.OUTPUT_SECTION,
                    outputSumForNode,
                    outputDataOverall,
                    outputDataOverallSum,
                )

            // total summation of input and output
            val totalIOSummationForNode =
                getTotalSumOfInputOutputResults(
                    "${translations["totalInput"]} - ${translations["totalOutput"]}",
                    inputSumForNode,
                    outputSumForNode
                )

            data.add(
                SheetData(
                    mutableMapOf(
                        (dataGroupNames?.get(0) ?: "section1") to inputDataForNode,
                        (dataGroupNames?.get(1) ?: "section2") to mutableListOf(inputSumForNode),
                        (dataGroupNames?.get(2) ?: "section3") to outputDataForNode,
                        (dataGroupNames?.get(3) ?: "section4") to mutableListOf(outputSumForNode),
                        (dataGroupNames?.get(4) ?: "section5") to totalIOSummationForNode
                    ),
                    it
                )
            )
        }

        var totalSummations: SheetData<MaterialQuantityReportData>? = null
        if (filter.allNodes().size > 1) {
            // total summation of input and output
            val totalIODataOverallSummation =
                getTotalSumOfInputOutputResults(
                    "${translations["totalInput"]} - ${translations["totalOutput"]}",
                    inputDataOverallSum,
                    outputDataOverallSum
                )

            // compute total summations for input - output
            totalSummations =
                SheetData(
                    mutableMapOf(
                        (dataGroupNames?.get(0) ?: "section1") to inputDataOverall.map { it.value },
                        (dataGroupNames?.get(1)
                            ?: "section2") to mutableListOf(inputDataOverallSum),
                        (dataGroupNames?.get(2)
                            ?: "section3") to outputDataOverall.map { it.value },
                        (dataGroupNames?.get(3)
                            ?: "section4") to mutableListOf(outputDataOverallSum),
                        (dataGroupNames?.get(4) ?: "section5") to totalIODataOverallSummation
                    )
                )
        }

        return (listOfNotNull(totalSummations) + data).toMutableList()
    }

    private fun assignAnalyticalFactorToMaterials(
        materials: Map<Long, String>,
        resultMonths: Map<String, SummationResult>,
        resultMonthsPerYear: Map<String, SummationResult>,
        years: List<Int>,
        nodeId: Long? = null,
        section: MaterialQuantityReportEnum,
        sectionSumData: MaterialQuantityReportData,
        sectionOverallData: Map<Long, MaterialQuantityReportData>,
        sectionOverallSumData: MaterialQuantityReportData
    ): MutableList<MaterialQuantityReportData> {
        val inputMaterialsQuantityReportData = mutableListOf<MaterialQuantityReportData>()
        materials.forEach {
            val overallReport = sectionOverallData[it.key]
            val report =
                MaterialQuantityReportData(
                    SimpleMaterialDto().apply {
                        id = it.key
                        name = it.value
                    },
                    sectionType = section,
                    scale =
                        if (section == MaterialQuantityReportEnum.OUTPUT_SECTION) BigDecimal(-1)
                        else BigDecimal(1)
                )

            // append analytical factor sum for years overview
            for (i in 0..2) {
                val monthResultsPerYear =
                    resultMonthsPerYear[getAssignKey(years[3 - i], nodeId, it.key)]
                report.addValue(i, monthResultsPerYear)
                overallReport?.addValue(i, monthResultsPerYear)
                sectionOverallSumData.addValue(i, monthResultsPerYear)
                sectionSumData.addValue(i, monthResultsPerYear)
                setProdAbbrValue(monthResultsPerYear)
            }

            // append values for months of selected years
            for (i in 3..14) {
                val monthResultsPerMonth =
                    resultMonths[getAssignKeyIncludeMonth(years[0], i - 2, nodeId, it.key)]
                val monthResultsPerMonthOfLastYear =
                    resultMonths[getAssignKeyIncludeMonth(years[1], i - 2, nodeId, it.key)]
                report.addValue(i, monthResultsPerMonth, monthResultsPerMonthOfLastYear)
                overallReport?.addValue(i, monthResultsPerMonth, monthResultsPerMonthOfLastYear)
                sectionOverallSumData.addValue(
                    i,
                    monthResultsPerMonth,
                    monthResultsPerMonthOfLastYear
                )
                sectionSumData.addValue(i, monthResultsPerMonth, monthResultsPerMonthOfLastYear)
                setProdAbbrValue(monthResultsPerMonth)
            }
            inputMaterialsQuantityReportData.add(report)
        }
        return inputMaterialsQuantityReportData
    }

    private fun getTotalSumOfInputOutputResults(
        sectionName: String,
        inputSumData: MaterialQuantityReportData,
        outputSumData: MaterialQuantityReportData,
    ): MutableList<MaterialQuantityReportData> {
        val inputMaterialsQuantityReportData = mutableListOf<MaterialQuantityReportData>()

        val report =
            MaterialQuantityReportData(
                SimpleMaterialDto().apply { name = sectionName },
                MaterialQuantityReportEnum.TOTAL_IN_OUT_SECTION
            )

        // append analytical factor sum for years overview
        for (i in 0..14) {
            report.addValue(i, inputSumData.getValue(i), inputSumData.getValueOfLastYear(i))
            report.addValue(i, outputSumData.getValue(i), outputSumData.getValueOfLastYear(i))
        }

        inputMaterialsQuantityReportData.add(report)

        return inputMaterialsQuantityReportData
    }

    private fun getAssignKeyIncludeMonth(
        year: Int,
        index: Int?,
        nodeId: Long? = null,
        materialId: Long?
    ): String {
        return if (nodeId == null) "(${year})-(${index})-(${materialId})"
        else "(${year})-(${index})-(${nodeId})-(${materialId})"
    }

    private fun getAssignKey(year: Int, nodeId: Long? = null, materialId: Long?): String {
        return if (nodeId == null) "(${year})-(${materialId})"
        else "(${year})-(${nodeId})-(${materialId})"
    }

    private fun setProdAbbrValue(summationResult: SummationResult?) {
        if (summationResult?.unitAbbr != null && prodUnitAbbr == null) {
            val unit = unitService.getUnitByAbbr(summationResult.unitAbbr)
            prodUnitAbbr = unit?.name
        }
    }

    override fun generateGroupHeadersMap(
        filter: MaterialIntensityReportFilter,
        translations: Map<String?, String?>,
        map: HashMap<String, Any?>,
        params: Any?
    ) {
        val node = params as SimpleNodeDto?
        super.generateGroupHeadersMap(filter, translations, map, params)
        val selectedScope = filter.scopeId?.let { scopeService.getView(it) }
        val firstSelectedAP =
            if (selectedAnalysisParam.isNotEmpty()) selectedAnalysisParam[0]
            else
                throw ValidationException(
                    ErrorCode.DATA_VALIDATION,
                    translations["pleaseSelectComponent"] ?: "???pleaseSelectComponent???"
                )

        var param = translations["total"]
        if (node != null) {
            param = node.name
        }
        map["sheetName"] = param

        map["plantCarbonHeader1"] = translations["ussSteelCorporation"]
        map["plantCarbonHeader"] =
            String.format(
                translations["plantCarbonHeader"] ?: "???plantCarbonHeader???",
                param,
                selectedScope?.nameTranslated,
                selectedAnalysisParam.getHeaderOfValues(translations)
            )
        map["energy"] = translations["energy"]
        map["headerYearYTD"] = "${filter.year} ${translations["headerYtd"]}"
        map["headerYear1YTD"] = "${filter.year?.minus(1)} ${translations["headerYtd"]}"
        map["headerYear3"] = filter.year?.minus(3)?.let { BigDecimal(it) }
        map["headerYear2"] = filter.year?.minus(2)?.let { BigDecimal(it) }
        map["headerYear1"] = filter.year?.minus(1)?.let { BigDecimal(it) }
        map["headerYear"] = filter.year?.let { BigDecimal(it) }
        map["carbonSource"] =
            String.format(
                translations["carbonSource"] ?: "???carbonSource???",
                firstSelectedAP.name
            )
        map["units"] = translations["units2"]
        map["inputs"] = translations["Inputs"]
        map["outputs"] = translations["outputs"]
        map["inputsMinusOutputs"] = translations["inputMinusOutputCarbonReport"]
        map["changeLabel"] = translations["changeLabel"]
        map["ytd"] = translations["ytd"]

        map.appendFormattedEnvironmentData(translations, env, version, buildTime)

        map.appendFormattedParamsData(translations, paramService)

        prodUnitAbbr?.let { map["unit"] = prodUnitAbbr }

        if (node == null) {
            map["text3"] = translations["note"]
            map["param3"] = translations["paramNoteCarbonReport"]
            map["text4"] = translations["YTDCalculationThrough"]
            map["param4"] = translations["month.${filter.rpd}"]
        } else {
            map["text3"] = translations["YTDCalculationThrough"]
            map["param3"] = translations["month.${filter.rpd}"]
            map["text4"] = translations["plantCheckDate"]
            map["text5"] = translations["plantLockDate"]
            val dataLock = node.id?.let { nodeDataLockService.getDataLockByNode(it) }
            dataLock?.let {
                map["param4"] = dataLock.checkDate?.toLocalDate().formatDateToMonthWithUserLocale()
                map["param5"] = dataLock.lockDate?.toLocalDate().formatDateToMonthWithUserLocale()
            }
        }
    }
}
