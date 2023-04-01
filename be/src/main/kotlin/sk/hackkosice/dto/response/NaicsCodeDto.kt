package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.simple.SimpleNaicsCodeDto

@Schema(description = "NaicsCodeDto")
class NaicsCodeDto : SimpleNaicsCodeDto() {

    @Schema(description = "description") var description: String? = null
}
