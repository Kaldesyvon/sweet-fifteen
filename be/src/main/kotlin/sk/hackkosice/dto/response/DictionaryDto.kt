package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import sk.esten.uss.gbco2.dto.response.simple.SimpleLanguageDto

@Schema(description = "dictionaryDto")
class DictionaryDto {

    @Schema(description = "id", nullable = true) var id: Long? = null

    @NotBlank
    @Size(max = 80)
    @Schema(description = "key", nullable = false, maxLength = 80)
    var key: String? = null

    @NotBlank
    @Size(max = 2000)
    @Schema(description = "translation", nullable = false, maxLength = 2000)
    var translation: String? = null

    @Schema(description = "objVersion", nullable = false, defaultValue = "0")
    var objVersion: Long? = 0

    @NotNull
    @Schema(description = "language", nullable = false)
    var language: SimpleLanguageDto? = null
}
