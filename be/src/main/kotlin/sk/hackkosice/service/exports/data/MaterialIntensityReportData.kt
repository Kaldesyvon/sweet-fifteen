package sk.esten.uss.gbco2.service.exports.data

import java.math.BigDecimal
import java.math.RoundingMode
import sk.esten.uss.gbco2.service.exports.ExportConstants
import sk.esten.uss.gbco2.utils.appendChangeComputations
import sk.esten.uss.gbco2.utils.setScale

class MaterialIntensityReportData(private val name: String?, private var isEmpty: Boolean = true) :
    Exportable {

    private var analyticalValues: MutableList<BigDecimal?> = MutableList(size = 19) { null }
    private var quantityValues: MutableList<BigDecimal?> = MutableList(size = 19) { null }
    private var analyticalValuesLastYear: MutableList<BigDecimal?> = MutableList(size = 19) { null }
    private var quantityValuesLastYear: MutableList<BigDecimal?> = MutableList(size = 19) { null }

    fun hasToBeRendered(): Boolean {
        return !isEmpty
    }

    fun addAnalyticalValue(
        index: Int,
        value: BigDecimal? = null,
        valueLastYear: BigDecimal? = null
    ) {
        value?.let { addValue(index, analyticalValues, it) }

        // last year value
        valueLastYear?.let { addValue(index, analyticalValuesLastYear, it) }
    }

    fun addQuantityValues(
        quantityValues: MutableList<BigDecimal?>,
        quantityPrevYearValues: MutableList<BigDecimal?>
    ) {
        this.quantityValues = quantityValues
        this.quantityValuesLastYear = quantityPrevYearValues
    }

    fun getQuantityValues(prevYear: Boolean = false): MutableList<BigDecimal?> {
        return if (prevYear) quantityValuesLastYear else quantityValues
    }

    fun addQuantityValue(index: Int, value: BigDecimal? = null, valueLastYear: BigDecimal? = null) {
        value?.let { addValue(index, quantityValues, it) }

        // last year value
        valueLastYear?.let { addValue(index, quantityValuesLastYear, it) }
    }

    private fun addValue(index: Int, valuesList: MutableList<BigDecimal?>, value: BigDecimal) {
        var currentValue = valuesList[index]
        currentValue = currentValue?.add(value) ?: value
        valuesList[index] = currentValue
        if (index < 15) {
            isEmpty = false
        }
    }

    private fun computeIntensity(index: Int): BigDecimal? {
        if (analyticalValues[index] == null ||
                analyticalValues[index]?.toDouble() == 0.0 ||
                quantityValues[index] == null ||
                quantityValues[index]?.toDouble() == 0.0
        ) {
            return null
        }
        val result =
            analyticalValues[index]?.divide(
                (quantityValues[index]),
                ExportConstants.DIVIDE_SCALE,
                RoundingMode.HALF_UP
            )
        return result?.abs()
    }

    private fun computeYTDSum(
        analyticalValues: MutableList<BigDecimal?>,
        quantityValues: MutableList<BigDecimal?>
    ): BigDecimal? {
        val analyticalValue = computeYTD(analyticalValues)
        val quantityValue = computeYTD(quantityValues)
        if (analyticalValue.toDouble() == 0.0) {
            return null
        }
        if (quantityValue.toDouble() == 0.0) {
            return null
        }
        return analyticalValue.divide(
            (quantityValue),
            ExportConstants.DIVIDE_SCALE,
            RoundingMode.HALF_UP
        )
    }

    private fun computeYTD(computationResults: MutableList<BigDecimal?>): BigDecimal =
        computationResults.map { it }.subList(3, 15).sumOf { it ?: 0.toBigDecimal() }.abs()

    override fun fillDataMap(translations: Map<String?, String?>, params: Any?): Map<String, Any?> {
        val m = HashMap<String, Any?>()
        m["plantName"] = name
        m["year3"] = setScale(computeIntensity(0), ExportConstants.EXCEL_SCALE)
        m["year2"] = setScale(computeIntensity(1), ExportConstants.EXCEL_SCALE)
        m["year1"] = setScale(computeIntensity(2), ExportConstants.EXCEL_SCALE)

        m["jan"] = setScale(computeIntensity(3), ExportConstants.EXCEL_SCALE)
        m["feb"] = setScale(computeIntensity(4), ExportConstants.EXCEL_SCALE)
        m["mar"] = setScale(computeIntensity(5), ExportConstants.EXCEL_SCALE)
        m["apr"] = setScale(computeIntensity(6), ExportConstants.EXCEL_SCALE)
        m["may"] = setScale(computeIntensity(7), ExportConstants.EXCEL_SCALE)
        m["jun"] = setScale(computeIntensity(8), ExportConstants.EXCEL_SCALE)
        m["jul"] = setScale(computeIntensity(9), ExportConstants.EXCEL_SCALE)
        m["aug"] = setScale(computeIntensity(10), ExportConstants.EXCEL_SCALE)
        m["sep"] = setScale(computeIntensity(11), ExportConstants.EXCEL_SCALE)
        m["oct"] = setScale(computeIntensity(12), ExportConstants.EXCEL_SCALE)
        m["nov"] = setScale(computeIntensity(13), ExportConstants.EXCEL_SCALE)
        m["dec"] = setScale(computeIntensity(14), ExportConstants.EXCEL_SCALE)
        // compute YTD summaries
        val thisYearResult = computeYTDSum(analyticalValues, quantityValues)
        val lastYearResult = computeYTDSum(analyticalValuesLastYear, quantityValuesLastYear)
        m["year"] = setScale(thisYearResult, ExportConstants.EXCEL_SCALE)
        m["year1ytd"] = setScale(lastYearResult, ExportConstants.EXCEL_SCALE)

        // change computation of the YTD present and last year
        m.appendChangeComputations(translations, thisYearResult, lastYearResult)

        return m
    }
}
