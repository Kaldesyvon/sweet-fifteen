package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.ScopeProcessSpecDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.ScopeProcessSpecMapper
import sk.esten.uss.gbco2.model.entity.scope_process_spec.ScopeProcessSpec
import sk.esten.uss.gbco2.model.entity.scope_process_spec.VScopeProcessSpecTranslated
import sk.esten.uss.gbco2.model.repository.scope_process_spec.ScopeProcessSpecERepository
import sk.esten.uss.gbco2.model.repository.scope_process_spec.ScopeProcessSpecVRepository
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
class ScopeProcessSpecService(
    override val entityRepository: ScopeProcessSpecERepository,
    override val viewRepository: ScopeProcessSpecVRepository,
    private val mapper: ScopeProcessSpecMapper,
) :
    CrudServiceView<
        ScopeProcessSpec,
        VScopeProcessSpecTranslated,
        ScopeProcessSpecDto,
        ScopeProcessSpecDto,
        ScopeProcessSpecDto,
        ScopeProcessSpecDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun createEntity(createDto: ScopeProcessSpecDto): ScopeProcessSpec {
        val obj = mapper.map(createDto)
        newEnTranslation(obj.nameK, createDto.name, obj.translations)
        return obj
    }

    override fun updateEntity(updateDto: ScopeProcessSpecDto, entity: ScopeProcessSpec) {
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.translations)
    }

    override fun viewToDto(
        entity: VScopeProcessSpecTranslated,
        translated: Boolean
    ): ScopeProcessSpecDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun viewToDetailDto(
        entity: VScopeProcessSpecTranslated,
        translated: Boolean
    ): ScopeProcessSpecDto = viewToDto(entity, translated)
}
