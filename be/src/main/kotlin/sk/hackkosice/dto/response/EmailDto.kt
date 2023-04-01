package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Schema(description = "emailDto")
class EmailDto {
    @Schema(description = "id", nullable = true) var id: Long? = null

    @NotBlank
    @Size(max = 50)
    @Schema(description = "email", nullable = false, maxLength = 50)
    var email: String? = null

    @Size(max = 20)
    @Schema(description = "login, use AD login", nullable = false, maxLength = 20)
    var login: String? = null

    @NotBlank
    @Size(max = 4000)
    @Schema(description = "subject", nullable = false, maxLength = 4000)
    var subject: String? = null

    @NotNull @Schema(description = "sent", nullable = false) var sent: LocalDateTime? = null

    @Schema(description = "body", nullable = true) var body: String? = null

    @Size(max = 50)
    @Schema(description = "attachmentName", nullable = true, maxLength = 50)
    var attachmentName: String? = null

    @Schema(
        description = "hasAttachment",
        nullable = false,
    )
    var hasAttachment: Boolean? = null
}
