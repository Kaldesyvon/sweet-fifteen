package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "reportedIssueAttDto")
class ReportedIssueAttDto {
    @Schema(description = "id") var id: Long? = null
    @Schema(description = "name") var name: String? = null
}
