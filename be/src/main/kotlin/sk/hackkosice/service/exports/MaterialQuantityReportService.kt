package sk.esten.uss.gbco2.service.exports

import java.math.BigDecimal
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.config.enums.EnvEnum
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.MaterialReadAllFilter
import sk.esten.uss.gbco2.dto.request.filter.exports.MaterialQuantityReportFilter
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeDto
import sk.esten.uss.gbco2.generics.service.ReportServiceView
import sk.esten.uss.gbco2.mapper.MaterialMapper
import sk.esten.uss.gbco2.mapper.NodeMapper
import sk.esten.uss.gbco2.model.entity.quantity.IOEnum
import sk.esten.uss.gbco2.model.repository.quantity_month.AdvQuantityMonthRepository
import sk.esten.uss.gbco2.service.*
import sk.esten.uss.gbco2.service.exports.data.MaterialQuantityReportData
import sk.esten.uss.gbco2.service.exports.data.MaterialQuantityReportEnum
import sk.esten.uss.gbco2.service.exports.data.SheetData
import sk.esten.uss.gbco2.service.exports.data.SummationResult
import sk.esten.uss.gbco2.utils.*

@Service
class MaterialQuantityReportService(
    override val dictionaryUiService: DictionaryUiService,
    override val fopService: FOPService,
    private val paramService: ParamService,
    private val nodeDataLockService: NodeDataLockService,
    private val scopeService: ScopeService,
    private val nodeService: NodeService,
    private val materialService: MaterialService,
    private val materialMapper: MaterialMapper,
    private val nodeMapper: NodeMapper,
    private val quantityMonthRepository: AdvQuantityMonthRepository,
    @Value("\${uss.gbco2.environment:}") val env: EnvEnum,
    @Value("\${application.version:unknown}") val version: String,
    @Value("\${application.build-time:unknown}") val buildTime: String
) :
    ReportServiceView<
        MaterialQuantityReportData,
        String,
        MaterialQuantityReportFilter,
        SheetData<MaterialQuantityReportData>>() {

    override fun getDataForReport(
        filter: MaterialQuantityReportFilter?,
        translations: Map<String?, String?>,
        dataGroupNames: List<String>?
    ): MutableList<SheetData<MaterialQuantityReportData>> {

        filter?.validate()

        // get subNodes of plants and superPlants
        val superPlantWithSubNodeIds =
            filter?.superPlantIds?.let { nodeService.getAllSubNodeIds(it, filter.year) }
        val plantWithSubNodeIds =
            filter?.plantIds?.let { nodeService.getAllSubNodeIds(it, filter.year) }

        val allNodeWithSubNodes = mutableListOf<Long>()
        superPlantWithSubNodeIds?.let { allNodeWithSubNodes.addAll(it) }
        plantWithSubNodeIds?.let { allNodeWithSubNodes.addAll(it) }

        val years = filter?.year.rangeOfYears(4)

        // select materials
        val materialFilter =
            MaterialReadAllFilter().apply {
                nodeIds = superPlantWithSubNodeIds
                scopeId = filter?.scopeId
                materialReport = listOf(1)
                withSubNodes = false
                sortColumn = "reportPos"
                direction = Sort.Direction.ASC
            }
        val materials =
            materialService.repositoryGetAllQuery(materialFilter).map {
                materialMapper.mapToSimple(it)
            }

        // select products
        materialFilter.materialReport = listOf(2)
        val products =
            materialService.repositoryGetAllQuery(materialFilter).map {
                materialMapper.mapToSimple(it)
            }

        // select calculated quantities
        val materialIds = materials.mapNotNull { it.id } + products.mapNotNull { it.id }

        // create list for sheets with data
        val data: MutableList<SheetData<MaterialQuantityReportData>> = mutableListOf()
        var renderedMaterials: Map<String, Boolean>? = null

        // assign quantities to materials for total summary sheet
        val selectedNodes = filter?.allNodes()
        if (!selectedNodes.isNullOrEmpty() && selectedNodes.size > 1) {

            val quantitiesTotal =
                quantityMonthRepository.getByMaterialPerMonthWithIO(
                    filter,
                    allNodeWithSubNodes,
                    materialIds
                )

            val quantitiesTotalForYears =
                quantityMonthRepository.getByMaterialPerYearWithIO(
                    filter,
                    allNodeWithSubNodes,
                    materialIds
                )

            val inputData =
                assignQuantitiesToMaterials(
                    materials,
                    quantitiesTotal,
                    quantitiesTotalForYears,
                    years,
                    IOEnum.INPUT,
                    MaterialQuantityReportEnum.INPUT_SECTION
                )
            val outputData =
                assignQuantitiesToMaterials(
                    materials,
                    quantitiesTotal,
                    quantitiesTotalForYears,
                    years,
                    IOEnum.OUTPUT,
                    MaterialQuantityReportEnum.OUTPUT_SECTION
                )
            val productData =
                assignQuantitiesToMaterials(
                    products,
                    quantitiesTotal,
                    quantitiesTotalForYears,
                    years,
                    IOEnum.OUTPUT,
                    MaterialQuantityReportEnum.PRODUCT_SECTION
                )

            // get map of materials, which have to be rendered empty in other sheets (depends on
            // rendered materials in first sheet)
            renderedMaterials =
                inputData.associateBy(
                    { "${MaterialQuantityReportEnum.INPUT_SECTION.value}-${it.material.id}" },
                    { true }
                ) +
                    outputData.associateBy(
                        { "${MaterialQuantityReportEnum.OUTPUT_SECTION.value}-${it.material.id}" },
                        { true }
                    )

            data.add(
                SheetData(
                    mutableMapOf(
                        (dataGroupNames?.get(0) ?: "section1") to inputData,
                        (dataGroupNames?.get(1) ?: "section2") to outputData,
                        (dataGroupNames?.get(2) ?: "section3") to productData
                    )
                )
            )
        }

        // select quantities for each node
        val quantities =
            quantityMonthRepository.getByNodesPerMonthWithIO(
                filter,
                superPlantWithSubNodeIds,
                plantWithSubNodeIds,
                materialIds
            )

        val quantitiesForYears =
            quantityMonthRepository.getByNodesAndMaterialsPerYearWithIO(
                filter,
                superPlantWithSubNodeIds,
                plantWithSubNodeIds,
                materialIds
            )

        // select all nodes for generate sheets
        val nodeFilter =
            ReadAllParamsFilterDto().apply {
                sortColumn = "nameTranslated"
                direction = Sort.Direction.ASC
            }
        val nodes =
            nodeService.getAllNodesByNodeIds(filter?.allNodes(), nodeFilter.toSort()).map {
                nodeMapper.mapSimple(it)
            }

        nodes.forEach {
            data.add(
                SheetData(
                    mutableMapOf(
                        (dataGroupNames?.get(0)
                            ?: "section1") to
                            assignQuantitiesToMaterials(
                                materials,
                                quantities,
                                quantitiesForYears,
                                years,
                                IOEnum.INPUT,
                                MaterialQuantityReportEnum.INPUT_SECTION,
                                it.id,
                                renderedMaterials
                            ),
                        (dataGroupNames?.get(1)
                            ?: "section2") to
                            assignQuantitiesToMaterials(
                                materials,
                                quantities,
                                quantitiesForYears,
                                years,
                                IOEnum.OUTPUT,
                                MaterialQuantityReportEnum.OUTPUT_SECTION,
                                it.id,
                                renderedMaterials
                            ),
                        (dataGroupNames?.get(2)
                            ?: "section3") to
                            assignQuantitiesToMaterials(
                                products,
                                quantities,
                                quantitiesForYears,
                                years,
                                IOEnum.OUTPUT,
                                MaterialQuantityReportEnum.PRODUCT_SECTION,
                                it.id,
                                renderedMaterials
                            )
                    ),
                    it,
                )
            )
        }

        return data
    }

    // computation of the right id for quantity assignation
    private fun getAssignKeyIncludeMonth(
        year: Int,
        index: Int?,
        nodeId: Long? = null,
        materialId: Long?,
        io: IOEnum,
    ): String {
        return if (nodeId == null) "(${year})-(${index})-(${materialId})-(${io.dbValue})"
        else "(${year})-(${index})-(${nodeId})-(${materialId})-(${io.dbValue})"
    }

    private fun getAssignKey(
        year: Int,
        nodeId: Long? = null,
        materialId: Long?,
        io: IOEnum,
    ): String {
        return if (nodeId == null) "(${year})-(${materialId})-(${io.dbValue})"
        else "(${year})-(${nodeId})-(${materialId})-(${io.dbValue})"
    }

    // assignation of the right quantity value to target material - return computed data structure
    private fun assignQuantitiesToMaterials(
        materials: List<SimpleMaterialDto>,
        quantities: Map<String, SummationResult>,
        quantitiesForYears: Map<String, SummationResult>,
        years: List<Int>,
        io: IOEnum,
        section: MaterialQuantityReportEnum,
        nodeId: Long? = null,
        renderedOptions: Map<String, Boolean>? = null
    ): MutableList<MaterialQuantityReportData> {
        val inputMaterialsQuantityReportData = mutableListOf<MaterialQuantityReportData>()
        materials.forEach {
            val report = MaterialQuantityReportData(it, section, renderedOptions)

            // append quantities sum for years overview
            for (i in 0..2) {
                report.addValue(
                    i,
                    quantitiesForYears[getAssignKey(years[3 - i], nodeId, it.id, io)]
                )
            }

            // append values for months of selected years
            for (i in 3..14) {
                report.addValue(
                    i,
                    quantities[getAssignKeyIncludeMonth(years[0], i - 2, nodeId, it.id, io)],
                    quantities[getAssignKeyIncludeMonth(years[1], i - 2, nodeId, it.id, io)]
                )
            }

            if (report.hasToBeRendered()) {
                inputMaterialsQuantityReportData.add(report)
            }
        }
        return inputMaterialsQuantityReportData
    }

    override fun getUIKeysForTranslations(): Map<String?, String?> {
        val toTranslate = ExportConstants.materialQuantityExcelTranslations
        toTranslate.add(env.key)
        toTranslate.addAll(getMonthTranslationKeys())
        return dictionaryUiService.getAllTranslatedLabels(toTranslate)
    }

    override fun generateGroupHeadersMap(
        filter: MaterialQuantityReportFilter,
        translations: Map<String?, String?>,
        map: HashMap<String, Any?>,
        params: Any?
    ) {
        val node = params as SimpleNodeDto?
        super.generateGroupHeadersMap(filter, translations, map, params)
        val param = translations["total"]
        if (node != null) {
            map["sheetName"] = node.name
        } else {
            map["sheetName"] = param
        }
        map["materialQuantitiesHeader1"] = translations["ussSteelCorporation"]
        map["materialQuantitiesHeader"] =
            String.format(
                translations["materialQuantitiesHeader"] ?: "???materialQuantitiesHeader???",
                param?.uppercase()
            )
        map["headerYearYTD"] = "${filter.year} ${translations["headerYtd"]}"
        map["headerYear1YTD"] = "${filter.year?.minus(1)} ${translations["headerYtd"]}"
        map["headerYear3"] = filter.year?.minus(3)?.let { BigDecimal(it) }
        map["headerYear2"] = filter.year?.minus(2)?.let { BigDecimal(it) }
        map["headerYear1"] = filter.year?.minus(1)?.let { BigDecimal(it) }
        map["headerYear"] = filter.year?.let { BigDecimal(it) }
        map["headerUnitAbbr"] = translations["headerUnit"]

        map.appendFormattedEnvironmentData(translations, env, version, buildTime)

        map.appendFormattedParamsData(translations, paramService)

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

        map["text6"] = translations["calculation"]
        filter.scopeId?.let {
            val scope = it.let { it1 -> scopeService.getView(it1) }
            map["param6"] = scope.nameTranslated
        }
    }
}
