package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Schema(description = "paramDto")
class ParamDto {
    @NotNull @Schema(description = "id") var id: Long? = null

    @Schema(description = "dateValue", nullable = true) var dateValue: LocalDate? = null

    @Size(max = 500)
    @Schema(description = "value", nullable = true, maxLength = 500)
    var value: String? = null

    @NotNull @Schema(description = "editable", nullable = false) var editable: Boolean = false

    @NotBlank
    @Size(max = 40)
    @Schema(description = "code", nullable = false, maxLength = 40)
    var code: String? = null

    @Size(max = 2000)
    @Schema(description = "memo", nullable = true, maxLength = 2000)
    var memo: String? = null

    @Schema(description = "objVersion", nullable = false, defaultValue = "0")
    var objVersion: Long? = 0

    @Schema(description = "modified") var modified: LocalDateTime? = null

    @Schema(description = "modifiedBy") var modifiedBy: String? = null
}
