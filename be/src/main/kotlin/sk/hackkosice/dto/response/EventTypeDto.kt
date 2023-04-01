package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "eventTypeDto")
class EventTypeDto {

    @Schema(description = "id") var id: Long? = null

    @Schema(description = "detailTranslated") var detailTranslated: String? = null

    @Schema(description = "textTranslated") var textTranslated: String? = null

    @Schema(description = "eventLevel") var eventLevel: Int? = null
}
