package sk.esten.uss.gbco2.mapper

import java.util.*
import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateReportedIssueAttachmentDto
import sk.esten.uss.gbco2.dto.response.ReportedIssueAttDto
import sk.esten.uss.gbco2.model.entity.reported_issue.ReportedIssueAtt

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface ReportedIssueAttMapper {

    fun map(entity: ReportedIssueAtt): ReportedIssueAttDto

    fun mapList(list: Set<ReportedIssueAtt>): MutableList<ReportedIssueAttDto>
    fun createList(list: MutableList<CreateReportedIssueAttachmentDto>): Set<ReportedIssueAtt>

    @Mappings(Mapping(target = "id", ignore = true), Mapping(target = "issue", ignore = true))
    fun map(dto: CreateReportedIssueAttachmentDto): ReportedIssueAtt

    fun mapToByteArray(base64Icon: String?): ByteArray? {
        return base64Icon?.let { Base64.getDecoder().decode(it.substringAfterLast(",")) }
    }
}
