package sk.esten.uss.gbco2.service.exports.data

enum class MaterialQuantityReportEnum(val id: Int, val value: String, val io: Int) {
    INPUT_SECTION(0, "inputsData", 1),
    OUTPUT_SECTION(1, "outputsData", -1),
    PRODUCT_SECTION(2, "productsData", -1),
    TOTAL_IN_OUT_SECTION(1, "totalInOut", 1),
    TOTAL_IN_SECTION(2, "totalInput", 1),
    TOTAL_OUT_SECTION(3, "totalOutput", -1),
}
