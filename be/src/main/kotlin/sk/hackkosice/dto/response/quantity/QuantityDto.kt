package sk.esten.uss.gbco2.dto.response.quantity

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDateTime
import sk.esten.uss.gbco2.dto.response.detail.QuantityDetailDto
import sk.esten.uss.gbco2.dto.response.simple.*
import sk.esten.uss.gbco2.model.entity.quantity.IOEnum

@Schema(description = "quantityDto", subTypes = [QuantityDetailDto::class])
open class QuantityDto {
    @Schema(description = "id") var id: Long? = null

    @Schema(description = "node") var node: SimpleNodeDto? = null

    @Schema(description = "material") var material: SimpleMaterialDto? = null

    @Schema(description = "materialNode") var materialNode: SimpleMaterialNodeDto? = null

    @Schema(description = "dateFrom") var dateFrom: LocalDateTime? = null

    @Schema(description = "dateTo") var dateTo: LocalDateTime? = null

    @Schema(description = "quantity") var quantity: BigDecimal? = null

    @Schema(description = "unitToAbbr") var unitToAbbr: String? = null

    @Schema(description = "uncertainty") var uncertainty: BigDecimal? = null

    @Schema(description = "numberOfMeasurements") var numberOfMeasurements: Long? = null

    @Schema(description = "io") var io: IOEnum? = null

    @Schema(description = "autoAnalysis") var autoAnalysis: Boolean? = null

    @Schema(description = "analysisParam") var analysisParam: SimpleAnalysisParamDto? = null

    @Schema(description = "mdlA") var mdlA: String? = null

    @Schema(description = "formattedFactorA") var formattedFactorA: BigDecimal? = null

    @Schema(description = "formattedFactorB") var formattedFactorB: BigDecimal? = null

    @Schema(description = "formattedFactorC") var formattedFactorC: BigDecimal? = null

    @Schema(description = "formattedUnitAbbrTo") var formattedUnitAbbrTo: String? = null

    @Schema(description = "meter") var meter: SimpleMeterDto? = null

    @Schema(description = "remoteMaterialCode") var remoteMaterialCode: String? = null

    @Schema(description = "editable") var editable: Boolean? = null
    @Schema(description = "fromWs") var fromWs: Boolean? = null

    @Schema(name = "created") var created: LocalDateTime? = null
    @Schema(name = "createdBy") var createdBy: String? = null
    @Schema(name = "modified") var modified: LocalDateTime? = null
    @Schema(name = "modifiedBy") var modifiedBy: String? = null
}
