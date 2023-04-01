package sk.esten.uss.gbco2.service.exports.data

interface Exportable {
    fun fillDataMap(translations: Map<String?, String?>, params: Any?): Map<String, Any?>
}
