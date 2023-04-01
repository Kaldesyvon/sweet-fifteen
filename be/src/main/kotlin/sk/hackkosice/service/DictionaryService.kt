package sk.esten.uss.gbco2.service

import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateDictionaryDto
import sk.esten.uss.gbco2.dto.request.filter.DictionaryFilter
import sk.esten.uss.gbco2.dto.response.DictionaryDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.DictionaryMapper
import sk.esten.uss.gbco2.model.entity.dictionary.DictionaryFullyMapped
import sk.esten.uss.gbco2.model.entity.dictionary.VDictionaryTranslated
import sk.esten.uss.gbco2.model.repository.dictionary.DictionaryFullyMappedRepository
import sk.esten.uss.gbco2.model.repository.dictionary.DictionaryVRepository
import sk.esten.uss.gbco2.model.specification.DictionarySpecification
import sk.esten.uss.gbco2.utils.principalLangOrEn

@Service
class DictionaryService(
    override val entityRepository: DictionaryFullyMappedRepository,
    override val viewRepository: DictionaryVRepository,
    private val mapper: DictionaryMapper,
) :
    CrudServiceView<
        DictionaryFullyMapped,
        VDictionaryTranslated,
        DictionaryDto,
        DictionaryDto,
        CreateDictionaryDto,
        CreateDictionaryDto,
        Long,
        DictionaryFilter,
        ReadAllParamsFilterDto>() {

    override fun viewToDto(entity: VDictionaryTranslated, translated: Boolean): DictionaryDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun repositoryPageQuery(filter: DictionaryFilter): Page<VDictionaryTranslated> {
        val specification = DictionarySpecification(filter)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    override fun viewToDetailDto(
        entity: VDictionaryTranslated,
        translated: Boolean
    ): DictionaryDto = viewToDto(entity, translated)

    override fun createEntity(createDto: CreateDictionaryDto): DictionaryFullyMapped {
        return mapper.map(createDto)
    }

    override fun updateEntity(updateDto: CreateDictionaryDto, entity: DictionaryFullyMapped) {
        mapper.update(updateDto, entity)
    }

    @Transactional(readOnly = true)
    fun findTranslationByKeyAndLanguage(
        key: String,
        languageId: Long = principalLangOrEn().id ?: 1
    ): String? {
        return viewRepository.findTranslationByKeyAndLanguage(key, languageId)
    }

    @Transactional(readOnly = true)
    fun getMissingLanguageIds(dictionaryId: Long): List<Long> {
        return entityRepository.findAllMissingLanguagesForDictionaryId(dictionaryId)
    }
}
