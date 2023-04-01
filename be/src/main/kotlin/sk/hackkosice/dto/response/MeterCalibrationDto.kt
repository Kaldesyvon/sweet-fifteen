package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import sk.esten.uss.gbco2.dto.response.simple.SimpleMeterDto

@Schema(description = "meterCalibrationDto")
class MeterCalibrationDto {
    @Schema(description = "id") var id: Long? = null

    @Schema(description = "memo") var memo: String? = null

    @Schema(description = "meter") var meter: SimpleMeterDto? = null

    @Schema(description = "calibrationBy") var calibrationBy: String? = null

    @Schema(
        description = "calibrationDate",
    )
    var calibrationDate: LocalDateTime? = null

    @Schema(description = "calibrationDescription") var calibrationDescription: String? = null

    @Schema(description = "correctiveActions") var correctiveActions: String? = null

    @Schema(description = "objVersion") var objVersion: Long? = 0

    @Schema(description = "createdBy", nullable = false) var createdBy: String? = null

    @Schema(description = "created", nullable = false) var created: LocalDateTime? = null

    @Schema(description = "modifiedBy", nullable = true) var modifiedBy: String? = null

    @Schema(description = "modified", nullable = true) var modified: LocalDateTime? = null
}
