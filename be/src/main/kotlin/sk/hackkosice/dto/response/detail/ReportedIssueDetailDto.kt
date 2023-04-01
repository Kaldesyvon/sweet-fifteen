package sk.esten.uss.gbco2.dto.response.detail

import io.swagger.v3.oas.annotations.media.Schema
import sk.esten.uss.gbco2.dto.response.ReportedIssueAttDto
import sk.esten.uss.gbco2.dto.response.ReportedIssueDto

@Schema(description = "reportedIssueDetailDto")
class ReportedIssueDetailDto : ReportedIssueDto() {

    var attachments: MutableList<ReportedIssueAttDto> = mutableListOf()
}
