package sk.esten.uss.gbco2.service.exports

import java.math.BigDecimal
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.config.enums.EnvEnum
import sk.esten.uss.gbco2.dto.request.filter.exports.MaterialIntensityReportFilter
import sk.esten.uss.gbco2.dto.response.ErrorCode
import sk.esten.uss.gbco2.dto.response.NodeTypeDto
import sk.esten.uss.gbco2.dto.response.simple.*
import sk.esten.uss.gbco2.exceptions.NotFoundException
import sk.esten.uss.gbco2.exceptions.ValidationException
import sk.esten.uss.gbco2.generics.service.ReportServiceView
import sk.esten.uss.gbco2.model.entity.analysis_param.AnalysisParamSuper
import sk.esten.uss.gbco2.model.entity.material.MaterialSuper
import sk.esten.uss.gbco2.model.entity.node_type.NodeTypeSuper
import sk.esten.uss.gbco2.model.entity.param.Param
import sk.esten.uss.gbco2.model.entity.scope.ScopeSuper
import sk.esten.uss.gbco2.model.repository.quantity_month.AdvQuantityMonthRepository
import sk.esten.uss.gbco2.model.repository.results_month.AdvResultsMonthRepository
import sk.esten.uss.gbco2.service.*
import sk.esten.uss.gbco2.service.exports.data.*
import sk.esten.uss.gbco2.utils.*

@Service
class MaterialIntensityReportService(
    override val dictionaryUiService: DictionaryUiService,
    override val fopService: FOPService,
    private val paramService: ParamService,
    private val materialService: MaterialService,
    private val nodeService: NodeService,
    private val nodeTypeService: NodeTypeService,
    private val regionService: RegionService,
    private val scopeService: ScopeService,
    private val analysisParamService: AnalysisParamService,
    private val unitService: UnitService,
    private val unitSetSettingsAP: UnitSetSettingsApService,
    private val quantityMonthRepository: AdvQuantityMonthRepository,
    private val resultsMonthRepository: AdvResultsMonthRepository,
    @Value("\${uss.gbco2.environment:}") val env: EnvEnum,
    @Value("\${application.version:unknown}") val version: String,
    @Value("\${application.build-time:unknown}") val buildTime: String
) :
    ReportServiceView<
        MaterialIntensityReportSection,
        String,
        MaterialIntensityReportFilter,
        SheetData<MaterialIntensityReportSection>>() {

    var reportType: IntensityReportTypeEnum = IntensityReportTypeEnum.C_AP
    private var selectedAnalysisParam: List<SimpleAnalysisParamDto> = listOf()

    private var prodUnitAbbr: String? = null

    private var analysisParamUnitAbbr: String? = null
    private var combinedEnergyExportIntegratedFacilitiesMaterial: SimpleMaterialDto? = null

    fun setReportType(reportType: IntensityReportTypeEnum): MaterialIntensityReportService {
        this.reportType = reportType
        return this
    }

    override fun getUIKeysForTranslations(): Map<String?, String?> {
        val toTranslate = ExportConstants.materialQuantityExcelTranslations
        toTranslate.add(env.key)
        toTranslate.addAll(getMonthTranslationKeys())
        toTranslate.addAll(ExportConstants.exceptionMessagesTranslations)
        if (this.reportType == IntensityReportTypeEnum.C_AP) {
            toTranslate.add("corpResultsReportHeader2EndingScope1")
            toTranslate.add("corpResultsReportHeader2EndingScope2")
            toTranslate.add("corpResultsReportHeader2")
        } else if (this.reportType == IntensityReportTypeEnum.C_ENERGY_COMBINED) {
            toTranslate.add("corpResultsReportHeader2energyCombined")
        }
        return dictionaryUiService.getAllTranslatedLabels(toTranslate)
    }

    override fun getDataForReport(
        filter: MaterialIntensityReportFilter?,
        translations: Map<String?, String?>,
        dataGroupNames: List<String>?
    ): MutableList<SheetData<MaterialIntensityReportSection>> {

        filter?.validate() ?: throw IllegalArgumentException("Filter is null!")

        val parsedSelectedAPIds =
            filter.allSelectedAPs().ifEmpty {
                throw IllegalArgumentException("Select at least one analysis param!")
            }

        setAnalysisParamUnitAbbr(parsedSelectedAPIds[0])
        selectedAnalysisParam =
            analysisParamService
                .getAllSimpleByIds(parsedSelectedAPIds)
                .sortBySelectionItemsOrder(parsedSelectedAPIds)
        val analysisParamIds = selectedAnalysisParam.mapNotNull { it.id }

        val materialCodes =
            paramService
                .getParamByCode(Param.INTENSITIES_REPORT_SUMMARIES_PRODUCTS)
                .value
                ?.split(";")
                ?: mutableListOf()

        // get all materials by GBC_PARAMS codes - necessary to generate summary section of report
        val materials = materialService.getAllMaterialByCodes(materialCodes)
        materials.validateMaterialsProducts(
            materialCodes,
            Param.INTENSITIES_REPORT_SUMMARIES_PRODUCTS
        )

        // get all material ids to mutableList for later addiction
        val materialIds = materials.mapNotNull { it.id }.toMutableList()

        // get all report grouping critters
        val nodeTypes = nodeTypeService.getAllWithMaterialReport(filter.allNodes())
        val regions: Map<Long, String?> =
            regionService
                .getAllWithNode()
                .filter { it.id != null }
                .associateBy({ it.id!! }, { it.name })
        val nodes = nodeService.getNodesByNodeTypes(filter.allNodes())

        val superPlantsWithSubNodesIds =
            filter.superPlantIds?.let { nodeService.getAllSubNodeIds(it, filter.year) }
        val plantsWithSubNodesIds =
            filter.plantIds?.let { nodeService.getAllSubNodeIds(it, filter.year) }

        if (reportType == IntensityReportTypeEnum.C_ENERGY_COMBINED) {
            val combinedEnergyReportMaterials =
                materialService.getAllMaterialByCodes(listOfNotNull(MaterialSuper.HOT_BAND_CODE))
            if (combinedEnergyReportMaterials.isNotEmpty()) {
                combinedEnergyExportIntegratedFacilitiesMaterial = combinedEnergyReportMaterials[0]
                combinedEnergyExportIntegratedFacilitiesMaterial?.id?.let {
                    materialIds.appendValueIfNotExists(it)
                }
            } else {
                throw NotFoundException(
                    String.format(
                        translations["cannotFindDenominatorForIntegratedFacilities"]
                            ?: "???cannotFindDenominatorForIntegratedFacilities???",
                        MaterialSuper.HOT_BAND_CODE
                    )
                )
            }
        }

        // append materials included in nodeTypes and not included in products
        nodeTypes.mapNotNull { it.materialReport?.id }.forEach {
            materialIds.appendValueIfNotExists(it)
        }

        // material quantity
        val quantities =
            quantityMonthRepository.getForIntensity(
                filter,
                superPlantsWithSubNodesIds,
                plantsWithSubNodesIds,
                materialIds
            )

        // set un itAbbr from selected quantities
        setProdAbbrValue(quantities)

        val monthResults =
            resultsMonthRepository.getForIntensity(
                filter,
                superPlantsWithSubNodesIds,
                plantsWithSubNodesIds,
                analysisParamIds
            )

        // create sections
        val nodeTypeSections =
            nodeTypes.map { nodeType ->
                MaterialIntensityReportSection(
                    getSectionHeader(translations, nodeType),
                    computeSpecialSection(filter, quantities, monthResults, nodes, nodeType),
                )
            }

        val regionSections =
            materials.map { material ->
                MaterialIntensityReportSection(
                    getSectionHeaderSummary(translations, material),
                    computeSummarySection(
                        filter,
                        quantities,
                        monthResults,
                        nodes,
                        regions,
                        material.id!!,
                        translations["totalUSSteel"] ?: "???totalUSSteel???"
                    )
                )
            }

        return mutableListOf(
            SheetData(
                mutableMapOf(
                    "section" to
                        nodeTypeSections.filter { it.hasToBeRendered() } +
                            regionSections.filter { it.hasToBeRendered() }
                )
            )
        )
    }

    private fun computeSpecialSection(
        filter: MaterialIntensityReportFilter,
        quantities: Map<String, SummationResult>,
        results: Map<String, SummationResult>,
        nodes: Map<String, String>,
        nodeType: NodeTypeDto
    ): MutableList<MaterialIntensityReportData> {

        // create all structures for calculations
        val nodesMap = mutableMapOf<Long, MaterialIntensityReportData>()
        nodes.filter { (k, _) -> k.parseKeyId(1) == nodeType.id }.forEach { (k, v) ->
            k.parseKeyId(0)?.let { nodesMap[it] = MaterialIntensityReportData(v) }
        }

        val denominatorId = nodeType.materialReport?.id
        // compute calculation quantities for all selected scopes
        quantities.filter { (k, _) -> (k.parseKeyId(3) == denominatorId) }.forEach { (k, v) ->
            // parsed identifiers
            val year = k.parseKeyId(0)
            val month = k.parseKeyId(1)
            val nodeId = k.parseKeyId(2)
            val hasToBeComputed = nodesMap[nodeId]
            hasToBeComputed?.let { computeValues(it, filter, v, month, year, isQuantity = true) }
        }

        // compute all selected month results for selected scope
        results.forEach { (k, v) ->
            // parsed identifiers
            val year = k.parseKeyId(0)
            val month = k.parseKeyId(1)
            val nodeId = k.parseKeyId(2)
            val hasToBeComputed = nodesMap[nodeId]
            hasToBeComputed?.let { computeValues(it, filter, v, month, year, isQuantity = false) }
        }

        return nodesMap.values.toMutableList()
    }

    private fun computeSummarySection(
        filter: MaterialIntensityReportFilter,
        quantities: Map<String, SummationResult>,
        results: Map<String, SummationResult>,
        nodes: Map<String, String>,
        regions: Map<Long, String?>,
        denominatorId: Long,
        sectionsName: String?
    ): MutableList<MaterialIntensityReportData> {

        // create all structures for calculations
        val nodesMap = mutableMapOf<Long, String>()
        val regionsMap = mutableMapOf<Long, MaterialIntensityReportData>()
        regions.forEach { (k, v) -> regionsMap[k] = MaterialIntensityReportData(v) }
        nodes.forEach { (k, v) -> k.parseKeyId(0)?.let { nodesMap[it] = v } }

        val lineTotal = MaterialIntensityReportData(sectionsName, isEmpty = false)

        quantities.filter { (k, _) -> (k.parseKeyId(3) == denominatorId) }.forEach { (k, v) ->
            // parsed identifiers
            val year = k.parseKeyId(0)
            val month = k.parseKeyId(1)
            val nodeId = k.parseKeyId(2)
            val regionId = k.parseKeyId(4)
            val lines = listOf(regionsMap[regionId], lineTotal)
            val hasToBeComputed = nodesMap[nodeId]
            hasToBeComputed?.let {
                lines.forEach { report ->
                    report?.let { r -> computeValues(r, filter, v, month, year, isQuantity = true) }
                }
            }
        }

        results.forEach { (k, v) ->
            // parsed identifiers
            val year = k.parseKeyId(0)
            val month = k.parseKeyId(1)
            val nodeId = k.parseKeyId(2)
            val regionId = k.parseKeyId(3)
            val lines = listOf(regionsMap[regionId], lineTotal)
            val hasToBeComputed = nodesMap[nodeId]
            hasToBeComputed?.let {
                lines.forEach { report ->
                    report?.let { r ->
                        computeValues(r, filter, v, month, year, isQuantity = false)
                    }
                }
            }
        }

        return (regionsMap.values + lineTotal).toMutableList()
    }

    private fun computeValues(
        computedSection: MaterialIntensityReportData?,
        filter: MaterialIntensityReportFilter,
        result: SummationResult,
        month: Long?,
        year: Long?,
        isQuantity: Boolean
    ) {

        filter.prevYears().forEachIndexed { i, y ->
            if (year?.toInt() == y) {
                month?.let {
                    if (isQuantity) {
                        computedSection?.addQuantityValue(i, result.value)
                    } else {
                        computedSection?.addAnalyticalValue(i, result.value)
                    }
                }
            }
        }
        when (year?.toInt()) {
            filter.year?.minus(1) -> {
                month?.let {
                    if (it <= filter.rpd!!) {
                        if (isQuantity) {
                            computedSection?.addQuantityValue(month.toInt() + 2, null, result.value)
                        } else {
                            computedSection?.addAnalyticalValue(
                                month.toInt() + 2,
                                null,
                                result.value
                            )
                        }
                    }
                }
            }
            filter.year -> {
                month?.let {
                    if (it <= filter.rpd!!) {
                        if (isQuantity) {
                            computedSection?.addQuantityValue(month.toInt() + 2, result.value)
                        } else {
                            computedSection?.addAnalyticalValue(month.toInt() + 2, result.value)
                        }
                    }
                }
            }
        }
    }

    override fun generateGroupHeadersMap(
        filter: MaterialIntensityReportFilter,
        translations: Map<String?, String?>,
        map: HashMap<String, Any?>,
        params: Any?
    ) {
        super.generateGroupHeadersMap(filter, translations, map, params)
        map["corpResultsReportHeader1"] =
            translations["ussSteelCorporation"]?.uppercase() ?: "???corpResultsReportHeader1???"
        val selectedScope = filter.scopeId?.let { scopeService.getView(it) }
        val firstSelectedAP =
            if (selectedAnalysisParam.isNotEmpty()) selectedAnalysisParam[0]
            else
                throw ValidationException(
                    ErrorCode.DATA_VALIDATION,
                    translations["pleaseSelectComponent"] ?: "???pleaseSelectAP???"
                )

        // defines header params for second headline
        val headerParams =
            listOf(
                filter.year?.minus(3).toString(),
                filter.year.toString(),
                "${selectedScope?.nameTranslated} ${selectedAnalysisParam.getHeaderOfValues(translations)}"
            )

        // defines second headline for report
        if (this.reportType == IntensityReportTypeEnum.C_AP) {
            var ending = ""
            if (firstSelectedAP.code == AnalysisParamSuper.CO2_CODE) {
                when (selectedScope?.id) {
                    ScopeSuper.ID_SCOPE1_CO2 ->
                        ending += translations["corpResultsReportHeader2EndingScope1"]?.uppercase()
                    ScopeSuper.ID_SCOPE2_CO2 ->
                        ending += translations["corpResultsReportHeader2EndingScope2"]?.uppercase()
                }
            }
            map["corpResultsReportHeader2"] =
                translations["corpResultsReportHeader2"]?.let {
                    "${
                        String.format(it, headerParams[0], headerParams[1], headerParams[2]).uppercase()
                    } $ending"
                }
        } else if (this.reportType == IntensityReportTypeEnum.C_ENERGY_COMBINED) {
            map["corpResultsReportHeader2"] =
                translations["corpResultsReportHeader2energyCombined"]?.let {
                    String.format(it, headerParams[0], headerParams[1], headerParams[2]).uppercase()
                }
        }

        map["facility"] = translations["facility"]
        map["ytd"] = translations["ytd"]
        map["headerYear3"] = filter.year?.minus(3)?.let { BigDecimal(it) }
        map["headerYear2"] = filter.year?.minus(2)?.let { BigDecimal(it) }
        map["headerYear1"] = filter.year?.minus(1)?.let { BigDecimal(it) }
        map["headerYear"] = filter.year?.let { BigDecimal(it) }
        map["changeLabel"] = translations["changeLabel"]

        map.appendFormattedEnvironmentData(translations, env, version, buildTime)

        map["corpLockDate"] = translations["corporateLockDate"]
        map["paramCorpLockDate"] =
            paramService
                .getParamByCode(Param.LOCK_DATE_USS)
                .dateValue
                .formatDateToMonthWithUserLocale()
        map["corpCheckDate"] = translations["corporateCheckDate"]
        map["paramCorpCheckDate"] =
            paramService
                .getParamByCode(Param.CLOSE_DATE_USS)
                .dateValue
                .formatDateToMonthWithUserLocale()

        map["note"] = translations["note"]
        map["paramNote"] = translations["paramNoteCarbonReport"]
        map["YTDCalculationThrough"] = translations["YTDCalculationThrough"]
        map["paramYTDCalculationThrough"] = translations["month.${filter.rpd}"]
    }

    private fun setAnalysisParamUnitAbbr(analysisParamId: Long) {
        val unit = unitSetSettingsAP.getAPUnit(analysisParamId)
        analysisParamUnitAbbr = unit?.unit?.abbr.formatForReport()
    }

    private fun setProdAbbrValue(quantities: Map<String, SummationResult>) {
        for (quantity in quantities.values) {
            if (quantity.unitAbbr != null && prodUnitAbbr == null) {
                val unit = unitService.getUnitByAbbr(quantity.unitAbbr)
                prodUnitAbbr = unit?.name
                break
            }
        }
    }

    private fun getSectionHeader(
        translation: Map<String?, String?>,
        nodeType: NodeTypeDto
    ): String {
        var materialHeader = nodeType.materialReport?.name
        if (reportType == IntensityReportTypeEnum.C_ENERGY_COMBINED &&
                nodeType.id == NodeTypeSuper.INTEGRATED_TYPE_ID
        ) {
            materialHeader +=
                " & ${combinedEnergyExportIntegratedFacilitiesMaterial?.name.formatForReport()}"
        }
        return String.format(
                translation["intensityReportSectionHeader"] ?: "???intensityReportSectionHeader???",
                nodeType.name,
                analysisParamUnitAbbr.formatForReport(),
                prodUnitAbbr.formatForReport(),
                materialHeader
            )
            .uppercase()
    }

    private fun getSectionHeaderSummary(
        translation: Map<String?, String?>,
        material: SimpleMaterialDto
    ): String =
        String.format(
                translation["intensityReportSectionHeaderSummary"]
                    ?: "???intensityReportSectionHeaderSummary???",
                analysisParamUnitAbbr,
                prodUnitAbbr.formatForReport(),
                material.name
            )
            .uppercase()
}
