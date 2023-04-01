package sk.esten.uss.gbco2.service

import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateAuthorizationDto
import sk.esten.uss.gbco2.dto.request.filter.AuthorizationFilterDto
import sk.esten.uss.gbco2.dto.response.AuthorizationDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.AuthorizationMapper
import sk.esten.uss.gbco2.model.entity.authorization.Authorization
import sk.esten.uss.gbco2.model.entity.authorization.VAuthorizationTranslated
import sk.esten.uss.gbco2.model.repository.authorization.AuthorizationERepository
import sk.esten.uss.gbco2.model.repository.authorization.AuthorizationVRepository
import sk.esten.uss.gbco2.model.specification.AuthorizationSpecification

@Service
@Transactional
class AuthorizationService(
    override val entityRepository: AuthorizationERepository,
    override val viewRepository: AuthorizationVRepository,
    private val mapper: AuthorizationMapper,
) :
    CrudServiceView<
        Authorization,
        VAuthorizationTranslated,
        AuthorizationDto,
        AuthorizationDto,
        CreateAuthorizationDto,
        CreateAuthorizationDto,
        Long,
        AuthorizationFilterDto,
        ReadAllParamsFilterDto>() {

    override fun viewToDto(
        entity: VAuthorizationTranslated,
        translated: Boolean
    ): AuthorizationDto = if (translated) mapper.map(entity) else mapper.mapEn(entity)

    override fun viewToDetailDto(
        entity: VAuthorizationTranslated,
        translated: Boolean
    ): AuthorizationDto = viewToDto(entity, translated)

    override fun createEntity(createDto: CreateAuthorizationDto): Authorization =
        mapper.map(createDto)

    override fun updateEntity(updateDto: CreateAuthorizationDto, entity: Authorization) =
        mapper.update(updateDto, entity)

    override fun repositoryPageQuery(
        filter: AuthorizationFilterDto
    ): Page<VAuthorizationTranslated> =
        viewRepository.findAll(AuthorizationSpecification(filter), filter.toPageable())
}
