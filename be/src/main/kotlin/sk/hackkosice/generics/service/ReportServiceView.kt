package sk.esten.uss.gbco2.generics.service

import org.springframework.data.domain.Sort
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.ReportParamsFilterDto
import sk.esten.uss.gbco2.service.DictionaryUiService
import sk.esten.uss.gbco2.service.exports.FOPService
import sk.esten.uss.gbco2.service.exports.FileTypeEnum
import sk.esten.uss.gbco2.service.exports.data.Exportable
import sk.esten.uss.gbco2.service.exports.data.SheetData
import sk.esten.uss.gbco2.utils.createHeaderKey

abstract class ReportServiceView<
    DTO : Exportable, ID : Any, FILTER : ReportParamsFilterDto, SHEET : SheetData<DTO>> {

    abstract val dictionaryUiService: DictionaryUiService
    abstract val fopService: FOPService

    fun generateReport(
        filter: FILTER,
        dataGroupNames: List<String>?,
        templateName: String,
        targetName: String,
    ): Pair<ByteArray, String>? {
        val translations = getUIKeysForTranslations()
        val resultList = getDataForReport(filter, translations, dataGroupNames)
        val computedResultList = additionalDataComputation(filter, translations, resultList)
        val dataMap = mapOf("sheet" to generateDataMap(filter, translations, computedResultList))

        if (filter.fileType == FileTypeEnum.XLSX) {
            return fopService.generateBinaryXlsx(
                    "${getTemplateName(filter, templateName, translations)}.xlsx",
                    dataMap
                )
                ?.let { it to "$targetName.xlsx" }
        } else if (filter.fileType == FileTypeEnum.PDF) {
            TODO("Not yet implemented ")
        }
        return null
    }
    open fun additionalDataComputation(
        filter: FILTER,
        translations: Map<String?, String?>,
        dto: MutableList<SHEET>
    ): List<SHEET> = dto

    private fun generateDataMap(
        filter: FILTER,
        translations: Map<String?, String?>,
        data: List<SHEET>
    ): List<HashMap<String, Any?>> {
        val dataForSheet: MutableList<HashMap<String, Any?>> = mutableListOf()
        data.forEach { sheet ->
            val map = HashMap<String, Any?>()
            generateGroupHeadersMap(filter, translations, map, sheet.param)
            sheet.data.forEach { (dataGroupName, dataList) ->
                generateGroupDataMap(dataGroupName, translations, map, dataList, sheet.param)
            }
            dataForSheet.add(map)
        }
        return dataForSheet
    }

    open fun generateGroupHeadersMap(
        filter: FILTER,
        translations: Map<String?, String?>,
        map: HashMap<String, Any?>,
        params: Any?
    ) {
        translations.forEach { map[it.key.createHeaderKey() ?: "undefined"] = it.value }
    }

    open fun getTemplateName(
        filter: FILTER,
        targetName: String,
        translations: Map<String?, String?>
    ): String = targetName

    private fun generateGroupDataMap(
        dataGroupName: String,
        translations: Map<String?, String?>,
        map: MutableMap<String, Any?>,
        data: List<DTO>,
        params: Any?
    ) {
        val maps: MutableList<Map<String, Any?>> = mutableListOf()
        for (report in data) {
            maps.add(report.fillDataMap(translations, params))
        }
        map[dataGroupName] = maps
    }

    abstract fun getUIKeysForTranslations(): Map<String?, String?>

    @Transactional(readOnly = true)
    abstract fun getDataForReport(
        filter: FILTER?,
        translations: Map<String?, String?>,
        dataGroupNames: List<String>?
    ): MutableList<SHEET>

    protected fun sortBy(attributes: List<String>): Sort {
        return Sort.by(
            attributes.map { Sort.Order(Sort.Direction.ASC, it, Sort.NullHandling.NULLS_LAST) }
        )
    }
}
