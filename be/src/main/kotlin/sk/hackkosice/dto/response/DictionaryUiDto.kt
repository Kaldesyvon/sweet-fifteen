package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "dictionaryUiDto")
class DictionaryUiDto {
    @Schema(description = "languageId") var languageId: Long? = null

    @Schema(description = "key") var key: String? = null

    @Schema(description = "translation") var translation: String? = null
}
