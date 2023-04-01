package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.simple.SimpleJournalTypeDto

@Schema(description = "journalTypeDto")
class JournalTypeDto : SimpleJournalTypeDto() {
    @Schema(description = "memo") var memo: String? = null
}
