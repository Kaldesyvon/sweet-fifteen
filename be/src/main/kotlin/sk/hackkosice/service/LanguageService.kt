package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.LanguageDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.LanguageMapper
import sk.esten.uss.gbco2.model.entity.language.Language
import sk.esten.uss.gbco2.model.entity.language.VLanguageTranslated
import sk.esten.uss.gbco2.model.repository.language.LanguageERepository
import sk.esten.uss.gbco2.model.repository.language.LanguageVRepository
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
class LanguageService(
    private val mapper: LanguageMapper,
    override val entityRepository: LanguageERepository,
    override val viewRepository: LanguageVRepository,
) :
    CrudServiceView<
        Language,
        VLanguageTranslated,
        LanguageDto,
        LanguageDto,
        LanguageDto,
        LanguageDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun createEntity(createDto: LanguageDto): Language {
        val obj = mapper.map(createDto)
        newEnTranslation(obj.nameK, createDto.name, obj.translations)
        return obj
    }

    override fun updateEntity(updateDto: LanguageDto, entity: Language) {
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.translations)
    }

    override fun viewToDto(entity: VLanguageTranslated, translated: Boolean): LanguageDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun viewToDetailDto(entity: VLanguageTranslated, translated: Boolean): LanguageDto =
        viewToDto(entity, translated)
}
