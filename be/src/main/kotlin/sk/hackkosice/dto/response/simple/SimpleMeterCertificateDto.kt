package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "simpleMeterCertificateDto")
open class SimpleMeterCertificateDto : BaseSimpleDto() {
    @Schema(description = "validTo") var validTo: LocalDate? = null
}
