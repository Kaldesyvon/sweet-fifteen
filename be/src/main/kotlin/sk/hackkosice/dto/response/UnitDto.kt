package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Schema(description = "unitDto")
class UnitDto {
    @Schema(description = "id", nullable = false) var id: Long? = null

    @NotBlank
    @Size(max = 2000)
    @Schema(
        description =
            "Translated name, use 'nameTranslated' as column while sorting or for pagination",
        nullable = false,
        maxLength = 2000
    )
    var name: String? = null

    @Schema(description = "k", nullable = false) var k: BigDecimal? = null
    @Schema(description = "q", nullable = false) var q: BigDecimal? = null

    @NotBlank
    @Size(max = 20)
    @Schema(description = "code", nullable = false)
    var code: String? = null

    @NotBlank
    @Size(max = 20)
    @Schema(description = "abbr", nullable = false)
    var abbr: String? = null

    @Schema(description = "unitTypeId") var unitTypeId: Long? = null

    @Schema(description = "objVersion", nullable = false, defaultValue = "0")
    var objVersion: Long? = 0

    @Schema(description = "unitType", nullable = false) var unitType: UnitTypeDto? = null

    @get:Schema(description = "referenceUnit")
    val referenceUnit: Boolean
        get() = k != null && k?.toFloat() == 1f && q != null && q?.toFloat() == 0f
}
