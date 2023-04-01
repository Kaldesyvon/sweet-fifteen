package sk.esten.uss.gbco2.utils

import org.springframework.data.domain.Sort

class Constants {
    companion object {

        const val MAX_AD_ACCESS_TOKEN_LIFETIME_IN_MUNUTES =
            90 + 5 // 90min (max. default from MS) + 5 min more

        const val DEFAULT_PAGE = 0
        const val DEFAULT_PAGE_SIZE = 10
        val DEFAULT_SORT_DIRECTION = Sort.Direction.ASC

        const val EN_LANG_ID = 1L
        const val SK_LANG_ID = 2L

        const val METRIC_UNIT_SET_ID = 1L
        const val US_UNIT_SET_ID = 2L

        const val ADV_MONTHS_BACK = 12L
        const val RUN_ADV_METHOD = "runAutoDataValidation"
        const val RUN_AUTO_ASSIGNMENT_METHOD = "runAutomaticAssignment"
        const val ADV_DELIMTER = "[+\\\\-*/\\\\^\\\\(\\\\) ]+"

        const val ATTEMPT_LOGIN_NAME = "attempt"

        const val MAPPING_COMPONENT_MODEL_SPRING_LAZY = "springLazy"

        const val EMPTY_DB_STRING_VALUE = "''"
    }
}
