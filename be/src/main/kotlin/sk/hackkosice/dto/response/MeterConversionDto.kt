package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.simple.SimpleMeterDto

@Schema(description = "meterConversionDto")
class MeterConversionDto {
    @Schema(description = "id") var id: Long? = null
    @Schema(description = "meterCode") var meterCode: String? = null
    @Schema(description = "meter") var meter: SimpleMeterDto? = null
    @Schema(description = "memo") var memo: String? = null
    @Schema(description = "objVersion", nullable = false, defaultValue = "0")
    var objVersion: Long? = 0
}
