package sk.esten.uss.gbco2.dto.response.detail

import io.swagger.v3.oas.annotations.media.Schema
import javax.persistence.OrderBy
import sk.esten.uss.gbco2.dto.response.*
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisParamDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeDto

@Schema(description = "scopeDetailDto")
class ScopeDetailDto : ScopeDto() {
    @OrderBy(value = "id")
    @Schema(description = "scopeMaterialNodes")
    var scopeMaterialNodes: List<ScopeMaterialNodeDto> = listOf()

    @OrderBy(value = "id")
    @Schema(description = "scopeDenominators")
    var scopeDenominators: List<SimpleMaterialDto>? = null

    @OrderBy(value = "id")
    @Schema(description = "analysis params")
    var scopeAnalysisParams: List<SimpleAnalysisParamDto>? = null

    @OrderBy(value = "id")
    @Schema(description = "nodes included")
    var nodesIncluded: List<SimpleNodeDto>? = null

    @OrderBy(value = "id")
    @Schema(description = "materials included")
    var materialsIncluded: List<SimpleMaterialDto> = listOf()
}
