package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeDto

@Schema(description = "nodeDatalockDto")
class NodeDatalockDto {

    @Schema(description = "id") var id: Long? = null

    @Schema(description = "node") var node: SimpleNodeDto? = null

    @Schema(description = "lockDate") var lockDate: LocalDateTime? = null

    @Schema(description = "lockedBy") var lockedBy: String? = null

    @Schema(description = "lockedDate") var lockedDate: LocalDateTime? = null

    @Schema(description = "checkDate") var checkDate: LocalDateTime? = null

    @Schema(description = "checkedBy") var checkedBy: String? = null

    @Schema(description = "checkedDate") var checkedDate: LocalDateTime? = null

    @Schema(description = "auditDate") var auditDate: LocalDateTime? = null

    @Schema(description = "auditedBy") var auditedBy: String? = null

    @Schema(description = "auditedDate") var auditedDate: LocalDateTime? = null

    @Schema(description = "objVersion") var objVersion: Long? = null
}
