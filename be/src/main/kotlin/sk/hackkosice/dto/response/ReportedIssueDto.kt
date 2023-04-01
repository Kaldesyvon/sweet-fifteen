package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import sk.esten.uss.gbco2.dto.response.detail.ReportedIssueDetailDto
import sk.esten.uss.gbco2.model.entity.reported_issue.RIPriorityEnum
import sk.esten.uss.gbco2.model.entity.reported_issue.RIStateEnum
import sk.esten.uss.gbco2.model.entity.reported_issue.RITypeEnum

@Schema(description = "reportedIssueDto", subTypes = [ReportedIssueDetailDto::class])
open class ReportedIssueDto {
    @Schema(description = "id") var id: Long? = null
    @Schema(description = "screen") var screen: String? = null
    @Schema(description = "type") var type: RITypeEnum? = null
    @Schema(description = "priority") var priority: RIPriorityEnum? = null
    @Schema(description = "state") var state: RIStateEnum? = null
    @Schema(description = "issueComment") var issueComment: String? = null
    @Schema(description = "createdBy") var createdBy: String? = null
    @Schema(description = "createdAt") var createdAt: LocalDateTime? = null
    @Schema(description = "fixedBy") var fixedBy: String? = null
    @Schema(description = "fixedAt") var fixedAt: LocalDateTime? = null
    @Schema(description = "fixedComment") var fixedComment: String? = null
}
