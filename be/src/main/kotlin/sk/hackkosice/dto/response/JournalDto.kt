package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "journalDto")
class JournalDto {
    @Schema(description = "id") var id: Long? = null

    @Schema(description = "detail", maxLength = 4000, nullable = false) var detail: String? = null

    @Schema(description = "journalType", nullable = false) var journalType: JournalTypeDto? = null

    @Schema(description = "iud", nullable = false, maxLength = 1) var iud: String? = null

    @Schema(description = "created", nullable = false) var created: LocalDateTime? = null

    @Schema(description = "createdBy", nullable = false) var createdBy: String? = null
}
