package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate

open class PlantResultDto(
    @Schema(description = "month", nullable = false) var month: LocalDate? = null,
    @Schema(description = "monthValue", nullable = false) var monthValue: BigDecimal? = null,
    @Schema(description = "nodeId", nullable = false) var nodeId: Long? = null,
    @Schema(description = "unitAbbrAv", nullable = false) var unitAbbrAv: String? = null,
)
