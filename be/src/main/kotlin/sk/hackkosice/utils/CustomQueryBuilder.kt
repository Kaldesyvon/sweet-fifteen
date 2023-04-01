package sk.esten.uss.gbco2.utils

/** builder wrapper class for creating custom queries with already included [params] map */
class CustomQueryBuilder(stringQuery: String) {

    var stringQuery: String = stringQuery.required()
        set(value) {
            field = value.required()
        }
        get() = field.trimSpaces()

    val params: MutableMap<String, Any?> = mutableMapOf()

    /**
     * plus operator function to append string to the query
     *
     * no whitespaces before/after are needed, they are added automatically
     *
     * DO NOT add direct values, instead of use [addParam] function!
     */
    operator fun plus(str: String) {
        stringQuery += " $str "
    }

    /** params binding function */
    fun addParam(paramName: String, parameterValue: Any?) {
        params[paramName] = parameterValue
    }

    fun appendIdsIsIn(
        ids: List<Long>?,
        tableAlias: String,
        columnAlias: String,
        definedQueryParam: String? = null
    ) {
        if (!ids.isNullOrEmpty()) {
            val listOfChunks = ids.chunked(999)
            plus("AND (")

            for ((index, idsSubList) in listOfChunks.iterator().withIndex()) {
                val queryParam =
                    definedQueryParam?.let { "${it}_${index}".replace(".", "_") }
                        ?: "${tableAlias}_${columnAlias}_$index".replace(".", "_")

                plus("$tableAlias.$columnAlias IN (:$queryParam)")
                addParam(queryParam, idsSubList)

                if (index + 1 != listOfChunks.size) {
                    plus("OR")
                }
            }
            plus(")")
        } else {
            plus("AND $tableAlias.$columnAlias IN (null)")
        }
    }

    private fun String.required() =
        this.also { require(it.trim().isNotEmpty()) { "The stringQuery cannot be blank" } }
}
