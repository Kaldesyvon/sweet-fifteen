package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.model.entity.quantity.IOEnum

@Schema(description = "scopeMaterialNodeDataDto")
class ScopeMaterialNodeDataDto {
    @Schema(description = "materialNodeId", nullable = true) var materialNodeId: Long? = null

    @Schema(description = "io", nullable = true) var io: IOEnum? = null

    @Schema(description = "useInCalculation", nullable = true) var useInCalculation: Boolean? = null

    @Schema(description = "useAs", nullable = true) var useAs: IOEnum? = null

    @Schema(description = "materialId", nullable = true) var materialId: Long? = null

    @Schema(description = "nodeId", nullable = true) var nodeId: Long? = null
}
