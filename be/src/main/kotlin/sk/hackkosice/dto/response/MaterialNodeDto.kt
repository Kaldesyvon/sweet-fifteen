package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.detail.MaterialNodeDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialNodeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeDto

@Schema(subTypes = [MaterialNodeDetailDto::class], description = "materialNodeDto")
open class MaterialNodeDto : SimpleMaterialNodeDto() {

    @Schema(description = "material") var material: SimpleMaterialDto? = null

    @Schema(description = "node") var node: SimpleNodeDto? = null

    @Schema(description = "objVersion") var objVersion: Long? = 0
}
