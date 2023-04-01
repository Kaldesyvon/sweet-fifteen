package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Schema(description = "businessUnitDto")
class BusinessUnitDto {

    @Schema(description = "id") var id: Long? = null

    @NotBlank
    @Size(max = 20)
    @Schema(description = "code", nullable = false, maxLength = 20)
    var code: String? = null

    @NotBlank
    @Size(max = 2000)
    @Schema(
        description =
            "Translated name, use 'nameTranslated' as column while sorting or for pagination",
        nullable = false,
        maxLength = 2000
    )
    var name: String? = null

    @Size(max = 250)
    @Schema(description = "memo", nullable = true, maxLength = 250)
    var memo: String? = null

    @Schema(description = "objVersion", nullable = false, defaultValue = "0")
    var objVersion: Long? = 0

    @Schema(description = "reportPosition", nullable = true) var reportPosition: Int? = null
}
