package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateReportedIssueDto
import sk.esten.uss.gbco2.dto.response.ReportedIssueDto
import sk.esten.uss.gbco2.dto.response.detail.ReportedIssueDetailDto
import sk.esten.uss.gbco2.model.entity.reported_issue.ReportedIssue

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [ReportedIssueAttMapper::class]
)
interface ReportedIssueMapper {

    fun map(entity: ReportedIssue): ReportedIssueDto
    fun mapDetail(entity: ReportedIssue): ReportedIssueDetailDto

    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(target = "fixedBy", ignore = true),
        Mapping(target = "fixedAt", ignore = true),
        Mapping(target = "fixedComment", ignore = true),
        Mapping(target = "createdBy", ignore = true),
        Mapping(target = "createdAt", ignore = true),
        Mapping(target = "state", ignore = true),
    )
    fun map(dto: CreateReportedIssueDto): ReportedIssue
}
