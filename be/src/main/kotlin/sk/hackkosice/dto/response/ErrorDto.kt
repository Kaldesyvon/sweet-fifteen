package sk.esten.uss.gbco2.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.exceptions.BusinessException

@Schema(description = "DTO for Exceptions")
class ErrorDto {
    @JsonProperty(value = "code")
    @Schema(description = "Error code", required = true)
    var code: ErrorCode? = null

    @JsonProperty(value = "description")
    @Schema(description = "Error description")
    var description: String? = null

    @JsonProperty(value = "parameter")
    @Schema(
        description = "Error parameters",
        example = "{\"key1\" : \"value1\", \"key2\" : \"value2\"}",
        type = "Map"
    )
    var parameters: Map<String, Any>? = null
}

/**
 *
 * transform instance of [BusinessException] to [ErrorDto]
 */
fun BusinessException.toErrorDto(): ErrorDto {
    val ex = this
    return ErrorDto().apply {
        code = ex.code
        description = ex.description
        parameters = ex.parameters
    }
}

enum class ErrorCode {
    INTERNAL_SERVER_ERROR,
    DATA_VALIDATION,
    FORBIDDEN,
    UNAUTHORIZED,
    DATABASE_ERROR,
    OPTIMISTIC_LOCK,
    NOT_FOUND,
    NOT_UNIQUE_KEY_VALUE,
    CANNOT_BE_UPDATED
}
