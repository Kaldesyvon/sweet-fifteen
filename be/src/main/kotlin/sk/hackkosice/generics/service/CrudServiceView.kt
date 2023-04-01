package sk.esten.uss.gbco2.generics.service

import javax.naming.directory.InvalidAttributesException
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
import sk.esten.uss.gbco2.generics.model.IdViewEntity
import sk.esten.uss.gbco2.generics.model.TranslatedEntity
import sk.esten.uss.gbco2.generics.model.repository.CrudEntityRepository
import sk.esten.uss.gbco2.generics.model.repository.CrudViewRepository
import sk.esten.uss.gbco2.generics.model.repository.applyOrderWithNullHandling
import sk.esten.uss.gbco2.model.entity.dictionary.Dictionary
import sk.esten.uss.gbco2.model.repository.dictionary.DictionaryERepository
import sk.esten.uss.gbco2.utils.principalLangOrEn

abstract class CrudServiceView<
    ENTITY : IdEntity<ID>,
    VIEW : IdViewEntity<ID>,
    DTO : Any,
    DETAIL_DTO : DTO,
    CREATE_DTO : Any,
    UPDATE_DTO : Any,
    ID : Any,
    FILTER : PageableParamsFilterDto,
    FILTER_ALL : ReadAllParamsFilterDto> {

    abstract val entityRepository: CrudEntityRepository<ENTITY, ID>
    abstract val viewRepository: CrudViewRepository<VIEW, ID>

    @Autowired lateinit var dictRepo: DictionaryERepository

    /** get from entity table */
    @Transactional(readOnly = true)
    open fun get(id: ID?): ENTITY? {
        if (id == null) {
            return null
        }
        return getInternal(id)
    }

    /** get from entity table */
    @Transactional(readOnly = true)
    open fun getView(id: ID): VIEW {
        return viewRepository.findOne(
                Specification.where<VIEW> { root, _, builder ->
                    builder.equal(root.get<ID>("id"), id)
                }
                    .and { root, _, builder ->
                        builder.equal(root.get<Long>("languageId"), principalLangOrEn().id)
                    }
            )
            .orElseThrow { NotFoundException() }
    }

    @Transactional(readOnly = true)
    open fun getById(id: ID, translated: Boolean): DETAIL_DTO {
        return viewToDetailDto(getView(id), translated)
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

    abstract fun viewToDto(entity: VIEW, translated: Boolean = true): DTO

    abstract fun viewToDetailDto(entity: VIEW, translated: Boolean = true): DETAIL_DTO

    abstract fun createEntity(createDto: CREATE_DTO): ENTITY

    abstract fun updateEntity(updateDto: UPDATE_DTO, entity: ENTITY)

    @Transactional
    open fun persistInternal(entity: ENTITY): DTO {
        val id =
            entityRepository.saveAndFlush(entity).getPk()
                ?: throw InvalidAttributesException("Id must not be null")
        return viewToDto(getView(id))
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
        return entityRepository.findByIdOrNull(id) ?: throw NotFoundException()
    }

    @Transactional(readOnly = true)
    open fun getAllWithFilter(filter: FILTER_ALL?): List<VIEW> {
        return repositoryGetAllQuery(filter)
    }

    @Transactional(readOnly = true)
    open fun getPaginatedWithFilter(filter: FILTER): Page<DTO> {
        return repositoryPageQuery(filter).map { viewToDto(it) }
    }

    @Transactional(readOnly = true)
    open fun repositoryPageQuery(filter: FILTER): Page<VIEW> {
        if (filter::class == PageableParamsFilterDto::class) {
            return viewRepository.findAll(
                Specification.where { root, _, builder ->
                    builder.equal(root.get<Long>("languageId"), principalLangOrEn().id)
                },
                filter.toPageable()
            )
        } else {
            throw IllegalAccessException(
                "Unknown filter type [" +
                    filter.javaClass.name +
                    "], Override repositoryPageQuery method!"
            )
        }
    }

    @Transactional(readOnly = true)
    open fun repositoryGetAllQuery(filter: FILTER_ALL?): List<VIEW> {
        return if (filter != null) {
            if (filter::class == ReadAllParamsFilterDto::class) {
                viewRepository.findAll(languageEqual(filter.toSort()))
            } else {
                throw IllegalAccessException(
                    "Unknown filter type [${filter.javaClass.name}], Override repositoryGetAllQuery method!"
                )
            }
        } else {
            viewRepository.findAll(
                languageEqual(Sort.by(Sort.Order.by("id").ignoreCase().nullsLast()))
            )
        }
    }

    fun languageEqual(sort: Sort?): Specification<VIEW> {
        return Specification<VIEW> { root, query, builder ->
            sort?.applyOrderWithNullHandling(builder, root, query)
            builder.equal(root.get<Long>("languageId"), principalLangOrEn().id)
        }
    }
}
