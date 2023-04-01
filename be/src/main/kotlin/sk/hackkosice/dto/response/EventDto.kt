package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "eventDto")
class EventDto {

    @Schema(description = "id") var id: Long? = null

    @Schema(description = "eventDate") var eventDate: LocalDateTime? = null

    @Schema(description = "eventType") var eventType: EventTypeDto? = null

    @Schema(description = "method") var method: String? = null

    @Schema(description = "params") var params: String? = null
}
