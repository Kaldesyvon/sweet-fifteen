package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.ScopeFuelSpecDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.ScopeFuelSpecMapper
import sk.esten.uss.gbco2.model.entity.scope_fuel_spec.ScopeFuelSpec
import sk.esten.uss.gbco2.model.entity.scope_fuel_spec.VScopeFuelSpecTranslated
import sk.esten.uss.gbco2.model.repository.scope_fuel_spec.ScopeFuelSpecERepository
import sk.esten.uss.gbco2.model.repository.scope_fuel_spec.ScopeFuelSpecVRepository
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
class ScopeFuelSpecService(
    override val entityRepository: ScopeFuelSpecERepository,
    override val viewRepository: ScopeFuelSpecVRepository,
    private val mapper: ScopeFuelSpecMapper,
) :
    CrudServiceView<
        ScopeFuelSpec,
        VScopeFuelSpecTranslated,
        ScopeFuelSpecDto,
        ScopeFuelSpecDto,
        ScopeFuelSpecDto,
        ScopeFuelSpecDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun viewToDto(
        entity: VScopeFuelSpecTranslated,
        translated: Boolean
    ): ScopeFuelSpecDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: ScopeFuelSpecDto): ScopeFuelSpec {
        val scopeFuelSpec = mapper.map(createDto)
        newEnTranslation(scopeFuelSpec.nameK, createDto.name, scopeFuelSpec.translations)
        return scopeFuelSpec
    }

    override fun updateEntity(updateDto: ScopeFuelSpecDto, entity: ScopeFuelSpec) {
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.translations)
    }

    override fun viewToDetailDto(
        entity: VScopeFuelSpecTranslated,
        translated: Boolean
    ): ScopeFuelSpecDto = viewToDto(entity, translated)
}
