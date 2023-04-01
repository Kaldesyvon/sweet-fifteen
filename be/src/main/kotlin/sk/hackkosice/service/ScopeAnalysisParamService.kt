package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.ScopeAnalysisParamDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.ScopeAnalysisParamMapper
import sk.esten.uss.gbco2.model.entity.scope_analysis_param.ScopeAnalysisParam
import sk.esten.uss.gbco2.model.entity.scope_analysis_param.VScopeAnalysisParamTranslated
import sk.esten.uss.gbco2.model.repository.scope_analysis_param.ScopeAnalysisParamERepository
import sk.esten.uss.gbco2.model.repository.scope_analysis_param.ScopeAnalysisParamVRepository

@Service
class ScopeAnalysisParamService(
    override val entityRepository: ScopeAnalysisParamERepository,
    override val viewRepository: ScopeAnalysisParamVRepository,
    private val mapper: ScopeAnalysisParamMapper
) :
    CrudServiceView<
        ScopeAnalysisParam,
        VScopeAnalysisParamTranslated,
        ScopeAnalysisParamDto,
        ScopeAnalysisParamDto,
        ScopeAnalysisParamDto,
        ScopeAnalysisParamDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {
    override fun viewToDto(
        entity: VScopeAnalysisParamTranslated,
        translated: Boolean
    ): ScopeAnalysisParamDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun viewToDetailDto(
        entity: VScopeAnalysisParamTranslated,
        translated: Boolean
    ): ScopeAnalysisParamDto = viewToDto(entity, translated)

    override fun createEntity(createDto: ScopeAnalysisParamDto): ScopeAnalysisParam {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun updateEntity(updateDto: ScopeAnalysisParamDto, entity: ScopeAnalysisParam) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    fun deleteAll(scopeAnalysisParams: Set<ScopeAnalysisParam>) {
        entityRepository.deleteAllInBatch(scopeAnalysisParams)
    }

    fun saveAll(scopeAnalysisParams: Set<ScopeAnalysisParam>) {
        entityRepository.saveAllAndFlush(scopeAnalysisParams)
    }
}
