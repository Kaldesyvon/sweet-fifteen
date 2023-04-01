package sk.esten.uss.gbco2.service

import javax.naming.directory.InvalidAttributesException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.PrincipalUserDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateUserDto
import sk.esten.uss.gbco2.dto.request.filter.UsersFilter
import sk.esten.uss.gbco2.dto.response.UserDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleLanguageDto
import sk.esten.uss.gbco2.exceptions.DatabaseException
import sk.esten.uss.gbco2.exceptions.NotFoundException
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.AuthorizationMapper
import sk.esten.uss.gbco2.mapper.UserMapper
import sk.esten.uss.gbco2.model.entity.authorization.Authorization
import sk.esten.uss.gbco2.model.entity.user.User
import sk.esten.uss.gbco2.model.entity.user.VUserTranslated
import sk.esten.uss.gbco2.model.repository.authorization.AuthorizationERepository
import sk.esten.uss.gbco2.model.repository.user.UserERepository
import sk.esten.uss.gbco2.model.repository.user.UserVRepository
import sk.esten.uss.gbco2.model.specification.UserSpecification
import sk.esten.uss.gbco2.utils.principal
import sk.esten.uss.gbco2.utils.principalIdOrNull

@Service
class UserService(
    override val entityRepository: UserERepository,
    override val viewRepository: UserVRepository,
    private val mapper: UserMapper,
    private val authorizationERepository: AuthorizationERepository,
    private val authorizationMapper: AuthorizationMapper
) :
    CrudServiceView<
        User,
        VUserTranslated,
        UserDto,
        UserDto,
        CreateUserDto,
        CreateUserDto,
        Long,
        UsersFilter,
        ReadAllParamsFilterDto>() {

    override fun createEntity(createDto: CreateUserDto): User = mapper.map(createDto)

    override fun updateEntity(updateDto: CreateUserDto, entity: User) =
        mapper.update(updateDto, entity)

    override fun viewToDto(entity: VUserTranslated, translated: Boolean): UserDto =
        if (translated) mapper.map(entity) else mapper.mapEn(entity)

    override fun viewToDetailDto(entity: VUserTranslated, translated: Boolean): UserDto =
        viewToDto(entity, translated)

    @Transactional
    override fun create(createDto: CreateUserDto): UserDto {
        try {
            val user = createEntity(createDto)
            val authorizations =
                createDto
                    .authorizations
                    ?.map {
                        val mappedAuthorization = authorizationMapper.map(it)
                        mappedAuthorization.user = user
                        mappedAuthorization
                    }
                    .orEmpty()

            val userId =
                entityRepository.saveAndFlush(user).getPk()
                    ?: throw InvalidAttributesException("Id must not be null")
            authorizationERepository.saveAll(authorizations)

            return viewToDto(getView(userId))
        } catch (e: DataIntegrityViolationException) {
            throw DatabaseException(e)
        }
    }

    @Transactional
    override fun update(id: Long, updateDto: CreateUserDto): UserDto {
        try {
            val user = getInternal(id)
            updateEntity(updateDto, user)

            val currentAuthorizations = user.authorizations
            val authsToSave = mutableListOf<Authorization>()

            updateDto.authorizations?.forEach { dtoAuthorization ->
                val existingRoleAuthorizations =
                    currentAuthorizations.filter { authorization ->
                        authorization.role?.id == dtoAuthorization.roleId
                    }
                if (existingRoleAuthorizations.isEmpty()) {
                    // new
                    val mappedAuthorization = authorizationMapper.map(dtoAuthorization)
                    mappedAuthorization.user = user
                    authsToSave.add(mappedAuthorization)
                } else {
                    val roleNodeAuthorization =
                        existingRoleAuthorizations.find { it.node?.id == dtoAuthorization.nodeId }

                    if (roleNodeAuthorization == null) {
                        val mappedAuthorization = authorizationMapper.map(dtoAuthorization)
                        mappedAuthorization.user = user
                        authsToSave.add(mappedAuthorization)
                    } else {
                        // this authorization does not change, just leave it
                        authsToSave.add(roleNodeAuthorization)
                    }
                }
            }

            val authsToDelete = currentAuthorizations - authsToSave
            entityRepository.save(user)
            authorizationERepository.deleteAll(authsToDelete)
            authorizationERepository.saveAllAndFlush(authsToSave)

            return viewToDto(getView(id))
        } catch (e: DataIntegrityViolationException) {
            throw DatabaseException(e)
        }
    }

    override fun repositoryPageQuery(filter: UsersFilter): Page<VUserTranslated> =
        viewRepository.findAll(UserSpecification(filter), filter.toPageable())

    @Transactional
    fun editUserLang(languageId: Long) =
        principalIdOrNull()?.let { entityRepository.updateUserLang(it, languageId) }

    @Transactional
    fun getUserLang(): SimpleLanguageDto {
        val principal = principal()
        return SimpleLanguageDto().apply {
            id = principal?.languageId ?: 1
            code = principal?.languageCode ?: "EN"
        }
    }

    @Transactional
    fun updateCurrentUser(updateDto: CreateUserDto): UserDto {
        val currentUser =
            get(principalIdOrNull()) ?: throw NotFoundException("Logged user was not found")
        mapper.updateCurrentUser(updateDto, currentUser)
        return persistInternal(currentUser)
    }

    @Transactional(readOnly = true)
    fun getCurrentUser(): UserDto =
        viewToDto(getView(principalIdOrNull() ?: throw NotFoundException()))

    @Transactional(readOnly = true)
    fun findUserPrincipalByLogin(username: String, fetchRoles: Boolean = true): PrincipalUserDto? {
        return entityRepository.findUserPrincipalByLogin(username)?.apply {
            if (fetchRoles) {
                id?.let {
                    authorizations = authorizationERepository.findUserPrincipalAuthorizations(it)
                }
            }
        }
    }

    fun getAllUsersForNotification(): List<User> {
        return entityRepository.findAllUsersForNotification()
    }
}
