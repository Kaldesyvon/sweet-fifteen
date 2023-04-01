package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import sk.esten.uss.gbco2.dto.response.detail.NodeDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeDto

@Schema(subTypes = [NodeDetailDto::class], description = "nodeDto")
open class NodeDto : SimpleNodeDto() {
    @Schema(description = "country") var country: CountryDto? = null

    @Schema(description = "parentNode") var parentNode: SimpleNodeDto? = null

    @Schema(description = "code") var code: String? = null

    @Schema(description = "memo") var memo: String? = null

    @Schema(description = "validFrom") var validFrom: LocalDateTime? = null

    @Schema(description = "validTo") var validTo: LocalDateTime? = null

    @Schema(description = "objVersion") var objVersion: Long? = null

    @Schema(description = "processId") var processId: String? = null
}
