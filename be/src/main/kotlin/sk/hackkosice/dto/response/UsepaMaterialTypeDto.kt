package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Size
import sk.esten.uss.gbco2.dto.response.simple.SimpleUsepaMaterialTypeDto

@Schema(description = "usepaMaterialTypeDto")
class UsepaMaterialTypeDto : SimpleUsepaMaterialTypeDto() {

    @Size(max = 250)
    @Schema(description = "memo", nullable = true, maxLength = 250)
    var memo: String? = null

    @Schema(description = "objVersion", nullable = false, defaultValue = "0")
    var objVersion: Long? = 0
}
