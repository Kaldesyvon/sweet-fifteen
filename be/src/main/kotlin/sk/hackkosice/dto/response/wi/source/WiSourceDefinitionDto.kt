package sk.esten.uss.gbco2.dto.response.wi.source

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import sk.esten.uss.gbco2.model.entity.quantity.IOEnum

@Schema(description = "wiSourceDefinitionDto")
class WiSourceDefinitionDto {
    @Schema(description = "idMaterialNode") var idMaterialNode: Long? = null
    @Schema(description = "idMaterial") var idMaterial: Long? = null
    @Schema(description = "materialName") var materialName: String? = null
    @Schema(description = "io") var io: IOEnum? = null

    @Schema(description = "idAnalysisParam") var idAnalysisParam: Long? = null
    @Schema(description = "analysisParamName") var analysisParamName: String? = null
    @Schema(description = "analyticalValue") var analyticalValue: BigDecimal? = null
    @Schema(description = "abbrav") var abbrav: String? = null

    @Schema(description = "quantityProduces") var quantityProduces: BigDecimal? = null
    @Schema(description = "abbrqp") var abbrqp: String? = null
}
