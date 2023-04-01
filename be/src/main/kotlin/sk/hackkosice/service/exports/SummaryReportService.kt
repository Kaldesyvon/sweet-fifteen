package sk.esten.uss.gbco2.service.exports

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.config.enums.EnvEnum
import sk.esten.uss.gbco2.dto.request.filter.exports.SummaryReportFilter
import sk.esten.uss.gbco2.generics.service.ReportServiceView
import sk.esten.uss.gbco2.model.repository.quantity.AdvQuantityRepository
import sk.esten.uss.gbco2.service.*
import sk.esten.uss.gbco2.service.exports.data.SheetData
import sk.esten.uss.gbco2.service.exports.data.SummaryReportData
import sk.esten.uss.gbco2.utils.*

@Service
class SummaryReportService(
    override val dictionaryUiService: DictionaryUiService,
    override val fopService: FOPService,
    private val nodeService: NodeService,
    private val paramService: ParamService,
    private val scopeService: ScopeService,
    private val advQuantityRepository: AdvQuantityRepository,
    @Value("\${uss.gbco2.environment:}") val env: EnvEnum,
    @Value("\${application.version:unknown}") val version: String,
    @Value("\${application.build-time:unknown}") val buildTime: String
) :
    ReportServiceView<
        SummaryReportData, String, SummaryReportFilter, SheetData<SummaryReportData>>() {

    override fun getDataForReport(
        filter: SummaryReportFilter?,
        translations: Map<String?, String?>,
        dataGroupNames: List<String>?
    ): MutableList<SheetData<SummaryReportData>> {
        filter?.nodeIds = nodeService.getAllSubNodeIds(filter?.nodeIds)
        val preparedDataMap =
            advQuantityRepository.getQuantitySummaryReportData(filter).map { SummaryReportData(it) }
        return mutableListOf(
            SheetData(mutableMapOf((dataGroupNames?.get(0) ?: "section") to preparedDataMap))
        )
    }

    override fun getUIKeysForTranslations(): Map<String?, String?> {
        val toTranslate = ExportConstants.summaryExcelTranslations
        toTranslate.add(env.key)
        return dictionaryUiService.getAllTranslatedLabels(toTranslate)
    }

    override fun generateGroupHeadersMap(
        filter: SummaryReportFilter,
        translations: Map<String?, String?>,
        map: HashMap<String, Any?>,
        params: Any?
    ) {
        super.generateGroupHeadersMap(filter, translations, map, params)
        map["summaryHeader1"] = translations["ussSteelCorporation"]
        map["summaryHeader"] =
            String.format(
                translations["summaryHeader"]?.uppercase() ?: "???summaryHeader???",
                filter.year
            )
        map["headerFactors"] = translations["headerFactors"]
        map["headerFactorName"] = translations["aParamName"]

        map.appendFormattedEnvironmentData(translations, env, version, buildTime)

        map.appendFormattedParamsData(translations, paramService)

        map["text3"] = translations["calculation"]
        filter.scopeId?.let {
            val scope = it.let { it1 -> scopeService.getView(it1) }
            map["param3"] = scope.nameTranslated
        }
    }
}
