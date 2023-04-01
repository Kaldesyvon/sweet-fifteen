package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Schema(description = "meterCertificateDto")
class MeterCertificateDto {
    @Schema(description = "id") var id: Long? = null

    @NotBlank
    @Size(max = 2000)
    @Schema(
        description =
            "Translated name, use 'nameTranslated' as column while sorting or for pagination",
        nullable = false,
        maxLength = 200
    )
    var name: String? = null

    @Size(max = 250) @Schema(description = "memo", nullable = true) var memo: String? = null

    @Size(max = 250)
    @Schema(description = "document name", nullable = true)
    var documentName: String? = null

    @Size(max = 250)
    @Schema(description = "document content type", nullable = true)
    var documentContentType: String? = null

    @NotNull @Schema(description = "meter id", nullable = false) var meterId: Long? = null

    @NotNull @Schema(description = "validFrom", nullable = false) var validFrom: LocalDate? = null

    @Schema(description = "validTo", nullable = true) var validTo: LocalDate? = null

    @NotNull @Schema(description = "objVersion", nullable = false) var objVersion: Long = 0
}
