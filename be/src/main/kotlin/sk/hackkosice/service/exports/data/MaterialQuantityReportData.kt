package sk.esten.uss.gbco2.service.exports.data

import java.math.BigDecimal
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialDto
import sk.esten.uss.gbco2.service.exports.ExportConstants
import sk.esten.uss.gbco2.utils.appendChangeComputations
import sk.esten.uss.gbco2.utils.setScale

class MaterialQuantityReportData(
    val material: SimpleMaterialDto,
    private val sectionType: MaterialQuantityReportEnum,
    private val renderOptions: Map<String, Boolean?>? = null,
    private val scale: BigDecimal = BigDecimal(1)
) : Exportable {

    private var isEmpty: Boolean = true
    private var unitAbbr: String? = null
    private var values: MutableList<BigDecimal?> = MutableList(size = 19) { null }
    private var valuesLastYear: MutableList<BigDecimal?> = MutableList(size = 19) { null }

    fun hasToBeRendered(): Boolean {
        if (sectionType == MaterialQuantityReportEnum.PRODUCT_SECTION) {
            return true
        }
        if (renderOptions == null) {
            return !isEmpty
        }
        return when (sectionType) {
            MaterialQuantityReportEnum.INPUT_SECTION ->
                renderOptions["${MaterialQuantityReportEnum.INPUT_SECTION.value}-${material.id}"]
                    ?: false
            MaterialQuantityReportEnum.OUTPUT_SECTION ->
                renderOptions["${MaterialQuantityReportEnum.OUTPUT_SECTION.value}-${material.id}"]
                    ?: false
            else -> false
        }
    }

    fun addValue(index: Int, value: SummationResult?, valueOfLastYear: SummationResult? = null) {
        addValue(index, values, value)
        addValue(index, valuesLastYear, valueOfLastYear)
    }

    fun addValue(index: Int, value: BigDecimal?, valueOfLastYear: BigDecimal? = null) {
        addValue(index, values, value)
        addValue(index, valuesLastYear, valueOfLastYear)
    }

    private fun addValue(index: Int, values: MutableList<BigDecimal?>, value: SummationResult?) {
        value?.value?.let {
            var currentValue = values[index]
            currentValue = currentValue?.add(value.value) ?: value.value
            values[index] = currentValue
            if (index < 15) {
                isEmpty = false
            }
            if (value.unitAbbr != null && unitAbbr == null) {
                unitAbbr = value.unitAbbr
            }
        }
    }

    private fun addValue(index: Int, values: MutableList<BigDecimal?>, value: BigDecimal?) {
        value?.let {
            var currentValue = values[index]
            currentValue = currentValue?.add(value) ?: value
            values[index] = currentValue
            if (index < 15) {
                isEmpty = false
            }
        }
    }

    fun getValue(index: Int): BigDecimal? {
        return values[index]
    }

    fun getValueOfLastYear(index: Int): BigDecimal? {
        return valuesLastYear[index]
    }

    private fun computeYTDSum(values: MutableList<BigDecimal?>): BigDecimal? {
        val quantityValue = computeYTD(values)
        if (quantityValue.toDouble() == 0.0) {
            return null
        }
        return computeYTD(values)
    }

    private fun computeYTD(computationResults: MutableList<BigDecimal?>): BigDecimal =
        computationResults.map { it }.subList(3, 15).sumOf { it ?: 0.toBigDecimal() }.abs()

    override fun fillDataMap(translations: Map<String?, String?>, params: Any?): Map<String, Any?> {
        val m = HashMap<String, Any?>()
        m["energy"] = material.name
        if (!isEmpty) {
            m["year3"] = setScale(values[0]?.multiply(scale), ExportConstants.EXCEL_SCALE)
            m["year2"] = setScale(values[1]?.multiply(scale), ExportConstants.EXCEL_SCALE)
            m["year1"] = setScale(values[2]?.multiply(scale), ExportConstants.EXCEL_SCALE)

            m["jan"] = setScale(values[3]?.multiply(scale), ExportConstants.EXCEL_SCALE)
            m["feb"] = setScale(values[4]?.multiply(scale), ExportConstants.EXCEL_SCALE)
            m["mar"] = setScale(values[5]?.multiply(scale), ExportConstants.EXCEL_SCALE)
            m["apr"] = setScale(values[6]?.multiply(scale), ExportConstants.EXCEL_SCALE)
            m["may"] = setScale(values[7]?.multiply(scale), ExportConstants.EXCEL_SCALE)
            m["jun"] = setScale(values[8]?.multiply(scale), ExportConstants.EXCEL_SCALE)
            m["jul"] = setScale(values[9]?.multiply(scale), ExportConstants.EXCEL_SCALE)
            m["aug"] = setScale(values[10]?.multiply(scale), ExportConstants.EXCEL_SCALE)
            m["sep"] = setScale(values[11]?.multiply(scale), ExportConstants.EXCEL_SCALE)
            m["oct"] = setScale(values[12]?.multiply(scale), ExportConstants.EXCEL_SCALE)
            m["nov"] = setScale(values[13]?.multiply(scale), ExportConstants.EXCEL_SCALE)
            m["dec"] = setScale(values[14]?.multiply(scale), ExportConstants.EXCEL_SCALE)
            val thisYearResult = computeYTDSum(values)
            val lastYearResult = computeYTDSum(valuesLastYear)
            m["yearYTD"] = setScale(thisYearResult, ExportConstants.EXCEL_SCALE)
            m["year1YTD"] = setScale(lastYearResult, ExportConstants.EXCEL_SCALE)

            m.appendChangeComputations(translations, thisYearResult, lastYearResult)

            m["unitAbbr"] = unitAbbr
        }
        return m
    }
}
