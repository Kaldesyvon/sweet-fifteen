package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import sk.esten.uss.gbco2.dto.response.detail.MaterialDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialDto
import sk.esten.uss.gbco2.dto.response.tree.MaterialTreeDto

@Schema(subTypes = [MaterialDetailDto::class, MaterialTreeDto::class], description = "materialDto")
open class MaterialDto : SimpleMaterialDto() {

    @Schema(description = "memo") var memo: String? = null

    @Schema(description = "code") var code: String? = null

    @Schema(description = "objVersion") var objVersion: Long? = 0

    @Schema(description = "product") var product: Boolean? = null

    @Schema(description = "materialReport") var materialReport: Int? = null

    @Schema(
        description = "materialGroup",
    )
    var materialGroup: Boolean? = null

    @Schema(description = "unitType") var unitType: UnitTypeDto? = null

    @Schema(description = "usepaMaterialType") var usepaMaterialType: UsepaMaterialTypeDto? = null

    @Schema(description = "parentMaterial") var parentMaterial: SimpleMaterialDto? = null

    @Schema(description = "createdBy") var createdBy: String? = null

    @Schema(description = "created") var created: LocalDateTime? = null

    @Schema(description = "modifiedBy") var modifiedBy: String? = null

    @Schema(description = "modified") var modified: LocalDateTime? = null
}
