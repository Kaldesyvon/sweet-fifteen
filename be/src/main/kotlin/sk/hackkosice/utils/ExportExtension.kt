package sk.esten.uss.gbco2.utils

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import sk.esten.uss.gbco2.config.enums.EnvEnum
import sk.esten.uss.gbco2.dto.response.MaterialDto
import sk.esten.uss.gbco2.dto.response.simple.BaseSimpleDto
import sk.esten.uss.gbco2.model.entity.node_type.NodeTypeSuper
import sk.esten.uss.gbco2.model.entity.param.Param
import sk.esten.uss.gbco2.model.entity.unit_type.UnitTypeSuper
import sk.esten.uss.gbco2.service.ParamService
import sk.esten.uss.gbco2.service.exports.ExportConstants

fun setScale(value: BigDecimal?, scale: Int): BigDecimal? =
    value?.setScale(scale, RoundingMode.HALF_UP)

fun getMonthTranslationKeys(): List<String> {
    val monthKeys = mutableListOf<String>()
    for (i in 1..12) {
        monthKeys.add("month.$i")
    }
    return monthKeys
}

fun String?.createHeaderKey(): String? =
    this?.let { key ->
        "header${key.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}"
    }

fun HashMap<String, Any?>.appendFormattedEnvironmentData(
    translations: Map<String?, String?>,
    env: EnvEnum,
    version: String,
    buildTime: String
) {
    this["GBCO2Env"] = translations["GBCO2Env"]
    val formattedBuildTime =
        LocalDateTime.parse(buildTime, DateTimeFormatter.ISO_DATE_TIME)
            .format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(principal()?.currentTimeZone)
            )
    this["paramGBCO2Env"] =
        "${translations[env.key]} ${translations["version"]} $version $formattedBuildTime"
    this["createdBy"] = translations["createdBy"]
    val formattedCreatingTime =
        "${
            LocalDateTime.now(principal()?.currentTimeZone).format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
            )
        } ${principal()?.currentTimeZone?.id}"
    this["paramCreatedBy"] =
        String.format(
            translations["paramCreatedBy"] ?: "unknown",
            principalFullName(),
            formattedCreatingTime
        )
}

fun HashMap<String, Any?>.appendFormattedParamsData(
    translations: Map<String?, String?>,
    paramService: ParamService
) {
    this["text1"] = translations["corpCloseDate"]
    this["param1"] =
        paramService
            .getParamByCode(Param.CLOSE_DATE_USS)
            .dateValue
            .formatDateToMonthWithUserLocale()
    this["text2"] = translations["corporateLockDate"]
    this["param2"] =
        paramService.getParamByCode(Param.LOCK_DATE_USS).dateValue.formatDateToMonthWithUserLocale()
}

fun HashMap<String, Any?>.appendChangeComputations(
    translations: Map<String?, String?>,
    thisYearResult: BigDecimal?,
    lastYearResult: BigDecimal?
) {
    if (lastYearResult != null && thisYearResult != null) {
        if (lastYearResult.toDouble() == 0.0 || thisYearResult.toDouble() == 0.0) {
            this["change"] = translations["notApplicable"]
        } else if (lastYearResult.toDouble() != 0.0) {
            this["change"] =
                thisYearResult.subtract(lastYearResult).divide(lastYearResult, MathContext(30))
        }
    }
}

fun Long?.isIntegratedNodeType(): Boolean? {
    return if (this == NodeTypeSuper.INTEGRATED_TYPE_ID) {
        true
    } else {
        null
    }
}

fun String.parseKeyId(index: Int): Long? {
    val id = this.split('-')[index].let { it.substring(1, it.length - 1) }
    return if (id.isNotEmpty()) id.toLong() else null
}

fun Long?.isLimestoneNodeType(): Boolean {
    return this == NodeTypeSuper.LIMESTONE_TYPE_ID
}

fun <T : BaseSimpleDto> List<T>.getHeaderOfValues(translation: Map<String?, String?>): String {
    var value = ""
    this.forEachIndexed { i, a ->
        if (i > 0) {
            value += " ${translation["and"]} "
        }
        value += a.name?.uppercase()
    }
    return value
}

fun List<MaterialDto>.validateMaterialsProducts(materialCodes: List<String>, paramCode: String) {
    for (materialCode in materialCodes) {
        val material = this.findLast { it.code.equals(materialCode) }
        material?.let { m ->
            if (m.unitType?.id != UnitTypeSuper.MASS_TYPE_ID) {
                throw IllegalArgumentException(
                    "Material with code: $materialCode. Please check system settings parameter: $paramCode"
                )
            }
            val materialMatches = this.filter { it.id == m.id }
            if (materialMatches.size > 1) {
                throw IllegalArgumentException(
                    "Material cannot be used as denominator more than once. (material code: $materialCode) Please check system settings parameter: $paramCode"
                )
            }
        }
            ?: throw IllegalArgumentException(
                "Cannot find material with code: $materialCode. Please check system settings parameter: $paramCode"
            )
    }
}

fun calculateQuantity(quantity: BigDecimal?, io: BigDecimal?): Double? =
    setScale(quantity, ExportConstants.DIVIDE_SCALE)?.multiply(io)?.toDouble()

fun String?.formatForReport(): String = this ?: ""

fun Int?.rangeOfYears(yearCount: Int): List<Int> =
    this?.downTo(this.minus(yearCount - 1))?.toList() ?: listOf()

fun Cell.setValue(value: String?) {
    value?.let { this.setCellValue(it) }
}

fun Cell.setValue(value: LocalDateTime?) {
    value?.let { this.setCellValue(it) }
}

fun Cell.setValue(value: Double?) {
    value?.let { this.setCellValue(it) }
}

fun Cell.setValue(value: BigDecimal?) {
    value?.let { this.setCellValue(it.toDouble()) }
}

fun Cell.setScaledValue(value: BigDecimal?) {
    setScale(value, ExportConstants.DIVIDE_SCALE)?.let { this.setCellValue(it.toDouble()) }
}

fun Cell.setStyle(style: XSSFCellStyle): Cell {
    this.cellStyle = style
    return this
}
