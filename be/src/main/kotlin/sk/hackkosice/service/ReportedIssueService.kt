package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateReportedIssueDto
import sk.esten.uss.gbco2.dto.request.filter.ReportedIssueFilter
import sk.esten.uss.gbco2.dto.response.ReportedIssueDto
import sk.esten.uss.gbco2.dto.response.detail.ReportedIssueDetailDto
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.mapper.ReportedIssueMapper
import sk.esten.uss.gbco2.model.entity.reported_issue.ReportedIssue
import sk.esten.uss.gbco2.model.entity.reported_issue.ReportedIssueAtt
import sk.esten.uss.gbco2.model.repository.reported_issue.ReportedIssueAttRepository
import sk.esten.uss.gbco2.model.repository.reported_issue.ReportedIssueRepository
import sk.esten.uss.gbco2.model.specification.ReportedIssueParamSpecification

@Service
class ReportedIssueService(
    override val entityRepository: ReportedIssueRepository,
    private val reportedIssueAttRepository: ReportedIssueAttRepository,
    private val mapper: ReportedIssueMapper,
) :
    CrudService<
        ReportedIssue,
        ReportedIssueDto,
        ReportedIssueDetailDto,
        CreateReportedIssueDto,
        ReportedIssueDto,
        Long,
        ReportedIssueFilter,
        ReadAllParamsFilterDto>() {

    override fun createEntity(createDto: CreateReportedIssueDto): ReportedIssue {
        val entity = mapper.map(createDto)
        entity.attachments.forEach { it.issue = entity }
        return entity
    }

    override fun updateEntity(updateDto: ReportedIssueDto, entity: ReportedIssue) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    override fun repositoryPageQuery(filter: ReportedIssueFilter): Page<ReportedIssue> {
        val specification = ReportedIssueParamSpecification(filter)
        return entityRepository.findAll(specification, filter.toPageable())
    }

    override fun entityToDetailDto(
        entity: ReportedIssue,
        translated: Boolean
    ): ReportedIssueDetailDto {
        return mapper.mapDetail(entity)
    }

    override fun entityToDto(entity: ReportedIssue, translated: Boolean): ReportedIssueDto {
        return mapper.map(entity)
    }

    fun getAttachmentById(id: Long): ReportedIssueAtt? {
        return reportedIssueAttRepository.findByIdOrNull(id)
    }
}
