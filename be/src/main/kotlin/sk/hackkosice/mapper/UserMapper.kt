package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateUserDto
import sk.esten.uss.gbco2.dto.response.UserDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleUserDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.user.User
import sk.esten.uss.gbco2.model.entity.user.VUserTranslated
import sk.esten.uss.gbco2.service.*
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses =
        [
            NodeMapper::class,
            UnitSetMapper::class,
            LanguageMapper::class,
            ScopeMapper::class,
            AnalysisParamMapper::class,
            UnitSetService::class,
            LanguageService::class,
            ScopeService::class,
            AnalysisParamService::class,
            NodeService::class,
            AuthorizationMapper::class]
)
interface UserMapper {

    @SimpleMapper fun mapSimple(entity: VUserTranslated): SimpleUserDto

    @Mappings(
        Mapping(source = "function", target = "position"),
        Mapping(
            source = "unitSet",
            target = "unitSet",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "languageUser",
            target = "languageUser",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "defaultNode",
            target = "defaultNode",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "defaultScope",
            target = "defaultScope",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "defaultAnalysisParam",
            target = "defaultAnalysisParam",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "node",
            target = "node",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "authorizations",
            target = "authorizations",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
    )
    @TranslatedLanguageMapper
    fun map(entity: VUserTranslated): UserDto

    @Mappings(
        Mapping(source = "function", target = "position"),
        Mapping(
            source = "unitSet",
            target = "unitSet",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "languageUser",
            target = "languageUser",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "defaultNode",
            target = "defaultNode",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "defaultScope",
            target = "defaultScope",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "defaultAnalysisParam",
            target = "defaultAnalysisParam",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "node",
            target = "node",
            qualifiedBy = [EnglishLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "authorizations",
            target = "authorizations",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VUserTranslated): UserDto

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(source = "position", target = "function"),
        Mapping(source = "unitSetId", target = "unitSet"),
        Mapping(source = "nodeId", target = "node"),
        Mapping(source = "languageId", target = "languageUser"),
        Mapping(source = "defaultNodeId", target = "defaultNode"),
        Mapping(source = "defaultScopeId", target = "defaultScope"),
        Mapping(source = "defaultAnalysisParamId", target = "defaultAnalysisParam"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "authorizations", ignore = true),
    )
    fun update(dto: CreateUserDto, @MappingTarget entity: User)

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(source = "position", target = "function"),
        Mapping(source = "unitSetId", target = "unitSet"),
        Mapping(source = "nodeId", target = "node"),
        Mapping(source = "languageId", target = "languageUser"),
        Mapping(source = "defaultNodeId", target = "defaultNode"),
        Mapping(source = "defaultScopeId", target = "defaultScope"),
        Mapping(source = "defaultAnalysisParamId", target = "defaultAnalysisParam"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "loginAd", ignore = true),
        Mapping(target = "objVersion", ignore = true),
        Mapping(target = "authorizations", ignore = true)
    )
    fun map(dto: CreateUserDto): User

    @Mappings(
        Mapping(source = "position", target = "function"),
        Mapping(source = "unitSetId", target = "unitSet"),
        Mapping(source = "nodeId", target = "node"),
        Mapping(source = "languageId", target = "languageUser"),
        Mapping(source = "defaultNodeId", target = "defaultNode"),
        Mapping(source = "defaultScopeId", target = "defaultScope"),
        Mapping(source = "defaultAnalysisParamId", target = "defaultAnalysisParam"),
        Mapping(target = "enabled", ignore = true),
        Mapping(target = "name", ignore = true),
        Mapping(target = "surname", ignore = true),
        Mapping(target = "login", ignore = true),
        Mapping(target = "loginAd", ignore = true),
        Mapping(target = "authorizations", ignore = true),
        Mapping(target = "id", ignore = true),
    )
    @IgnoreBaseAttributes
    fun updateCurrentUser(dto: CreateUserDto, @MappingTarget entity: User)
}
