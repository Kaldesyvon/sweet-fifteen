package sk.esten.uss.gbco2.utils

import sk.esten.uss.gbco2.model.entity.dictionary.Dictionary

/** get translated value depends on principal's language or default (EN translation) */
fun mapTranslation(
    key: String?,
    trans: MutableMap<Long, Dictionary>?,
    translate: Boolean = true
): String {
    return trans
        ?.get(if (translate) principalLangOrEn().id else Constants.EN_LANG_ID)
        ?.translation
        ?.getTranslation()
        ?: "??? $key ???"
}

/** create new English translation */
fun newEnTranslation(
    key: String?,
    translation: String?,
    trans: MutableMap<Long, Dictionary>?,
) {
    key?.ifEmpty { null }?.let {
        trans?.put(
            Constants.EN_LANG_ID,
            Dictionary().apply {
                languageId = Constants.EN_LANG_ID
                this.key = it.removeDiacritics()
                this.translation =
                    if (translation.isNullOrEmpty()) Constants.EMPTY_DB_STRING_VALUE
                    else translation
            }
        )
    }
}

fun String?.getTranslation(): String? = if (this != Constants.EMPTY_DB_STRING_VALUE) this else null

/** update English translation */
fun updateEnTranslation(translation: String?, trans: MutableMap<Long, Dictionary>?) {
    val dictionary = trans?.get(Constants.EN_LANG_ID)
    dictionary?.translation =
        if (translation.isNullOrEmpty()) Constants.EMPTY_DB_STRING_VALUE else translation
}
