package sk.esten.uss.gbco2.service.exports.data

import javax.persistence.Tuple
import sk.esten.uss.gbco2.service.exports.ExportConstants
import sk.esten.uss.gbco2.utils.*

open class SummaryReportData(private val tuple: Tuple) : Exportable {
    override fun fillDataMap(translations: Map<String?, String?>, params: Any?): Map<String, Any?> {
        val m = HashMap<String, Any?>()

        val quantity = tuple.getBigDecimal("quantity")
        val io = tuple.getBigDecimal("io")
        val calculatedQ = calculateQuantity(quantity, io)

        m["plantName"] = tuple.getString("plant_name")
        m["materialName"] = tuple.getString("material_name")
        m["id"] = tuple.getBigDecimal("id_quantity")?.toDouble()
        m["quantity"] = calculatedQ
        m["unit"] = tuple.getString("unit")
        m["dateFrom"] = formatDateForExport(tuple.getLocalDateTime("date_from"))
        m["dateTo"] = formatDateForExport(tuple.getLocalDateTime("date_to"))
        m["factorName"] = tuple.getString("ap_name")
        m["factorA"] = setScale(tuple.getBigDecimal("factor_a"), ExportConstants.DIVIDE_SCALE)
        m["factorB"] = setScale(tuple.getBigDecimal("factor_b"), ExportConstants.DIVIDE_SCALE)
        m["units"] = tuple.getString("units")
        m["validFrom"] = formatDateForExport(tuple.getLocalDateTime("valid_from"))

        return m
    }
}
