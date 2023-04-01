package sk.esten.uss.gbco2.service.exports

import java.io.ByteArrayOutputStream
import javax.persistence.Tuple
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.filter.exports.AuditReportFilter
import sk.esten.uss.gbco2.dto.request.filter.exports.SpecialAuditReportFilter
import sk.esten.uss.gbco2.model.repository.quantity.AdvQuantityRepository
import sk.esten.uss.gbco2.service.DictionaryUiService
import sk.esten.uss.gbco2.service.exports.data.AuditReportParser
import sk.esten.uss.gbco2.utils.*

@Service
class AuditReportService(
    private val dictionaryUiService: DictionaryUiService,
    private val quantityRepository: AdvQuantityRepository
) {
    // Not override generics report service - caused by performance issue with large datasets
    fun generateReport(
        filter: AuditReportFilter?,
        dataGroupNames: List<String>,
        auditTemplateName: String,
        auditExportDownloadName: String
    ): Pair<ByteArray, String>? {

        val translations = getAllTranslations()

        val workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Audit")
        val cellStyle = workbook.createCellStyle()

        // generate header of sheet
        val headerRow = sheet.createRow(0)
        ExportConstants.auditReport.forEachIndexed { index, key ->
            sheet.setColumnWidth(index, 5000)
            headerRow.createCell(index).setStyle(cellStyle).setValue(translations[key] ?: key)
        }

        // get data from db
        val result = filter?.let { quantityRepository.getQuantityAudit(it) }
        val dataParser = AuditReportParser()

        // fill out Excel sheet
        result?.forEachIndexed { index, data ->
            val row = sheet.createRow(index + 1)
            dataParser.parseDataIntoRow(translations, data, row, cellStyle)
        }
        val baOut = ByteArrayOutputStream()
        workbook.write(baOut)
        val content = baOut.toByteArray()
        baOut.close()

        return content to "$auditExportDownloadName.xlsx"
    }

    fun generateReport(
        filter: SpecialAuditReportFilter?,
        dataGroupNames: List<String>,
        auditTemplateName: String,
        auditExportDownloadName: String
    ): Pair<ByteArray, String>? {

        val translations = getAllTranslations()

        val workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Audit")
        val cellStyle = workbook.createCellStyle()

        // generate header of excel sheet
        val headerRow = sheet.createRow(0)
        val specialAuditHeaderKeys = ExportConstants.auditReport.toMutableList()
        specialAuditHeaderKeys.add(16, "aParamName")
        specialAuditHeaderKeys.add(17, "factorA")
        specialAuditHeaderKeys.add("CC2")
        specialAuditHeaderKeys.forEachIndexed { index, key ->
            sheet.setColumnWidth(index, 5000)
            if (index == 16 || index == 17) {
                headerRow
                    .createCell(index)
                    .setStyle(cellStyle)
                    .setValue("${translations[key] ?: key} 2")
            } else {
                headerRow.createCell(index).setStyle(cellStyle).setValue(translations[key] ?: key)
            }
        }

        // get data from db
        val auditReportFilter =
            AuditReportFilter().apply {
                materialIds = filter?.materialIds
                nodeIds = filter?.nodeIds
                analysisParamIds =
                    listOfNotNull(filter?.analysisParamId, filter?.analysisParamIdProj)
                dateTo = filter?.dateTo
                dateFrom = filter?.dateFrom
                scopeId = filter?.scopeId
            }
        val result = quantityRepository.getQuantityAudit(auditReportFilter)
        val dataParser = AuditReportParser()
        val groupedResult = prepareTuplesForSpecialExport(result)

        // fill out Excel sheet
        groupedResult.values.forEachIndexed { index, data ->
            val row = sheet.createRow(index + 1)
            dataParser.parseDataIntoRow(translations, data, filter?.analysisParamId, row, cellStyle)
        }
        val baOut = ByteArrayOutputStream()
        workbook.write(baOut)
        val content = baOut.toByteArray()
        baOut.close()

        return content to "$auditExportDownloadName.xlsx"
    }

    private fun prepareTuplesForSpecialExport(tuples: List<Tuple>): Map<Long, List<Tuple>> {
        val map: MutableMap<Long, MutableList<Tuple>> = mutableMapOf()
        tuples.forEach { tuple ->
            val quantityId = tuple.getBigDecimal("id_quantity")?.toLong()
            quantityId?.let { id ->
                if (map[id] == null) {
                    map[id] = mutableListOf(tuple)
                } else {
                    map[id]?.add(tuple)
                }
            }
        }
        return map
    }

    private fun getAllTranslations(): Map<String?, String?> {
        val toTranslate = ExportConstants.auditReport.toMutableList()
        toTranslate.addAll(getMonthTranslationKeys())
        return this.dictionaryUiService.getAllTranslatedLabels(toTranslate)
    }
}
