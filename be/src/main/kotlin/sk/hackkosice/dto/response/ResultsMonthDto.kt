package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialNodeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeDto

@Schema(description = "resultsMonthDto")
class ResultsMonthDto {

    @Schema(description = "id") var id: Long? = null

    @Schema(description = "node") var node: SimpleNodeDto? = null

    @Schema(description = "material") var material: SimpleMaterialDto? = null

    @Schema(description = "materialNode") var materialNode: SimpleMaterialNodeDto? = null

    @Schema(description = "quantity") var quantity: BigDecimal? = null

    @Schema(description = "unitAbbr") var unitAbbr: String? = null

    @Schema(description = "uncertainty") var uncertainty: BigDecimal? = null
}
