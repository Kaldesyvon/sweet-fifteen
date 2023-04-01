package sk.esten.uss.gbco2.service.exports

import java.math.MathContext

class ExportConstants {

    companion object {
        const val REPORT_LOGO_NAME = "ussGbco2Logo.jpg"

        const val POSITIVE_CHANGE_COLOR = "blue"
        const val NEGATIVE_CHANGE_COLOR = "red"
        const val DEFAULT_CHANGE_COLOR = "black"

        const val ALL_PLANTS = "0"
        const val NO_FILTER = "0"

        const val CORP_INTENSITIES_EXPORT_TEMPLATE_NAME = "corpResult"
        const val CORP_INTENSITIES_SPECIAL_EXPORT_TEMPLATE_NAME = "corpResultSpecial"
        const val CORP_INTENSITIES_EXPORT_DOWNLOAD_NAME = "Intensities"
        const val PLANT_CARBON_TEMPLATE_NAME = "plantCarbon"

        const val MATERIAL_QUANTITIES_TEMPLATE_NAME = "materialQuantities"

        const val GENERATION_EXPORT_TEMPLATE_NAME = "plantCarbon"
        const val GENERATION_EXPORT_DOWNLOAD_NAME = "GenerationExport"

        const val PROGNOSIS_TEMPLATE_NAME = "prognosis"

        const val SUMMARY_TEMPLATE_NAME = "summary"
        const val SUMMARY_EXPORT_DOWNLOAD_NAME = "summary"

        const val AUDIT_TEMPLATE_NAME = "audit"
        const val AUDIT_EXPORT_DOWNLOAD_NAME = "audit"

        const val SPECIAL_AUDIT_TEMPLATE_NAME = "special_audit"
        const val SPECIAL_AUDIT_EXPORT_DOWNLOAD_NAME = "special_audit"

        const val REPORT_BASE_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.S"

        const val EXCEL_SCALE = 7
        const val PDF_SCALE = 0
        const val DIVIDE_SCALE = 30
        val MATH_CONTEXT = MathContext(30)

        const val C_AP = "AP"
        const val C_ENERGY_COMBINED = "EC"

        const val C_ENERGY_SPECIAL = "ENERGY_SPECIAL"

        const val USS_TOTAL_REGION = 0L

        val CO2_FORMULA = 3.664.toBigDecimal()

        val summaryExcelTranslations =
            mutableListOf(
                "ussSteelCorporation",
                "summaryHeader",
                "plantName",
                "materialName",
                "id",
                "quantity",
                "unit",
                "dateFrom",
                "dateTo",
                "headerFactors",
                "aParamName",
                "factorA",
                "factorB",
                "units",
                "validFrom",
                "GBCO2Env",
                "createdBy",
                "corpCloseDate",
                "corporateLockDate",
                "calculation",
                "version",
                "paramCreatedBy"
            )

        val materialQuantityExcelTranslations =
            mutableListOf(
                "notApplicable",
                "sheetName",
                "materialQuantitiesHeader",
                "corpResultsReportHeader1",
                "corpResultsReportHeader2",
                "ussSteelCorporation",
                "paramNoteSpecial",
                "materialName",
                "total",
                "page",
                "and",
                "ytd",
                "facility",
                "Inputs",
                "outputs",
                "totalUSSteel",
                "unit",
                "products",
                "units",
                "material",
                "changeLabel",
                "pleaseSelectAtleastOnePlant",
                "pleaseSelectCalculation",
                "GBCO2Env",
                "createdBy",
                "corpCloseDate",
                "corporateLockDate",
                "corporateCheckDate",
                "note",
                "paramNoteCarbonReport",
                "YTDCalculationThrough",
                "plantCheckDate",
                "plantLockDate",
                "calculation",
                "paramCreatedBy"
            )

        val analysisParamProdExcelTranslations =
            mutableListOf(
                "notApplicable",
                "sheetName",
                "ussSteelCorporation",
                "plantCarbonHeader",
                "energy",
                "carbonSource",
                "materialName",
                "total",
                "page",
                "and",
                "ytd",
                "inputMinusOutputCarbonReport",
                "Inputs",
                "outputs",
                "totalUSSteel",
                "totalInput",
                "totalOutput",
                "unit",
                "changeLabel",
                "units",
                "units2",
                "changeLabel",
                "pleaseSelectAtleastOnePlant",
                "pleaseSelectCalculation",
                "GBCO2Env",
                "createdBy",
                "corpCloseDate",
                "corporateLockDate",
                "corporateCheckDate",
                "note",
                "paramNoteCarbonReport",
                "YTDCalculationThrough",
                "plantCheckDate",
                "plantLockDate",
                "calculation",
                "paramCreatedBy"
            )

        val intensitySpecialExcelTranslations =
            mutableListOf(
                "ussSteelCorporation",
                "corpResultsReportHeader2energySpecial",
                "GBCO2Env",
                "createdBy",
                "paramCreatedBy",
                "page",
                "note",
                "paramNoteSpecial",
                "exportFootNote",
                "YTDCalculationThrough",
                "corporateLockDate",
                "corporateCheckDate",
                "facility",
                "headerYear3Base",
                "ytd",
                "changeLabel",
                "notApplicable",
                "pleaseSelectAtleastOnePlant",
                "pleaseSelectAllCalculations",
                "pleaseSelectComponent",
                "totalUSSteel",
                "totalTubularOperations",
                "totalEuropeanOps",
                "totalNAFlatRollOps",
                "intensityReportSectionHeaderSpecialLim",
                "total",
                "intensityReportSectionHeaderSpecial2",
                "intensityReportSectionHeaderSummary",
                "intensityReportSectionHeaderSpecial1",
                "intensityReportSectionHeader",
                "totalIntegratedWoBFs",
                "totalCanadaIntegrated",
                "totalEuropeIntegrated",
                "totalUSIntegrated",
                "unspecifiedError",
                "totalIntegratedwBFs",
                "totalBFCokeInj",
                "totalCanadaCokeInj",
                "totalEuropeCokeInj",
                "totalUSCokeInj"
            )

        val auditReport =
            mutableListOf(
                "plantName",
                "id",
                "idMaterial",
                "idAnalysis",
                "dateFrom",
                "month",
                "monthName",
                "dateTo",
                "quantity",
                "quantityUncertainty",
                "numberOfMeasurements",
                "io",
                "aParamName",
                "factorA",
                "factorB",
                "factorC",
                "analysisUncertainty",
                "validFrom",
                "unit",
                "units",
                "materialName",
                "bcMaterialCode",
                "meterId",
                "meterName",
                "meterUncertainty",
                "meterUpperRange",
                "CC",
                "CCO2"
            )

        val exceptionMessagesTranslations =
            mutableListOf(
                "intensityReportSectionHeaderSummary",
                "intensityReportSectionHeader",
            )
    }
}
