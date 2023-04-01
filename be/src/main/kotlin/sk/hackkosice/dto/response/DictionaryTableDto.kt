package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema

class DictionaryTableDto {

    @Schema(description = "id") var id: Long? = null

    @Schema(description = "tableName") var tableName: String? = null
}
