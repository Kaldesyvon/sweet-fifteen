package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleCountryDto")
open class SimpleCountryDto : BaseSimpleDto() {
    @Schema(description = "regionId") var regionId: Long? = null
}
