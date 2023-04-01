package sk.esten.uss.gbco2.service.exports.data

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import sk.esten.uss.gbco2.model.entity.material.VMaterialTranslated
import sk.esten.uss.gbco2.model.entity.node.VNodeTranslated

data class PrognosisXLSData(
    @Schema(description = "node") var node: VNodeTranslated? = null,
    @Schema(description = "material") var material: VMaterialTranslated? = null,
    @Schema(description = "avgQuantity") var avgQuantity: Double? = null,
    @Schema(description = "minQuality") var minQuantity: BigDecimal? = null,
    @Schema(description = "maxQuality") var maxQuantity: BigDecimal? = null,
    @Schema(description = "totalQuantity") var totalQuantity: BigDecimal? = null,
) {
    @Schema(description = "totalAnalyticalValue") var totalAnalyticalValue: BigDecimal? = null

    @Schema(description = "countQuantity") var countQuantity: Long? = null

    @Schema(description = "unitAVAbbr") var unitAbbrAV: String? = null

    @Schema(description = "unitAbbr") var unitAbbr: String? = null

    constructor(
        node: VNodeTranslated?,
        material: VMaterialTranslated?,
        avgQuantity: Double?,
        minQuantity: BigDecimal?,
        maxQuantity: BigDecimal?,
        totalQuantity: BigDecimal?,
        totalAnalyticalValue: BigDecimal?,
        countQuantity: Long?,
        unitAbbrAV: String?,
        unitAbbr: String?
    ) : this(node, material, avgQuantity, minQuantity, maxQuantity, totalQuantity) {
        this.totalAnalyticalValue = totalAnalyticalValue
        this.countQuantity = countQuantity
        this.unitAbbrAV = unitAbbrAV
        this.unitAbbr = unitAbbr
    }
}
