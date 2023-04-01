package sk.esten.uss.gbco2.service.exports.data

import sk.esten.uss.gbco2.dto.response.simple.BaseSimpleDto

class MaterialIntensityReportSection() : Exportable {
    constructor(
        sectionName: String,
        materialValues: MutableList<MaterialIntensityReportData>,
        sectionParam: String? = null
    ) : this() {
        this.materialValues = materialValues.filter { it.hasToBeRendered() }.toMutableList()
        this.sectionName = sectionName
        sectionParam?.let { this.sectionName = "$it ${sectionType.name}" }
    }

    private lateinit var sectionType: BaseSimpleDto
    private lateinit var materialValues: MutableList<MaterialIntensityReportData>
    private var sectionName: String = ""
    fun hasToBeRendered(): Boolean {
        return materialValues.isNotEmpty()
    }

    override fun fillDataMap(translations: Map<String?, String?>, params: Any?): Map<String, Any?> {
        val m = HashMap<String, Any?>()
        m["header"] = sectionName
        m["data"] =
            materialValues.filter { it.hasToBeRendered() }.map {
                it.fillDataMap(translations, params)
            }
        return m
    }
}
