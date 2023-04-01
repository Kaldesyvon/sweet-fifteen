package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleUnitDto

@Schema(description = "unitSetSettingsDto")
class UnitSetSettingsDto {
    @Schema(description = "id") var id: Long? = null

    @Schema(description = "unit", nullable = false) var unit: SimpleUnitDto? = null
    @Schema(description = "unitSet", nullable = false) var unitSet: UnitSetDto? = null
    @Schema(description = "material", nullable = false) var material: SimpleMaterialDto? = null

    @Schema(description = "objVersion", nullable = false, defaultValue = "0")
    var objVersion: Long? = 0
}
