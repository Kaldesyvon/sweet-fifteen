package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Schema(description = "unitTypeDto")
class UnitTypeDto {

    @Schema(description = "id") var id: Long? = null

    @NotNull
    @NotBlank
    @Size(max = 2000)
    @Schema(
        description =
            "Translated name, use 'nameTranslated' as column while sorting or for pagination",
        nullable = false,
        maxLength = 2000
    )
    var name: String? = null

    @Size(max = 2000)
    @Schema(description = "memo", nullable = true, maxLength = 2000)
    var memo: String? = null

    @Schema(description = "quantity", nullable = true) var quantity: Boolean? = false

    @Schema(description = "analysis", nullable = true) var analysis: Boolean? = false

    @Schema(description = "compute", nullable = true) var compute: Boolean? = true

    @Schema(description = "objVersion", nullable = false, defaultValue = "0")
    var objVersion: Long? = 0
}
