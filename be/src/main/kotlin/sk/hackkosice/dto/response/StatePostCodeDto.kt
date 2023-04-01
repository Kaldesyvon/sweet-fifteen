package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Size
import sk.esten.uss.gbco2.dto.response.simple.SimpleStatePostCodeDto

@Schema(description = "statePostCodeDto")
class StatePostCodeDto : SimpleStatePostCodeDto() {

    @Size(max = 50)
    @Schema(description = "countryName", nullable = true, maxLength = 50)
    var countryName: String? = null
}
