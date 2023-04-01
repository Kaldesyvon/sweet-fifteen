package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDateTime
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialConversionDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialDto
import sk.esten.uss.gbco2.model.entity.quantity.IOEnum

@Schema(description = "materialConversionDto")
open class MaterialConversionDto : SimpleMaterialConversionDto() {

    @Schema(name = "io") var io: IOEnum? = null

    @Schema(name = "quantityCoefficient") var quantityCoefficient: BigDecimal? = null

    @Schema(name = "analysisCoefficient") var analysisCoefficient: BigDecimal? = null

    @Schema(name = "loadTabQuantity") var loadTabQuantity: String? = null

    @Schema(name = "loadTabAnalysis") var loadTabAnalysis: String? = null

    @Schema(name = "editable") var editable: Boolean? = null

    @Schema(name = "autoAnalysis") var autoAnalysis: Boolean? = null

    @Schema(name = "validTo") var validTo: LocalDateTime? = null

    @Schema(description = "simpleMaterialDto") var material: SimpleMaterialDto? = null

    @Schema(name = "objVersion") var objVersion: Long? = 0
}
