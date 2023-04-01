package sk.esten.uss.gbco2.dto.response.simple

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "simpleMaterialConversionDto")
open class SimpleMaterialConversionDto {

    @Schema(description = "id") var id: Long? = null

    @Schema(description = "bcMaterialName") var bcMaterialName: String? = null

    @Schema(description = "bcMaterialCode") var bcMaterialCode: String? = null
}
