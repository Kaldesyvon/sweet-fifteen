package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "simpleAnalysisParamDto")
open class SimpleAnalysisParamDto() : BaseSimpleDto() {
    @Schema(description = "code") var code: String? = null

    @Schema(description = "symbol") var symbol: String? = null

    @Schema(description = "mass") var mass: BigDecimal? = null

    @Schema(description = "casNo") var casNo: Long? = null

    @Schema(description = "memo") var memo: String? = null

    @Schema(description = "unitTypeId") var unitTypeId: Long? = null

    constructor(id: Long?, name: String?) : this() {
        this.id = id
        this.name = name
    }
}
