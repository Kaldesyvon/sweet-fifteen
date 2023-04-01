package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.DictionaryUiDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.DictionaryUiMapper
import sk.esten.uss.gbco2.model.entity.dictionary_ui.DictionaryUi
import sk.esten.uss.gbco2.model.entity.dictionary_ui.VDictionaryUiTranslated
import sk.esten.uss.gbco2.model.repository.dictionary_ui.DictionaryUiERepository
import sk.esten.uss.gbco2.model.repository.dictionary_ui.DictionaryUiVRepository
import sk.esten.uss.gbco2.utils.principalLangOrEn

@Service
class DictionaryUiService(
    override val entityRepository: DictionaryUiERepository,
    override val viewRepository: DictionaryUiVRepository,
    val mapper: DictionaryUiMapper
) :
    CrudServiceView<
        DictionaryUi,
        VDictionaryUiTranslated,
        DictionaryUiDto,
        DictionaryUiDto,
        DictionaryUiDto,
        DictionaryUiDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    @Transactional(readOnly = true)
    fun findAllByLanguage(languageId: Long): Map<String, DictionaryUiDto> {
        return viewRepository
            .findAllByLanguageIdOrderByKey(languageId)
            .associateBy(
                keySelector = { it.key ?: "missing_key" },
                valueTransform = this::viewToDto
            )
    }

    @Transactional(readOnly = true)
    fun findTranslationByKeyAndLanguage(key: String): String? {
        return viewRepository.findTranslationByKeyAndLanguageId(key, principalLangOrEn().id ?: 1)
    }

    fun getAllTranslatedLabels(keys: List<String>): Map<String?, String?> {
        return viewRepository.findAllByKeys(keys, principalLangOrEn().id).associate {
            it.key to it.valueTranslated
        }
    }

    override fun updateEntity(updateDto: DictionaryUiDto, entity: DictionaryUi) {
        TODO("Not yet implemented")
    }

    override fun createEntity(createDto: DictionaryUiDto): DictionaryUi {
        TODO("Not yet implemented")
    }

    override fun viewToDto(entity: VDictionaryUiTranslated, translated: Boolean): DictionaryUiDto {
        return mapper.map(entity)
    }

    override fun viewToDetailDto(
        entity: VDictionaryUiTranslated,
        translated: Boolean
    ): DictionaryUiDto = viewToDto(entity, translated)
}
