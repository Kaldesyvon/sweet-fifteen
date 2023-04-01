package sk.esten.uss.gbco2.service

import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateUnitSetSettingsDto
import sk.esten.uss.gbco2.dto.request.filter.UnitSetSettingsFilter
import sk.esten.uss.gbco2.dto.response.UnitSetSettingsDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.UnitSetSettingsMapper
import sk.esten.uss.gbco2.model.entity.unit_set_settings.UnitSetSettings
import sk.esten.uss.gbco2.model.entity.unit_set_settings.VUnitSetSettingsTranslated
import sk.esten.uss.gbco2.model.repository.unit_set_settings.UnitSetSettingsERepository
import sk.esten.uss.gbco2.model.repository.unit_set_settings.UnitSetSettingsVRepository
import sk.esten.uss.gbco2.model.specification.UnitSetSettingsSpecification

@Service
class UnitSetSettingsService(
    override val entityRepository: UnitSetSettingsERepository,
    override val viewRepository: UnitSetSettingsVRepository,
    private val mapper: UnitSetSettingsMapper
) :
    CrudServiceView<
        UnitSetSettings,
        VUnitSetSettingsTranslated,
        UnitSetSettingsDto,
        UnitSetSettingsDto,
        CreateUnitSetSettingsDto,
        CreateUnitSetSettingsDto,
        Long,
        UnitSetSettingsFilter,
        ReadAllParamsFilterDto>() {
    override fun viewToDto(
        entity: VUnitSetSettingsTranslated,
        translated: Boolean
    ): UnitSetSettingsDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: CreateUnitSetSettingsDto): UnitSetSettings {
        return mapper.map(createDto)
    }

    override fun updateEntity(updateDto: CreateUnitSetSettingsDto, entity: UnitSetSettings) {
        mapper.update(updateDto, entity)
    }

    override fun repositoryPageQuery(
        filter: UnitSetSettingsFilter
    ): Page<VUnitSetSettingsTranslated> {
        val specification = UnitSetSettingsSpecification(filter)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    override fun viewToDetailDto(
        entity: VUnitSetSettingsTranslated,
        translated: Boolean
    ): UnitSetSettingsDto = viewToDto(entity, translated)
}
