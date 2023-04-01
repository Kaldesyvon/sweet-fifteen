package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Schema(description = "menuDto")
class MenuDto {
    @Schema(description = "id", nullable = false) var id: Long? = null

    @Schema(description = "parentId", nullable = true) var parentId: Long? = null

    @NotBlank
    @Size(max = 2000)
    @Schema(
        description =
            "Translated name, use 'nameTranslated' as column while sorting or for pagination",
        nullable = false,
        maxLength = 2000
    )
    var name: String? = null

    @Size(max = 500)
    @Schema(description = "memo", nullable = true, maxLength = 500)
    var memo: String? = null

    @NotBlank
    @Size(max = 250)
    @Schema(description = "url", nullable = false, maxLength = 40)
    var url: String? = null

    @Schema(description = "menuOrder", nullable = false) var menuOrder: Int? = null

    @Schema(description = "subMenu", nullable = false) var subMenu: List<MenuDto>? = mutableListOf()
}
