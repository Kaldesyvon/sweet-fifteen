package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

abstract class BaseSimpleDto {
    @Schema(description = "id") var id: Long? = null

    @Schema(description = "name") var name: String? = null
}
