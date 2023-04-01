package sk.esten.uss.gbco2.service.exports

import java.io.*
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import org.apache.fop.apps.FopFactory
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.*
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import sk.esten.uss.gbco2.utils.formatForReport
import sk.esten.uss.gbco2.utils.getInputStream
import sk.esten.uss.gbco2.utils.tryCast

@Service
class FOPService {
    private val XLT_FOR_EACH_STM = "xlt:forEach"
    private val XLT_INTENSITIES_CONTENT = "xlt:intensitiesContent"
    private val START_TOKEN = "$"
    private val END_TOKEN = "%"
    private val FO2HTML_TEMPLATE_NAME = "fo2html.xsl"

    private fun generateBinaryXlsx(
        wb: XSSFWorkbook,
        mapData: Map<String, List<Map<String, Any?>>>
    ): ByteArray? {
        val sheets = mapData["sheet"]
        if (sheets.isNullOrEmpty()) {
            val sheet = wb.getSheetAt(0)
            transformXlsxTemplate(sheet, mapData)
        } else {
            for (i in sheets.indices) {
                if (wb.numberOfSheets <= i) {
                    wb.cloneSheet(0)
                }
            }
            for (i in sheets.indices) {
                val sheet = wb.getSheetAt(i)
                val mappedDataSheet = sheets[i]
                if (mappedDataSheet["sheetName"] != null) {
                    wb.setSheetName(i, mappedDataSheet["sheetName"].toString())
                }
                transformXlsxTemplate(sheet, mappedDataSheet)
            }
        }
        wb.setPrintArea(
            0,
            0,
            getMaxCellsCount(wb.getSheetAt(0)) - 1,
            0,
            getRowsCount(wb.getSheetAt(0)) - 1
        )
        val baOut = ByteArrayOutputStream()
        wb.write(baOut)
        val content = baOut.toByteArray()
        baOut.close()
        return content
    }

    fun generateBinaryXlsx(
        templateFile: String,
        mapData: Map<String, List<Map<String, Any?>>>
    ): ByteArray? {
        val fileInputStream = "reports/$templateFile".getInputStream(this::class.java.classLoader)

        val wb = XSSFWorkbook(fileInputStream)
        return generateBinaryXlsx(wb, mapData)
    }

    private fun getRowsCount(sheet: XSSFSheet): Int {
        return sheet.lastRowNum
    }

    private fun getMaxCellsCount(sheet: XSSFSheet): Int {
        var maxCells = 0
        var maxCellsTemp = 0
        val rows: Iterator<*> = sheet.rowIterator()
        while (rows.hasNext()) {
            val row = rows.next() as XSSFRow
            val cells: Iterator<*> = row.cellIterator()
            while (cells.hasNext()) {
                cells.next()
                maxCellsTemp++
            }
            maxCells = maxCells.coerceAtLeast(maxCellsTemp)
            maxCellsTemp = 0
        }
        return maxCells
    }

    private fun transformXlsxTemplate(sheet: XSSFSheet, data: Map<String, Any?>?) {
        if (data.isNullOrEmpty()) return
        val rowsToRemove: MutableMap<String, XSSFRow?> = mutableMapOf()
        var rowsAdded: Boolean
        do {
            rowsAdded = false
            val rows: Iterator<*> = sheet.rowIterator()
            while (rows.hasNext()) {
                val row = rows.next() as XSSFRow? ?: break
                val cells: Iterator<*> = row.cellIterator()
                while (cells.hasNext()) {
                    val cell = cells.next() as XSSFCell
                    if (cell.cellType != CellType.STRING) {
                        continue
                    }
                    val cellValue = cell.richStringCellValue.string
                    val stToken = cellValue.indexOf(START_TOKEN)
                    val endToken = cellValue.indexOf(END_TOKEN)
                    if (stToken != -1 && endToken != -1) {
                        val key = cellValue.substring(stToken + 1, endToken)
                        if (key.startsWith(XLT_INTENSITIES_CONTENT) &&
                                !rowsToRemove.containsKey(key)
                        ) {
                            val dataSection =
                                data[parseStmKey(key)].tryCast<MutableList<Map<String, Any?>>>()
                            val rowToRemove = invokeXLTForEachForIntensity(sheet, row, dataSection)
                            rowsToRemove[key] = rowToRemove
                            rowsAdded = true
                            break
                        }
                        if (key.startsWith(XLT_FOR_EACH_STM) && !rowsToRemove.containsKey(key)) {
                            val dataSection =
                                data[parseStmKey(key)].tryCast<MutableList<Map<String, Any?>>>()
                            val rowToRemove = invokeXLTForEach(sheet, row, dataSection)
                            rowsToRemove[key] = rowToRemove
                            rowsAdded = true
                            break
                        }
                    }
                    replaceDataTokens(cell, data)
                }
                if (rowsAdded) break
            }
        } while (rowsAdded)
    }

    private fun replaceDataTokens(cell: XSSFCell, data: Map<String, Any?>) {
        var cellValue: String =
            try {
                cell.richStringCellValue.string
            } catch (e: NumberFormatException) {
                return
            }
        var stToken = cellValue.indexOf(START_TOKEN)
        var endToken = cellValue.indexOf(END_TOKEN)
        while (stToken != -1 && endToken != -1) {
            val key = cellValue.substring(stToken + 1, endToken)
            val token = "$$key%"
            val keyIx = cellValue.indexOf(token)
            var before = ""
            if (keyIx != -1) {
                before = cellValue.substring(0, keyIx)
            }
            var after = ""
            if (cellValue.length > keyIx + token.length) {
                after = cellValue.substring(keyIx + token.length, cellValue.length)
            }
            val dataValue = data[key]
            when (dataValue) {
                is BigDecimal -> {
                    val numberDataValue = data[key] as BigDecimal?
                    cell.setCellValue(numberDataValue?.toDouble() ?: 0.0)
                    return
                }
                is Long -> {
                    val numberDataValue = data[key] as Long?
                    cell.setCellValue(numberDataValue?.toDouble() ?: 0.0)
                    return
                }
                is Int -> {
                    val numberDataValue = data[key] as Int?
                    cell.setCellValue(numberDataValue?.toDouble() ?: 0.0)
                    return
                }
                is Double -> {
                    val numberDataValue = data[key] as Double?
                    cell.setCellValue(numberDataValue ?: 0.0)
                    return
                }
                is LocalDateTime -> {
                    val numberDataValue = data[key] as LocalDateTime?
                    cell.setCellValue(numberDataValue)
                    return
                }
            }
            val stringDataValue = (dataValue as String?).formatForReport()
            cellValue = before + stringDataValue + after
            stToken = cellValue.indexOf(START_TOKEN)
            endToken = cellValue.indexOf(END_TOKEN)
        }
        cell.setCellValue(XSSFRichTextString(cellValue))
    }

    private fun parseStmKey(str: String?): String? {
        return str?.let {
            val sIx = it.indexOf("(")
            val eIx = it.indexOf(")")
            return it.substring(sIx + 1, eIx)
        }
    }

    private fun invokeXLTForEach(
        sheet: XSSFSheet,
        rowParam: XSSFRow?,
        data: MutableList<Map<String, Any?>>?
    ): XSSFRow? {
        var row = rowParam
        if (data.isNullOrEmpty()) return row
        for (i in data.indices) {
            if (row == null) break
            var newRow: XSSFRow?
            val newRowNum = (row.rowNum + 1).toLong()
            if (i < data.size - 1) {
                newRow = insertRow(sheet, newRowNum, row)
                newRow?.rowNum = newRowNum.toInt()
            }
            val cells: Iterator<*> = row.cellIterator()
            while (cells.hasNext()) {
                replaceDataTokens(cells.next() as XSSFCell, data[i])
            }
            row = sheet.getRow(newRowNum.toInt())
        }
        return null
    }

    private fun invokeXLTForEachForIntensity(
        sheet: XSSFSheet,
        rowParam: XSSFRow,
        dataSection: MutableList<Map<String, Any?>>?
    ): XSSFRow? {
        if (dataSection.isNullOrEmpty()) return rowParam
        var index = rowParam.rowNum + 1
        val dataRowParam: XSSFRow = sheet.getRow(index)
        index++
        var newRow: XSSFRow? = sheet.getRow(index)
        dataSection.forEach { section ->
            if (newRow == null) {
                newRow = insertRow(sheet, index.toLong(), rowParam)
            }
            val cell = newRow?.getCell(0)
            cell?.setCellValue(section["header"] as String?)
            index++

            val dataSubSection = section["data"].tryCast<List<Map<String, Any?>>>()
            dataSubSection.forEach { subSection ->
                newRow = insertRow(sheet, index.toLong(), dataRowParam)
                val cells: MutableIterator<Cell>? = newRow?.cellIterator()
                cells?.let {
                    while (it.hasNext()) {
                        replaceDataTokens(it.next() as XSSFCell, subSection)
                    }
                }
                index++
            }
            newRow = null
        }
        sheet.removeRow(rowParam)
        sheet.removeRow(dataRowParam)
        // sheet.shiftRows(rowParam.rowNum + 2, sheet.lastRowNum, -2) // TODO need to fix - task
        // 3793
        return null
    }

    private fun insertRow(sheet: XSSFSheet, ix: Long, row: XSSFRow): XSSFRow? {
        if (ix <= sheet.lastRowNum) {
            sheet.shiftRows(ix.toInt(), sheet.lastRowNum, 1, true, false)
        }
        val newRow = sheet.createRow(ix.toInt())
        val i: Iterator<*> = row.cellIterator()
        while (i.hasNext()) {
            val cell = i.next() as XSSFCell
            val newCell = newRow.createCell(cell.columnIndex)
            newCell.cellStyle = cell.cellStyle
            if (cell.cellType == CellType.STRING) {
                newCell.setCellValue(cell.richStringCellValue)
            }
        }
        return newRow
    }

    fun generateHTML(xml: String): ByteArray {
        return transform(xml, getTemplateFile(FO2HTML_TEMPLATE_NAME))
    }

    private fun transform(xml: String, xsltFile: File): ByteArray {
        val bos = ByteArrayOutputStream()
        try {
            System.setProperty("jdk.xml.xpathExprOpLimit", "200")
            val factory = TransformerFactory.newInstance()
            val transformer = factory.newTransformer(StreamSource(xsltFile))
            transformer.transform(StreamSource(StringReader(xml)), StreamResult(bos))
            return bos.toByteArray()
        } catch (e: Exception) {
            throw e
        } finally {
            bos.close()
        }
    }

    private fun getConfiguredFopFactory(): FopFactory =
        FopFactory.newInstance(getFile("fopConfig.xml"))

    private fun getFile(name: String) = ResourceUtils.getFile("classpath:fop/$name")

    private fun getTemplateFile(name: String) = getFile("template/$name")
}
