package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class CountryDto {

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

    @Schema(description = "reportPos", nullable = true, minimum = "0")
    @Min(0)
    var reportPos: Long? = 0

    @Size(max = 250)
    @Schema(description = "memo", nullable = true, maxLength = 250)
    var memo: String? = null

    @Schema(description = "objVersion", nullable = false, defaultValue = "0")
    var objVersion: Long? = 0

    @Schema(description = "region", nullable = false) var region: RegionDto? = null

    @Schema(description = "regionId") var regionId: Long? = null
}
