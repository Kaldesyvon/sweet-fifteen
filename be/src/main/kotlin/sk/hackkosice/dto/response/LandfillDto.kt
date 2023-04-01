package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate
import sk.esten.uss.gbco2.dto.response.simple.SimpleLandfillDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleUnitDto
import sk.esten.uss.gbco2.model.entity.landfill.LandfillStatus

@Schema(description = "landfillDto")
class LandfillDto : SimpleLandfillDto() {

    @Schema(description = "status") var status: LandfillStatus? = null

    @Schema(description = "openedDate") var openedDate: LocalDate? = null

    @Schema(description = "projectedCloseDate") var projectedCloseDate: LocalDate? = null

    @Schema(description = "capacity") var capacity: BigDecimal? = null

    @Schema(description = "unit") var unit: SimpleUnitDto? = null

    @Schema(description = "leachateRecirculation") var leachateRecirculation: Boolean? = false

    @Schema(description = "passiveVentsFlares") var passiveVentsFlares: Boolean? = false

    @Schema(description = "passiveVentsFlares") var landfillCoverDescription: String? = null

    @Schema(description = "note1") var note1: String? = null

    @Schema(description = "note2") var note2: String? = null

    @Schema(description = "note3") var note3: String? = null

    @Schema(description = "node") var node: SimpleNodeDto? = null

    @Schema(description = "objVersion", defaultValue = "0") var objVersion: Long? = 0
}
