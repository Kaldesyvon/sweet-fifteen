package sk.esten.uss.gbco2.dto.response.emission_factor

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

@Schema(
    description =
        "emissionFactorDto - compromised of paged data & summary dto for currently filter applied"
)
class EmissionFactorDto(pageData: Page<EmissionFactorDataDto>, sum: EmissionFactorSumDto) {

    @Schema(description = "page content") var pageData: Page<EmissionFactorDataDto>? = pageData

    @Schema(description = "sumDto") var sum: EmissionFactorSumDto? = sum
}
