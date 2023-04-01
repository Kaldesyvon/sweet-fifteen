package sk.esten.uss.gbco2.dto.response.detail

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.MaterialDto
import sk.esten.uss.gbco2.dto.response.UnitSetSettingsDto

@Schema(description = "materialDetailDto")
class MaterialDetailDto : MaterialDto() {
    @Schema(description = "unitSetSettings")
    var unitSetSettings: List<UnitSetSettingsDto>? = listOf()
}
