package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.UnitAnalysisFormatDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.UnitAnalysisFormatMapper
import sk.esten.uss.gbco2.model.entity.unit_analysis_format.UnitAnalysisFormat
import sk.esten.uss.gbco2.model.entity.unit_analysis_format.VUnitAnalysisFormatTranslated
import sk.esten.uss.gbco2.model.repository.unit_analysis_format.UnitAnalysisFormatERepository
import sk.esten.uss.gbco2.model.repository.unit_analysis_format.UnitAnalysisFormatVRepository

@Service
class UnitAnalysisFormatService(
    override val entityRepository: UnitAnalysisFormatERepository,
    override val viewRepository: UnitAnalysisFormatVRepository,
    private val mapper: UnitAnalysisFormatMapper
) :
    CrudServiceView<
        UnitAnalysisFormat,
        VUnitAnalysisFormatTranslated,
        UnitAnalysisFormatDto,
        UnitAnalysisFormatDto,
        UnitAnalysisFormatDto,
        UnitAnalysisFormatDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun createEntity(createDto: UnitAnalysisFormatDto): UnitAnalysisFormat {
        return mapper.map(createDto)
    }

    override fun updateEntity(updateDto: UnitAnalysisFormatDto, entity: UnitAnalysisFormat) {
        mapper.update(updateDto, entity)
    }

    override fun viewToDto(
        entity: VUnitAnalysisFormatTranslated,
        translated: Boolean
    ): UnitAnalysisFormatDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun viewToDetailDto(
        entity: VUnitAnalysisFormatTranslated,
        translated: Boolean
    ): UnitAnalysisFormatDto = viewToDto(entity, translated)
}
