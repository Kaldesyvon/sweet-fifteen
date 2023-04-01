package sk.esten.uss.gbco2.generics.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.exceptions.DatabaseException
import sk.esten.uss.gbco2.exceptions.NotFoundException
import sk.esten.uss.gbco2.generics.model.IdEntity
import sk.esten.uss.gbco2.generics.model.TranslatedEntity
import sk.esten.uss.gbco2.generics.model.repository.CrudEntityRepository
import sk.esten.uss.gbco2.generics.model.repository.applyOrderWithNullHandling
import sk.esten.uss.gbco2.model.entity.dictionary.Dictionary
import sk.esten.uss.gbco2.model.repository.dictionary.DictionaryERepository

abstract class CrudService<
    ENTITY : IdEntity<ID>,
    DTO : Any,
    DETAIL_DTO : DTO,
    CREATE_DTO : Any,
    UPDATE_DTO : Any,
    ID : Any,
    FILTER : PageableParamsFilterDto,
    FILTER_ALL : ReadAllParamsFilterDto> {

    abstract val entityRepository: CrudEntityRepository<ENTITY, ID>

    @Autowired lateinit var dictRepo: DictionaryERepository

    @Transactional(readOnly = true)
    open fun get(id: ID?): ENTITY? {
        if (id == null) {
            return null
        }
        return getInternal(id)
    }

    @Transactional(readOnly = true)
    open fun getById(id: ID, translated: Boolean): DETAIL_DTO {
        return entityToDetailDto(getInternal(id), translated)
    }

    open fun create(createDto: CREATE_DTO): DTO {
        try {
            return persistInternal(createEntity(createDto))
        } catch (e: DataIntegrityViolationException) {
            throw DatabaseException(e)
        }
    }

    open fun update(id: ID, updateDto: UPDATE_DTO): DTO {
        try {
            return updateInternal(id, updateDto)
        } catch (e: DataIntegrityViolationException) {
            throw DatabaseException(e)
        }
    }

    open fun delete(id: ID) {
        try {

            return deleteInternal(id)
        } catch (e: DataIntegrityViolationException) {
            throw DatabaseException(e)
        }
    }

    abstract fun entityToDetailDto(entity: ENTITY, translated: Boolean = true): DETAIL_DTO

    abstract fun entityToDto(entity: ENTITY, translated: Boolean = true): DTO

    abstract fun createEntity(createDto: CREATE_DTO): ENTITY

    abstract fun updateEntity(updateDto: UPDATE_DTO, entity: ENTITY)

    @Transactional
    open fun persistInternal(entity: ENTITY): DTO {
        return entityToDto(entityRepository.saveAndFlush(entity))
    }

    @Transactional
    open fun updateInternal(id: ID, updateDto: UPDATE_DTO): DTO {
        val entity = getInternal(id)
        updateEntity(updateDto, entity)
        return persistInternal(entity)
    }

    @Transactional
    open fun deleteInternal(id: ID) {
        val entity = getInternal(id)
        var translationFieldListBackup: Collection<Dictionary> = listOf()
        try {
            if (entity is TranslatedEntity) {
                translationFieldListBackup = entity.translationFieldList()
                dictRepo.deleteAll(entity.translationFieldList())
            }
            return entityRepository.delete(entity)
        } catch (e: DataIntegrityViolationException) {
            try {
                dictRepo.saveAll(translationFieldListBackup)
            } catch (ignored: Exception) {}

            throw e
        }
    }

    protected fun getInternal(id: ID): ENTITY {
        return findInternal(id) ?: throw NotFoundException()
    }

    @Transactional(readOnly = true)
    protected open fun findInternal(id: ID): ENTITY? {
        return entityRepository.findByIdOrNull(id)
    }

    @Transactional(readOnly = true)
    open fun getAllWithFilter(filter: FILTER_ALL?): List<ENTITY> {
        return getAllWithFilterInternal(filter)
    }

    @Transactional(readOnly = true)
    open fun getAllWithFilterInternal(filter: FILTER_ALL?): List<ENTITY> {
        return repositoryGetAllQuery(filter)
    }

    @Transactional(readOnly = true)
    open fun getPaginatedWithFilter(filter: FILTER): Page<DTO> {
        return getPaginatedWithFilterInternal(filter).map { entityToDto(it) }
    }

    @Transactional(readOnly = true)
    open fun getPaginatedWithFilterInternal(filter: FILTER): Page<ENTITY> {
        return repositoryPageQuery(filter)
    }

    @Transactional(readOnly = true)
    open fun repositoryPageQuery(filter: FILTER): Page<ENTITY> {
        if (filter::class == PageableParamsFilterDto::class) {
            return entityRepository.findAll(filter.toPageable())
        } else {
            throw IllegalAccessException("Override repositoryPageQuery method using Specification!")
        }
    }

    @Transactional(readOnly = true)
    open fun repositoryGetAllQuery(filter: FILTER_ALL?): List<ENTITY> {
        return if (filter != null) {
            if (filter::class == ReadAllParamsFilterDto::class) {
                entityRepository.findAll(wrapToSpecification(filter.toSort()))
            } else {
                throw IllegalAccessException(
                    "Override repositoryGetAllQuery method using Specification!"
                )
            }
        } else {
            entityRepository.findAll(
                wrapToSpecification(Sort.by(Sort.Order.by("id").ignoreCase().nullsLast()))
            )
        }
    }

    private fun wrapToSpecification(sort: Sort?): Specification<ENTITY> {
        return Specification<ENTITY> { root, query, builder ->
            sort?.applyOrderWithNullHandling(builder, root, query)
            null // null (empty predicate) selects all entries
        }
    }
}
