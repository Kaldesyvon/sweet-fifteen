package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotNull

@Schema(description = "yearDto")
class YearDto {

    @NotNull @Schema(description = "id", nullable = false) var year: Short? = null
}
