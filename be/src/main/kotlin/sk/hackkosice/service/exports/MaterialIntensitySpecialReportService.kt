package sk.esten.uss.gbco2.service.exports

import java.math.BigDecimal
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.config.enums.EnvEnum
import sk.esten.uss.gbco2.dto.request.filter.exports.MaterialIntensityReportFilter
import sk.esten.uss.gbco2.dto.response.MaterialDto
import sk.esten.uss.gbco2.dto.response.NodeTypeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialDto
import sk.esten.uss.gbco2.generics.service.ReportServiceView
import sk.esten.uss.gbco2.model.entity.country.CountrySuper
import sk.esten.uss.gbco2.model.entity.node_type.NodeTypeSuper
import sk.esten.uss.gbco2.model.entity.param.Param
import sk.esten.uss.gbco2.model.entity.region.RegionSuper
import sk.esten.uss.gbco2.model.repository.quantity_month.AdvQuantityMonthRepository
import sk.esten.uss.gbco2.model.repository.results_month.AdvResultsMonthRepository
import sk.esten.uss.gbco2.service.*
import sk.esten.uss.gbco2.service.exports.data.MaterialIntensityReportData
import sk.esten.uss.gbco2.service.exports.data.MaterialIntensityReportSection
import sk.esten.uss.gbco2.service.exports.data.SheetData
import sk.esten.uss.gbco2.service.exports.data.SummationResult
import sk.esten.uss.gbco2.utils.*

@Service
class MaterialIntensitySpecialReportService(
    override val dictionaryUiService: DictionaryUiService,
    override val fopService: FOPService,
    private val paramService: ParamService,
    private val nodeService: NodeService,
    private val materialService: MaterialService,
    private val nodeTypeService: NodeTypeService,
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

    private var prodUnitAbbr: String? = null

    private var analysisParamUnitAbbr: String? = null

    override fun getUIKeysForTranslations(): Map<String?, String?> {
        val toTranslate = ExportConstants.intensitySpecialExcelTranslations
        toTranslate.add(env.key)
        toTranslate.addAll(getMonthTranslationKeys())
        return dictionaryUiService.getAllTranslatedLabels(toTranslate)
    }

    override fun getDataForReport(
        filter: MaterialIntensityReportFilter?,
        translations: Map<String?, String?>,
        dataGroupNames: List<String>?
    ): MutableList<SheetData<MaterialIntensityReportSection>> {

        filter?.validate() ?: throw IllegalArgumentException("Filter is null!")
        filter.allNodes().ifEmpty {
            throw IllegalArgumentException(translations["pleaseSelectAtleastOnePlant"])
        }
        someNull(filter.scopeId, filter.scopeId2, filter.scopeId3) {
            throw IllegalArgumentException(translations["pleaseSelectAllCalculations"])
        }

        val selectedAnalysisParam =
            filter.analysisParamId
                ?: throw IllegalArgumentException(translations["pleaseSelectComponent"])
        setAnalysisParamUnitAbbr(selectedAnalysisParam)

        val materials = mutableListOf<MaterialDto>()

        val summaryProducts = getMaterialsForSection(Param.INTENSITIES_REPORT_SUMMARIES_PRODUCTS)
        val denominators = getMaterialsForSection(Param.DEFAULT_EIS_DENOM1)
        val denominators2 = getMaterialsForSection(Param.DEFAULT_EIS_DENOM2)
        val denominatorsLim = getMaterialsForSection(Param.DEFAULT_LIM_DENOM)

        // get all report grouping critters
        val nodeTypes = nodeTypeService.getAllWithMaterialReport(filter.allNodes())
        val nodeTypesTranslated = nodeService.getNodesByNodeTypes(filter.allNodes())

        materials.addAll(denominators)
        materials.addAll(denominators2)
        materials.addAll(denominatorsLim)
        materials.addAll(summaryProducts)

        val materialIds = materials.mapNotNull { it.id }.toMutableList()
        val superPlantsWithSubNodesIds =
            filter.superPlantIds?.let { nodeService.getAllSubNodeIds(it, filter.year) }
        val plantsWithSubNodesIds =
            filter.plantIds?.let { nodeService.getAllSubNodeIds(it, filter.year) }

        // append materials included in nodeTypes and not included in products
        nodeTypes.mapNotNull { it.materialReport?.id }.forEach {
            materialIds.appendValueIfNotExists(it)
        }

        // material quantity
        val quantities =
            quantityMonthRepository.getForIntensitySpecial(
                filter,
                superPlantsWithSubNodesIds,
                plantsWithSubNodesIds,
                materialIds
            )

        // set un itAbbr from selected quantities
        setProdAbbrValue(quantities)

        val monthResults =
            resultsMonthRepository.getForIntensitySpecial(
                filter,
                superPlantsWithSubNodesIds,
                plantsWithSubNodesIds,
                listOf(selectedAnalysisParam)
            )

        // validated computation criteria
        val selectedScope =
            filter.scopeId
                ?: throw IllegalArgumentException(translations["pleaseSelectAllCalculations"] + "1")
        val selectedScope2 =
            filter.scopeId2
                ?: throw IllegalArgumentException(translations["pleaseSelectAllCalculations"] + "2")
        val selectedScope3 =
            filter.scopeId3
                ?: throw IllegalArgumentException(translations["pleaseSelectAllCalculations"] + "3")

        val firstSection =
            MaterialIntensityReportSection(
                String.format(
                        translations["intensityReportSectionHeaderSpecial1"] ?: "",
                        analysisParamUnitAbbr,
                        prodUnitAbbr
                    )
                    .uppercase(),
                computeSpecialSection(
                    filter,
                    selectedScope,
                    quantities,
                    monthResults,
                    nodeTypesTranslated,
                    denominators,
                    listOf(
                        translations["totalUSIntegrated"],
                        translations["totalEuropeIntegrated"],
                        translations["totalCanadaIntegrated"],
                        translations["totalIntegratedWoBFs"],
                        translations["totalIntegratedwBFs"]
                    )
                )
            )

        val secondSection =
            MaterialIntensityReportSection(
                String.format(
                        translations["intensityReportSectionHeaderSpecial2"] ?: "",
                        analysisParamUnitAbbr,
                        prodUnitAbbr
                    )
                    .uppercase(),
                computeSpecialSection(
                    filter,
                    selectedScope2,
                    quantities,
                    monthResults,
                    nodeTypesTranslated,
                    denominators2,
                    listOf(
                        translations["totalUSCokeInj"],
                        translations["totalEuropeCokeInj"],
                        translations["totalCanadaCokeInj"],
                        translations["totalBFCokeInj"],
                        translations["totalIntegratedwBFs"]
                    ),
                    selectedScope3
                )
            )

        val specialSections = mutableListOf(firstSection, secondSection)

        // create section by node types
        val sections = mutableListOf<MaterialIntensityReportSection>()
        nodeTypes.forEach { nodeType ->
            if (nodeType.id != NodeTypeSuper.INTEGRATED_TYPE_ID) {
                sections.add(
                    MaterialIntensityReportSection(
                        if (nodeType.id.isLimestoneNodeType())
                            String.format(
                                    translations["intensityReportSectionHeaderSpecialLim"] ?: "",
                                    analysisParamUnitAbbr,
                                    prodUnitAbbr
                                )
                                .uppercase()
                        else getSectionHeader(translations, nodeType),
                        computeSpecialSection(
                            filter,
                            selectedScope3,
                            quantities,
                            monthResults,
                            nodeTypesTranslated,
                            if (nodeType.id.isLimestoneNodeType()) denominatorsLim
                            else listOfNotNull(nodeType.materialReport),
                            listOf(
                                "notNeeded",
                                "notNeeded",
                                "notNeeded",
                                translations["total"] + " " + nodeType.name,
                                "notNeeded"
                            ),
                            nodeTypeId = nodeType.id
                        )
                    )
                )
            }
        }

        val allNodeWithSubNodes = mutableListOf<Long>()
        superPlantsWithSubNodesIds?.let { allNodeWithSubNodes.addAll(it) }
        plantsWithSubNodesIds?.let { allNodeWithSubNodes.addAll(it) }

        // create sections by summary materials
        val summarySections =
            summaryProducts.map { material ->
                MaterialIntensityReportSection(
                    getSectionHeaderSummary(translations, material),
                    safeLet(filter.scopeId3, material.id) { scopeId, materialId ->
                        computeSummarySection(
                            filter,
                            scopeId,
                            quantities,
                            monthResults,
                            nodeTypesTranslated,
                            materialId,
                            listOf(
                                translations["totalNAFlatRollOps"],
                                translations["totalEuropeanOps"],
                                translations["totalUSSteel"],
                                translations["totalTubularOperations"]
                            )
                        )
                    }
                        ?: throw IllegalArgumentException(translations["pleaseSelectCalculation"])
                )
            }

        return mutableListOf(
            SheetData(
                mutableMapOf(
                    "section" to
                        specialSections.filter { it.hasToBeRendered() } +
                            sections.filter { it.hasToBeRendered() } +
                            summarySections.filter { it.hasToBeRendered() }
                )
            )
        )
    }

    private fun getMaterialsForSection(param: String): List<MaterialDto> {
        val materialCodes = paramService.getParamByCode(param).value?.split(";") ?: mutableListOf()

        val summaryProducts = materialService.getAllMaterialByCodes(materialCodes)
        summaryProducts.validateMaterialsProducts(materialCodes, param)
        return summaryProducts
    }

    private fun computeSpecialSection(
        filter: MaterialIntensityReportFilter,
        scopeId: Long,
        quantities: Map<String, SummationResult>,
        results: Map<String, SummationResult>,
        nodes: Map<String, String>,
        denominatorIds: List<SimpleMaterialDto>,
        sectionsNames: List<String?>,
        scopeIdToComp: Long? = null,
        nodeTypeId: Long? = null
    ): MutableList<MaterialIntensityReportData> {

        // create all structures for calculations
        val nodesMap = mutableMapOf<Long, MaterialIntensityReportData>()
        val calculatedNodeTypeId = nodeTypeId ?: NodeTypeSuper.INTEGRATED_TYPE_ID
        nodes.filter { (k, _) -> (k.parseKeyId(1) == calculatedNodeTypeId) }.forEach { (k, v) ->
            k.parseKeyId(0)?.let { nodesMap[it] = MaterialIntensityReportData(v) }
        }

        val lineTotalUs =
            calculatedNodeTypeId.isIntegratedNodeType()?.let {
                MaterialIntensityReportData(sectionsNames[0], isEmpty = false)
            }
        val lineTotalEU =
            calculatedNodeTypeId.isIntegratedNodeType()?.let {
                MaterialIntensityReportData(sectionsNames[1], isEmpty = false)
            }
        val lineTotalCan =
            calculatedNodeTypeId.isIntegratedNodeType()?.let {
                MaterialIntensityReportData(sectionsNames[2], isEmpty = false)
            }
        val lineTotal =
            MaterialIntensityReportData(
                sectionsNames[3],
                isEmpty = calculatedNodeTypeId.isIntegratedNodeType()?.let { false } ?: true
            )
        val lineTotalScope2Comparison =
            scopeIdToComp?.let { MaterialIntensityReportData(sectionsNames[4], isEmpty = false) }
        val summationMap =
            mapOf(
                "${RegionSuper.ID_USA}-${CountrySuper.ID_USA}" to lineTotalUs,
                "${RegionSuper.ID_EUROPE}" to lineTotalEU,
                "${RegionSuper.ID_USA}-${CountrySuper.ID_CANADA}" to lineTotalCan,
                "total" to lineTotal
            )

        // compute calculation quantities for all selected scopes
        denominatorIds.forEach {
            quantities.filter { (k, _) -> (k.parseKeyId(3) == it.id) }.forEach { (k, v) ->
                // parsed identifiers
                val year = k.parseKeyId(0)
                val month = k.parseKeyId(1)
                val nodeId = k.parseKeyId(2)
                val regionId = k.parseKeyId(5)
                val countryId = k.parseKeyId(6)
                val lines =
                    listOf(
                        nodesMap[nodeId],
                        summationMap["${regionId}-${countryId}"],
                        summationMap["$regionId"],
                        summationMap["total"]
                    )
                if (lines[0] != null) {
                    lines.forEach { report ->
                        report?.let { r ->
                            computeValues(r, filter, v, month, year, isQuantity = true)
                        }
                    }
                }
            }
        }

        // compute all selected month results for selected scope
        results
            .filter { (k, _) -> (k.parseKeyId(3) == scopeId) || (k.parseKeyId(3) == scopeIdToComp) }
            .forEach { (k, v) ->
                // parsed identifiers
                val year = k.parseKeyId(0)
                val month = k.parseKeyId(1)
                val nodeId = k.parseKeyId(2)
                val parsedScopeId = k.parseKeyId(3)
                val regionId = k.parseKeyId(4)
                val countryId = k.parseKeyId(5)
                val lines =
                    listOf(
                        nodesMap[nodeId],
                        summationMap["${regionId}-${countryId}"],
                        summationMap["$regionId"],
                        summationMap["total"]
                    )
                lines[0]?.let {
                    if (parsedScopeId == scopeId) {
                        lines.forEach { report ->
                            report?.let { r ->
                                computeValues(r, filter, v, month, year, isQuantity = false)
                            }
                        }
                    } else if (parsedScopeId == scopeIdToComp) {
                        computeValues(
                            lineTotalScope2Comparison,
                            filter,
                            v,
                            month,
                            year,
                            isQuantity = false
                        )
                    }
                }
            }

        val computedLines =
            (nodesMap.values + summationMap.values).mapNotNull { it }.toMutableList()

        summationMap["total"]?.let {
            lineTotalScope2Comparison?.addQuantityValues(
                it.getQuantityValues(),
                it.getQuantityValues(prevYear = true)
            )
        }
        lineTotalScope2Comparison?.let { computedLines.add(it) }
        return computedLines
    }

    private fun computeSummarySection(
        filter: MaterialIntensityReportFilter,
        scopeId: Long,
        quantities: Map<String, SummationResult>,
        results: Map<String, SummationResult>,
        nodes: Map<String, String>,
        denominatorId: Long,
        sectionsNames: List<String?>
    ): MutableList<MaterialIntensityReportData> {

        // create all structures for calculations
        val nodesMap = mutableMapOf<Long, String>()
        val tubularNodesMap = mutableMapOf<Long, String>()

        nodes.forEach { (k, v) ->
            k.parseKeyId(1)?.let { nodeTypeId ->
                k.parseKeyId(0)?.let { nodeId ->
                    if (nodeTypeId == NodeTypeSuper.TUBULAR_TYPE_ID) {
                        tubularNodesMap[nodeId] = v
                    }
                    nodesMap[nodeId] = v
                }
            }
        }

        val lineTotalNA = MaterialIntensityReportData(sectionsNames[0], isEmpty = false)
        val lineTotalEU = MaterialIntensityReportData(sectionsNames[1], isEmpty = false)
        val lineTotal = MaterialIntensityReportData(sectionsNames[2], isEmpty = false)
        val summationMap =
            mapOf(
                "${RegionSuper.ID_USA}" to lineTotalNA,
                "${RegionSuper.ID_EUROPE}" to lineTotalEU,
                "tubular" to MaterialIntensityReportData(sectionsNames[3], isEmpty = false),
                "total" to lineTotal
            )

        quantities.filter { (k, _) -> (k.parseKeyId(3) == denominatorId) }.forEach { (k, v) ->
            // parsed identifiers
            val year = k.parseKeyId(0)
            val month = k.parseKeyId(1)
            val nodeId = k.parseKeyId(2)
            val regionId = k.parseKeyId(5)
            val lines =
                listOf(
                    tubularNodesMap[nodeId]?.let { summationMap["tubular"] }
                        ?: summationMap["$regionId"],
                    summationMap["total"]
                )
            val hasToBeComputed = nodesMap[nodeId]
            hasToBeComputed?.let {
                lines.forEach { report ->
                    report?.let { r -> computeValues(r, filter, v, month, year, isQuantity = true) }
                }
            }
        }

        results.filter { (k, _) -> (k.parseKeyId(3) == scopeId) }.forEach { (k, v) ->
            // parsed identifiers
            val year = k.parseKeyId(0)
            val month = k.parseKeyId(1)
            val nodeId = k.parseKeyId(2)
            val regionId = k.parseKeyId(4)
            val lines =
                listOf(
                    tubularNodesMap[nodeId]?.let { summationMap["tubular"] }
                        ?: summationMap["$regionId"],
                    summationMap["total"]
                )
            val hasToBeComputed = nodesMap[nodeId]
            hasToBeComputed?.let {
                lines.forEach { report ->
                    report?.let { r ->
                        computeValues(r, filter, v, month, year, isQuantity = false)
                    }
                }
            }
        }

        return summationMap.values.map { it }.toMutableList()
    }

    private fun computeValues(
        computedSection: MaterialIntensityReportData?,
        filter: MaterialIntensityReportFilter,
        result: SummationResult,
        month: Long?,
        year: Long?,
        isQuantity: Boolean
    ) {
        if (year?.toInt() == filter.baseYear) {
            if (isQuantity) {
                computedSection?.addQuantityValue(0, result.value)
            } else {
                computedSection?.addAnalyticalValue(0, result.value)
            }
        } else if (year?.toInt() == (filter.year?.minus(1))) {
            month?.let {
                if (isQuantity) {
                    computedSection?.addQuantityValue(2, result.value)
                } else {
                    computedSection?.addAnalyticalValue(2, result.value)
                }
                if (it <= filter.rpd!!) {
                    if (isQuantity) {
                        computedSection?.addQuantityValue(month.toInt() + 2, null, result.value)
                    } else {
                        computedSection?.addAnalyticalValue(month.toInt() + 2, null, result.value)
                    }
                }
            }
        } else if (year?.toInt() == filter.year) {
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

    private fun setProdAbbrValue(quantities: Map<String, SummationResult>) {
        for (quantity in quantities.values) {
            if (quantity.unitAbbr != null && prodUnitAbbr == null) {
                val unit = unitService.getUnitByAbbr(quantity.unitAbbr)
                prodUnitAbbr = unit?.name
                break
            }
        }
    }

    private fun setAnalysisParamUnitAbbr(analysisParamId: Long) {
        val unit = unitSetSettingsAP.getAPUnit(analysisParamId)
        analysisParamUnitAbbr = unit?.unit?.abbr.formatForReport()
    }

    private fun getSectionHeader(
        translation: Map<String?, String?>,
        nodeType: NodeTypeDto
    ): String {
        val materialHeader = nodeType.materialReport?.name
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

    override fun generateGroupHeadersMap(
        filter: MaterialIntensityReportFilter,
        translations: Map<String?, String?>,
        map: HashMap<String, Any?>,
        params: Any?
    ) {
        super.generateGroupHeadersMap(filter, translations, map, params)

        map["corpResultsReportHeader1"] = translations["ussSteelCorporation"]
        map["corpResultsReportHeader2"] =
            translations["corpResultsReportHeader2energySpecial"]?.let {
                String.format(it, filter.baseYear.toString(), filter.year.toString()).uppercase()
            }

        map["facility"] = translations["facility"]
        map["headerYear3"] = filter.baseYear
        map["headerYear3Base"] = translations["headerYear3Base"]
        map["headerYear1"] = filter.year?.minus(1)?.let { BigDecimal(it) }
        map["headerYear"] = filter.year?.let { BigDecimal(it) }
        map["ytd"] = translations["ytd"]
        map["changeLabel"] = translations["changeLabel"]
        map["paramNote"] = translations["paramNoteSpecial"]

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
        map["exportFootNote"] = translations["exportFootNote"]
        map["paramNote"] = translations["paramNoteCarbonReport"]
        map["YTDCalculationThrough"] = translations["YTDCalculationThrough"]
        map["paramYTDCalculationThrough"] = translations["month.${filter.rpd}"]
    }
}
