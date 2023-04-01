package sk.esten.uss.gbco2.dto.response.quantity

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

@Schema(
    description =
        "quantityPageResponseDto - compromised of paged data & summary dto for currently filter applied"
)
class QuantityPageResponseDto(pageData: Page<QuantityDto>, sum: QuantitySummaryDto) {

    @Schema(description = "page content") var pageData: Page<QuantityDto>? = pageData

    @Schema(description = "sumDto") var sum: QuantitySummaryDto? = sum
}
