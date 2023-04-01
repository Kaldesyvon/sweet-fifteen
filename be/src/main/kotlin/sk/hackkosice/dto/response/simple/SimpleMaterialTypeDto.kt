package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleMaterialTypeDto")
open class SimpleMaterialTypeDto() : BaseSimpleDto() {
    @Schema(description = "memo") var memo: String? = null

    @Schema(description = "code") var code: String? = null

    constructor(id: Long?, name: String?) : this() {
        this.id = id
        this.name = name
    }
}
