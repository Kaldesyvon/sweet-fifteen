package sk.esten.uss.gbco2.service

import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.WiAnalysisFilter
import sk.esten.uss.gbco2.dto.request.filter.WiSourceDefinitionFilterDto
import sk.esten.uss.gbco2.dto.response.detail.WiAnalysisDetailDto
import sk.esten.uss.gbco2.dto.response.wi.analysys.WiAnalysisDto
import sk.esten.uss.gbco2.dto.response.wi.source.WiSourceDefinitionDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.WiAnalysisMapper
import sk.esten.uss.gbco2.model.entity.wi.analysis.VWiAnalysisTranslated
import sk.esten.uss.gbco2.model.entity.wi.analysis.WiAnalysis
import sk.esten.uss.gbco2.model.repository.wi.AdvWiAnalysisRepository
import sk.esten.uss.gbco2.model.repository.wi.analysis.WiAnalysisERepository
import sk.esten.uss.gbco2.model.repository.wi.analysis.WiAnalysisVRepository
import sk.esten.uss.gbco2.model.specification.WiAnalysisSpecification

@Service
class WiAnalysisService(
    override val entityRepository: WiAnalysisERepository,
    override val viewRepository: WiAnalysisVRepository,
    private val advWiAnalysisRepository: AdvWiAnalysisRepository,
    private val mapper: WiAnalysisMapper,
) :
    CrudServiceView<
        WiAnalysis,
        VWiAnalysisTranslated,
        WiAnalysisDto,
        WiAnalysisDetailDto,
        WiAnalysisDto,
        WiAnalysisDto,
        Long,
        WiAnalysisFilter,
        ReadAllParamsFilterDto>() {
    override fun viewToDto(entity: VWiAnalysisTranslated, translated: Boolean): WiAnalysisDto {
        return mapper.map(entity)
    }

    override fun viewToDetailDto(
        entity: VWiAnalysisTranslated,
        translated: Boolean
    ): WiAnalysisDetailDto {
        return mapper.mapDetail(entity)
    }

    override fun createEntity(createDto: WiAnalysisDto): WiAnalysis {
        TODO("Not yet implemented ")
    }

    override fun updateEntity(updateDto: WiAnalysisDto, entity: WiAnalysis) {
        TODO("Not yet implemented")
    }

    override fun repositoryPageQuery(filter: WiAnalysisFilter): Page<VWiAnalysisTranslated> {
        val specification = WiAnalysisSpecification(filter)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    @Transactional(readOnly = true)
    fun getSourceDefinition(filter: WiSourceDefinitionFilterDto): List<WiSourceDefinitionDto>? {
        return advWiAnalysisRepository.getSourceDefinition(filter)
    }
}
