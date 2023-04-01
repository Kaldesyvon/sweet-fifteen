package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

class PlantResultResponseDto {

    @Schema(description = "totalValue") var totalValue: BigDecimal? = null

    @Schema(description = "totalResults") var totalResults: List<PlantResultDto>? = null
}
