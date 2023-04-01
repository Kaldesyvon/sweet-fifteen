package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.AnalysisParamTypeDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.AnalysisParamTypeMapper
import sk.esten.uss.gbco2.model.entity.analysis_param_type.AnalysisParamType
import sk.esten.uss.gbco2.model.entity.analysis_param_type.VAnalysisParamTypeTranslated
import sk.esten.uss.gbco2.model.repository.analysis_param_type.AnalysisParamTypeERepository
import sk.esten.uss.gbco2.model.repository.analysis_param_type.AnalysisParamTypeVRepository

@Service
class AnalysisParamTypeService(
    override val entityRepository: AnalysisParamTypeERepository,
    override val viewRepository: AnalysisParamTypeVRepository,
    private val mapper: AnalysisParamTypeMapper,
) :
    CrudServiceView<
        AnalysisParamType,
        VAnalysisParamTypeTranslated,
        AnalysisParamTypeDto,
        AnalysisParamTypeDto,
        AnalysisParamTypeDto,
        AnalysisParamTypeDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun createEntity(createDto: AnalysisParamTypeDto): AnalysisParamType {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun updateEntity(updateDto: AnalysisParamTypeDto, entity: AnalysisParamType) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    override fun viewToDto(
        entity: VAnalysisParamTypeTranslated,
        translated: Boolean
    ): AnalysisParamTypeDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun viewToDetailDto(
        entity: VAnalysisParamTypeTranslated,
        translated: Boolean
    ): AnalysisParamTypeDto = viewToDto(entity, translated)
}
