package sk.esten.uss.gbco2.dto.response.detail

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import sk.esten.uss.gbco2.dto.response.MaterialNodeAnalysisParamDto
import sk.esten.uss.gbco2.dto.response.MaterialNodeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialTypeDto

@Schema(description = "materialNodeDetailDto")
class MaterialNodeDetailDto : MaterialNodeDto() {

    @Schema(description = "materialNodeAnalysisParams")
    var materialNodeAnalysisParams: MutableList<MaterialNodeAnalysisParamDto> = mutableListOf()

    @Schema(description = "materialNodeTypes")
    var materialNodeTypes: MutableList<SimpleMaterialTypeDto> = mutableListOf()

    @Schema(description = "carbonMin") var carbonMin: BigDecimal? = null

    @Schema(description = "carbonMax") var carbonMax: BigDecimal? = null

    @Schema(description = "commonPipeParent") var commonPipeParent: Boolean? = null

    @Schema(description = "commonPipeId") var commonPipeId: String? = null

    @Schema(description = "note1") var note1: String? = null

    @Schema(description = "note2") var note2: String? = null

    @Schema(description = "note3") var note3: String? = null
}
