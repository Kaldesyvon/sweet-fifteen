package sk.esten.uss.gbco2.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateAuthorizationDto
import sk.esten.uss.gbco2.dto.request.filter.AuthorizationFilterDto
import sk.esten.uss.gbco2.dto.response.AuthorizationDto
import sk.esten.uss.gbco2.generics.controller.*
import sk.esten.uss.gbco2.model.entity.authorization.Authorization
import sk.esten.uss.gbco2.model.entity.authorization.VAuthorizationTranslated
import sk.esten.uss.gbco2.service.AuthorizationService

@RestController
@RequestMapping(path = ["/authorization"])
class AuthorizationController(
    override val crudService: AuthorizationService,
) :
    PageableCrudControllerView<
        Authorization,
        VAuthorizationTranslated,
        AuthorizationDto,
        AuthorizationDto,
        CreateAuthorizationDto,
        CreateAuthorizationDto,
        Long,
        AuthorizationFilterDto,
        ReadAllParamsFilterDto>,
    ReadDetailCrudControllerView<
        Authorization,
        VAuthorizationTranslated,
        AuthorizationDto,
        AuthorizationDto,
        CreateAuthorizationDto,
        CreateAuthorizationDto,
        Long,
        AuthorizationFilterDto,
        ReadAllParamsFilterDto>,
    CreateCrudControllerView<
        Authorization,
        VAuthorizationTranslated,
        AuthorizationDto,
        AuthorizationDto,
        CreateAuthorizationDto,
        CreateAuthorizationDto,
        Long,
        AuthorizationFilterDto,
        ReadAllParamsFilterDto>,
    UpdateCrudControllerView<
        Authorization,
        VAuthorizationTranslated,
        AuthorizationDto,
        AuthorizationDto,
        CreateAuthorizationDto,
        CreateAuthorizationDto,
        Long,
        AuthorizationFilterDto,
        ReadAllParamsFilterDto>,
    DeleteCrudControllerView<
        Authorization,
        VAuthorizationTranslated,
        AuthorizationDto,
        AuthorizationDto,
        CreateAuthorizationDto,
        CreateAuthorizationDto,
        Long,
        AuthorizationFilterDto,
        ReadAllParamsFilterDto>
