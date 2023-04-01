package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import sk.esten.uss.gbco2.dto.response.detail.ScopeDetailDto

@Schema(subTypes = [ScopeDetailDto::class], description = "scopeDto")
open class ScopeDto {
    // TODO create createScopeDto from that and after delete attributes waiting task 2831

    @Schema(description = "id") var id: Long? = null

    @NotBlank
    @Size(max = 2000)
    @Schema(description = "name", nullable = false, maxLength = 2000)
    var name: String? = null

    @NotNull
    @Schema(description = "objVersion", nullable = false, defaultValue = "0")
    var objVersion: Long? = 0

    @Schema(description = "memo", nullable = true) var memo: String? = null

    @Schema(description = "isAdminOnly", nullable = true) var isAdminOnly: Boolean? = true

    @Size(max = 2000)
    @Schema(description = "scopeProcessSpec", nullable = true)
    var scopeProcessSpec: ScopeProcessSpecDto? = null

    @Size(max = 2000)
    @Schema(description = "scopeTypeSpec", nullable = true)
    var scopeTypeSpec: ScopeTypeSpecDto? = null

    @Size(max = 2000)
    @Schema(description = "scopeFuelSpec", nullable = true)
    var scopeFuelSpec: ScopeFuelSpecDto? = null

    @Schema(description = "materialNodes count") var materialNodesCount: Int = 0

    @Schema(description = "denominators count") var denominatorsCount: Int = 0
}
