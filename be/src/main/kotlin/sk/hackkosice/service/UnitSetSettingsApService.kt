package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.UnitSetSettingsApFilter
import sk.esten.uss.gbco2.dto.response.UnitSetSettingsApDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.UnitSetSettingsApMapper
import sk.esten.uss.gbco2.model.entity.unit_set_settings_ap.UnitSetSettingsAp
import sk.esten.uss.gbco2.model.entity.unit_set_settings_ap.VUnitSetSettingsApTranslated
import sk.esten.uss.gbco2.model.repository.unit_set_settings_ap.UnitSetSettingsApERepository
import sk.esten.uss.gbco2.model.repository.unit_set_settings_ap.UnitSetSettingsApVRepository
import sk.esten.uss.gbco2.model.specification.UnitSetSettingsApSpecification
import sk.esten.uss.gbco2.utils.principalLangOrEn
import sk.esten.uss.gbco2.utils.principalUnitSetIdOrMetricId

@Service
class UnitSetSettingsApService(
    override val entityRepository: UnitSetSettingsApERepository,
    override val viewRepository: UnitSetSettingsApVRepository,
    private val mapper: UnitSetSettingsApMapper
) :
    CrudServiceView<
        UnitSetSettingsAp,
        VUnitSetSettingsApTranslated,
        UnitSetSettingsApDto,
        UnitSetSettingsApDto,
        UnitSetSettingsApDto,
        UnitSetSettingsApDto,
        Long,
        UnitSetSettingsApFilter,
        ReadAllParamsFilterDto>() {

    override fun viewToDto(
        entity: VUnitSetSettingsApTranslated,
        translated: Boolean
    ): UnitSetSettingsApDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: UnitSetSettingsApDto): UnitSetSettingsAp {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun updateEntity(updateDto: UnitSetSettingsApDto, entity: UnitSetSettingsAp) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    override fun repositoryPageQuery(
        filter: UnitSetSettingsApFilter
    ): Page<VUnitSetSettingsApTranslated> {
        val specification = UnitSetSettingsApSpecification(filter)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    override fun viewToDetailDto(
        entity: VUnitSetSettingsApTranslated,
        translated: Boolean
    ): UnitSetSettingsApDto = viewToDto(entity, translated)

    fun getAPUnit(analysisParamId: Long): UnitSetSettingsApDto? {
        val entity =
            viewRepository.findByLanguageIdAndAnalysisParamAndUnitSet(
                principalLangOrEn().id,
                analysisParamId,
                principalUnitSetIdOrMetricId()
            )
        return entity?.let { mapper.map(it) }
    }
}
