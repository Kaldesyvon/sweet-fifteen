package sk.esten.uss.gbco2.service

import java.time.LocalDateTime
import org.apache.commons.lang3.NotImplementedException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.LoginHistoryFilter
import sk.esten.uss.gbco2.dto.response.LoginDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleLoginDto
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.mapper.LoginMapper
import sk.esten.uss.gbco2.model.entity.login.Login
import sk.esten.uss.gbco2.model.repository.login.LoginRepository
import sk.esten.uss.gbco2.model.specification.LoginHistorySpecification
import sk.esten.uss.gbco2.utils.Constants

@Service
class LoginService(
    override val entityRepository: LoginRepository,
    private val mapper: LoginMapper
) :
    CrudService<
        Login,
        LoginDto,
        LoginDto,
        LoginDto,
        LoginDto,
        Long,
        LoginHistoryFilter,
        ReadAllParamsFilterDto>() {

    @Transactional
    fun logoutExpiredSessions(
        tokenLifetime: Int = Constants.MAX_AD_ACCESS_TOKEN_LIFETIME_IN_MUNUTES,
    ): Int = entityRepository.logoutExpiredSessions(tokenLifetime)

    @Transactional
    fun insertLoginRecord(
        success: Boolean,
        username: String,
        remoteAddr: String?,
        remotePort: Int?
    ): Login? {
        val addr = remoteAddr ?: "unknown"
        val port = remotePort?.toString() ?: "unknown"

        var login = Login().apply { login = username }
        if (success) {
            login = entityRepository.findActiveLoginByUsernameAndIp(username, addr) ?: login
        }
        login.loginDate = LocalDateTime.now()
        login.success = success
        login.ip = addr
        login.port = port
        return entityRepository.save(login)
    }

    @Transactional
    fun logAttemptLogin(remoteAddr: String?, remotePort: Int?): Login? {
        val login = Login().apply { login = Constants.ATTEMPT_LOGIN_NAME }
        login.loginDate = LocalDateTime.now()
        login.success = false
        login.ip = remoteAddr ?: "unknown"
        login.port = remotePort?.toString() ?: "unknown"
        return entityRepository.save(login)
    }

    @Transactional
    fun logLogin(username: String, remoteAddr: String?, remotePort: Int?, success: Boolean = true) {
        logoutExpiredSessions()
        removeAttemptLogin(remoteAddr, remotePort)
        insertLoginRecord(success, username, remoteAddr, remotePort)
    }

    @Transactional
    fun removeAttemptLogin(remoteAddr: String?, remotePort: Int?) {
        val addr = remoteAddr ?: "unknown"
        val port = remotePort?.toString() ?: "unknown"
        entityRepository.deleteAttemptLogin(addr, port)
    }

    @Transactional(readOnly = true)
    fun getAllLoggedUsers(sortColumn: String, direction: Sort.Direction): List<SimpleLoginDto> {
        return entityRepository.findAllActiveLogins(Sort.by(direction, sortColumn)).map {
            mapper.mapSimple(it)
        }
    }

    override fun entityToDetailDto(entity: Login, translated: Boolean): LoginDto =
        entityToDto(entity, translated)

    override fun entityToDto(entity: Login, translated: Boolean): LoginDto = mapper.map(entity)

    override fun createEntity(createDto: LoginDto): Nothing =
        throw NotImplementedException("functionality is overridden")

    override fun updateEntity(updateDto: LoginDto, entity: Login): Nothing =
        throw NotImplementedException("functionality is overridden")

    override fun repositoryPageQuery(filter: LoginHistoryFilter): Page<Login> {
        val specification = LoginHistorySpecification(filter)
        return entityRepository.findAll(specification, filter.toPageable())
    }
}
