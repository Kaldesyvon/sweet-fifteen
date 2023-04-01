package sk.esten.uss.gbco2.service.exports.data

import java.math.BigDecimal
import javax.persistence.Tuple
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import sk.esten.uss.gbco2.service.exports.ExportConstants.Companion.CO2_FORMULA
import sk.esten.uss.gbco2.service.exports.ExportConstants.Companion.DIVIDE_SCALE
import sk.esten.uss.gbco2.utils.*

open class AuditReportParser {
    fun parseDataIntoRow(
        translations: Map<String?, String?>,
        data: Tuple,
        row: Row,
        cellStyle: XSSFCellStyle,
    ) {
        // values for computations
        val quantity = data.getBigDecimal("quantity")
        val io = data.getBigDecimal("io")
        val factorA = data.getBigDecimal("factor_a")
        val useCalculation = data.getBigDecimal("use_calculation")
        val calculatedQ = calculateQuantity(quantity, io)

        row.createCell(0).setStyle(cellStyle).setValue(data.getString("plant_name"))
        row.createCell(1)
            .setStyle(cellStyle)
            .setValue(data.getBigDecimal("id_quantity")?.toDouble())
        row.createCell(2)
            .setStyle(cellStyle)
            .setValue(data.getBigDecimal("id_material")?.toDouble())
        row.createCell(3)
            .setStyle(cellStyle)
            .setValue(data.getBigDecimal("id_analysis")?.toDouble())

        val dateFrom = data.getLocalDateTime("date_from")
        row.createCell(4).setStyle(cellStyle).setValue(formatDateForExport(dateFrom))
        row.createCell(5).setStyle(cellStyle).setValue(dateFrom.format("yyyy-MM"))
        row.createCell(6)
            .setStyle(cellStyle)
            .setValue(translations["month.${dateFrom?.monthValue}"])

        row.createCell(7)
            .setStyle(cellStyle)
            .setValue(formatDateForExport(data.getLocalDateTime("date_to")))
        row.createCell(8).setStyle(cellStyle).setValue(calculatedQ)
        row.createCell(9)
            .setStyle(cellStyle)
            .setValue(data.getBigDecimal("quantity_uncertainty")?.toDouble())
        row.createCell(10)
            .setStyle(cellStyle)
            .setValue(data.getBigDecimal("number_of_measurements")?.toDouble())
        row.createCell(11).setStyle(cellStyle).setValue(io?.toDouble())

        // analysis param section
        row.createCell(12).setStyle(cellStyle).setValue(data.getString("ap_name"))
        row.createCell(13).setStyle(cellStyle).setScaledValue(data.getBigDecimal("factor_a"))
        row.createCell(14).setStyle(cellStyle).setScaledValue(data.getBigDecimal("factor_b"))
        row.createCell(15).setStyle(cellStyle).setScaledValue(data.getBigDecimal("factor_c"))
        row.createCell(16)
            .setStyle(cellStyle)
            .setValue(data.getBigDecimal("analysis_uncertainty")?.toDouble())
        row.createCell(17)
            .setStyle(cellStyle)
            .setValue(formatDateForExport(data.getLocalDateTime("valid_from")))

        row.createCell(18).setStyle(cellStyle).setValue(data.getString("unit"))
        row.createCell(19).setStyle(cellStyle).setValue(data.getString("units"))
        row.createCell(20).setStyle(cellStyle).setValue(data.getString("material_name"))
        row.createCell(21).setStyle(cellStyle).setValue(data.getString("bc_material_code"))

        row.createCell(22).setStyle(cellStyle).setValue(data.getString("meter_id"))
        row.createCell(23).setStyle(cellStyle).setValue(data.getString("meter_name"))
        row.createCell(24)
            .setStyle(cellStyle)
            .setValue(data.getBigDecimal("meter_uncertainty")?.toDouble())
        row.createCell(25)
            .setStyle(cellStyle)
            .setValue(data.getBigDecimal("meter_upper_range")?.toDouble())

        // computed values
        row.createCell(26)
            .setStyle(cellStyle)
            .setValue(
                setScale(getComputedCc(quantity, factorA, io, useCalculation), DIVIDE_SCALE)
                    ?.toDouble()
            )
        row.createCell(27)
            .setStyle(cellStyle)
            .setValue(
                setScale(getComputedCCo2(quantity, factorA, io, useCalculation), DIVIDE_SCALE)
                    ?.toDouble()
            )
    }

    fun parseDataIntoRow(
        translations: Map<String?, String?>,
        tuples: List<Tuple>,
        selectedAnalysisParam: Long?,
        row: Row,
        cellStyle: XSSFCellStyle,
    ) {
        var mainTuple: Tuple? = null
        var secondaryTuple: Tuple? = null

        // maximum tuples is 2
        tuples.forEach {
            val analysisParamId = it.getBigDecimal("id_analysis_param")?.toLong()
            if (analysisParamId == selectedAnalysisParam) {
                mainTuple = it
            } else {
                secondaryTuple = it
            }
        }

        // values for computations of main tuple
        val quantity = mainTuple?.getBigDecimal("quantity")
        val io = mainTuple?.getBigDecimal("io")
        val factorA = mainTuple?.getBigDecimal("factor_a")
        val useCalculation = mainTuple?.getBigDecimal("use_calculation")
        val calculatedQ = calculateQuantity(quantity, io)

        // values for computations of secondary Tuple
        val secondaryFactorA = secondaryTuple?.getBigDecimal("factor_a")

        row.createCell(0).setStyle(cellStyle).setValue(mainTuple?.getString("plant_name"))
        row.createCell(1)
            .setStyle(cellStyle)
            .setValue(mainTuple?.getBigDecimal("id_quantity")?.toDouble())
        row.createCell(2)
            .setStyle(cellStyle)
            .setValue(mainTuple?.getBigDecimal("id_material")?.toDouble())
        row.createCell(3)
            .setStyle(cellStyle)
            .setValue(mainTuple?.getBigDecimal("id_analysis")?.toDouble())

        val dateFrom = mainTuple?.getLocalDateTime("date_from")
        row.createCell(4).setStyle(cellStyle).setValue(formatDateForExport(dateFrom))
        row.createCell(5).setStyle(cellStyle).setValue(dateFrom.format("yyyy-MM"))
        row.createCell(6)
            .setStyle(cellStyle)
            .setValue(translations["month.${dateFrom?.monthValue}"])

        row.createCell(7)
            .setStyle(cellStyle)
            .setValue(formatDateForExport(mainTuple?.getLocalDateTime("date_to")))
        row.createCell(8).setStyle(cellStyle).setValue(calculatedQ)
        row.createCell(9)
            .setStyle(cellStyle)
            .setValue(mainTuple?.getBigDecimal("quantity_uncertainty")?.toDouble())
        row.createCell(10)
            .setStyle(cellStyle)
            .setValue(mainTuple?.getBigDecimal("number_of_measurements")?.toDouble())
        row.createCell(11).setStyle(cellStyle).setValue(io?.toDouble())

        // analysis param section
        row.createCell(12).setStyle(cellStyle).setValue(mainTuple?.getString("ap_name"))
        row.createCell(13).setStyle(cellStyle).setScaledValue(factorA)
        row.createCell(14).setStyle(cellStyle).setScaledValue(mainTuple?.getBigDecimal("factor_b"))
        row.createCell(15).setStyle(cellStyle).setScaledValue(mainTuple?.getBigDecimal("factor_c"))
        // secondary analysis param
        row.createCell(16).setStyle(cellStyle).setValue(secondaryTuple?.getString("ap_name"))
        row.createCell(17).setStyle(cellStyle).setScaledValue(secondaryFactorA)

        row.createCell(18)
            .setStyle(cellStyle)
            .setValue(mainTuple?.getBigDecimal("analysis_uncertainty")?.toDouble())
        row.createCell(19)
            .setStyle(cellStyle)
            .setValue(formatDateForExport(mainTuple?.getLocalDateTime("valid_from")))

        row.createCell(20).setStyle(cellStyle).setValue(mainTuple?.getString("unit"))
        row.createCell(21).setStyle(cellStyle).setValue(mainTuple?.getString("units"))
        row.createCell(22).setStyle(cellStyle).setValue(mainTuple?.getString("material_name"))
        row.createCell(23).setStyle(cellStyle).setValue(mainTuple?.getString("bc_material_code"))

        row.createCell(24).setStyle(cellStyle).setValue(mainTuple?.getString("meter_id"))
        row.createCell(25).setStyle(cellStyle).setValue(mainTuple?.getString("meter_name"))
        row.createCell(26)
            .setStyle(cellStyle)
            .setValue(mainTuple?.getBigDecimal("meter_uncertainty")?.toDouble())
        row.createCell(27)
            .setStyle(cellStyle)
            .setValue(mainTuple?.getBigDecimal("meter_upper_range")?.toDouble())

        // computed values with quantity and selected factor
        row.createCell(28)
            .setStyle(cellStyle)
            .setValue(
                setScale(getComputedCc(quantity, factorA, io, useCalculation), DIVIDE_SCALE)
                    ?.toDouble()
            )
        row.createCell(29)
            .setStyle(cellStyle)
            .setValue(
                setScale(getComputedCCo2(quantity, factorA, io, useCalculation), DIVIDE_SCALE)
                    ?.toDouble()
            )

        // computed CC2 with quantity and analysis param to compare factor A
        row.createCell(30)
            .setStyle(cellStyle)
            .setValue(
                setScale(
                        getComputedCc(quantity, secondaryFactorA, io, useCalculation),
                        DIVIDE_SCALE
                    )
                    ?.toDouble()
            )
    }

    private fun getComputedCc(
        quantity: BigDecimal?,
        factorA: BigDecimal?,
        io: BigDecimal?,
        useCalculation: BigDecimal?
    ): BigDecimal? {
        if (factorA == null || io == null || useCalculation == null) {
            return null
        }
        return quantity?.multiply(factorA)?.multiply(io)?.multiply(useCalculation)
    }

    private fun getComputedCCo2(
        quantity: BigDecimal?,
        factorA: BigDecimal?,
        io: BigDecimal?,
        useCalculation: BigDecimal?
    ): BigDecimal? = getComputedCc(quantity, factorA, io, useCalculation)?.multiply(CO2_FORMULA)
}
