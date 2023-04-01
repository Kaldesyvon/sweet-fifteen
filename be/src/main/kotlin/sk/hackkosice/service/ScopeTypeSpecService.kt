package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.ScopeTypeSpecDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.ScopeTypeSpecMapper
import sk.esten.uss.gbco2.model.entity.scope_type_spec.ScopeTypeSpec
import sk.esten.uss.gbco2.model.entity.scope_type_spec.VScopeTypeSpecTranslated
import sk.esten.uss.gbco2.model.repository.scope_type_spec.ScopeTypeSpecERepository
import sk.esten.uss.gbco2.model.repository.scope_type_spec.ScopeTypeSpecVRepository
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
class ScopeTypeSpecService(
    override val entityRepository: ScopeTypeSpecERepository,
    override val viewRepository: ScopeTypeSpecVRepository,
    private val mapper: ScopeTypeSpecMapper,
) :
    CrudServiceView<
        ScopeTypeSpec,
        VScopeTypeSpecTranslated,
        ScopeTypeSpecDto,
        ScopeTypeSpecDto,
        ScopeTypeSpecDto,
        ScopeTypeSpecDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun createEntity(createDto: ScopeTypeSpecDto): ScopeTypeSpec {
        val scopeTypeSpec = mapper.map(createDto)
        newEnTranslation(scopeTypeSpec.nameK, createDto.name, scopeTypeSpec.translations)
        return scopeTypeSpec
    }

    override fun updateEntity(updateDto: ScopeTypeSpecDto, entity: ScopeTypeSpec) {
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.translations)
    }

    override fun viewToDto(
        entity: VScopeTypeSpecTranslated,
        translated: Boolean
    ): ScopeTypeSpecDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun viewToDetailDto(
        entity: VScopeTypeSpecTranslated,
        translated: Boolean
    ): ScopeTypeSpecDto = viewToDto(entity, translated)
}
