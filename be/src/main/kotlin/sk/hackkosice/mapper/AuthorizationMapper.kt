package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateAuthorizationDto
import sk.esten.uss.gbco2.dto.request.create.CreateUserAuthorizationDto
import sk.esten.uss.gbco2.dto.response.AuthorizationDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleAuthorizationDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.authorization.Authorization
import sk.esten.uss.gbco2.model.entity.authorization.VAuthorizationTranslated
import sk.esten.uss.gbco2.service.NodeService
import sk.esten.uss.gbco2.service.RoleService
import sk.esten.uss.gbco2.service.UserService
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses =
        [
            RoleMapper::class,
            NodeMapper::class,
            UserMapper::class,
            RoleService::class,
            NodeService::class,
            UserService::class]
)
interface AuthorizationMapper {

    @Mappings(
        Mapping(
            source = "node",
            target = "node",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(source = "user", target = "user", qualifiedBy = [SimpleMapper::class]),
        Mapping(
            source = "role",
            target = "role",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        )
    )
    @TranslatedLanguageMapper
    fun map(entity: VAuthorizationTranslated): AuthorizationDto

    @Mappings(
        Mapping(
            source = "node",
            target = "node",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(source = "user", target = "user", qualifiedBy = [SimpleMapper::class]),
        Mapping(
            source = "role",
            target = "role",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        )
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VAuthorizationTranslated): AuthorizationDto

    @Mappings(
        Mapping(
            source = "node",
            target = "node",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "role",
            target = "role",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        )
    )
    @TranslatedLanguageMapper
    @SimpleMapper
    fun mapSimple(entity: VAuthorizationTranslated): SimpleAuthorizationDto

    @Mappings(
        Mapping(
            source = "node",
            target = "node",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "role",
            target = "role",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        )
    )
    @EnglishLanguageMapper
    @SimpleMapper
    fun mapSimpleEn(entity: VAuthorizationTranslated): SimpleAuthorizationDto

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(source = "userId", target = "user"),
        Mapping(source = "nodeId", target = "node"),
        Mapping(source = "roleId", target = "role")
    )
    fun map(createDto: CreateAuthorizationDto): Authorization

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(target = "user", ignore = true),
        Mapping(source = "nodeId", target = "node"),
        Mapping(source = "roleId", target = "role")
    )
    fun update(updateDto: CreateAuthorizationDto, @MappingTarget entity: Authorization)

    @Mappings(
        Mapping(source = "nodeId", target = "node"),
        Mapping(source = "roleId", target = "role"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "user", ignore = true),
    )
    @IgnoreBaseAttributes
    fun map(createDto: CreateUserAuthorizationDto): Authorization

    @AfterMapping
    fun clearNodeAuthorization(@MappingTarget target: Authorization) {
        if (target.role?.nodeRole == false) {
            target.node = null
        }
    }
}
