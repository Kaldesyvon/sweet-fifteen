package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateScopeDto
import sk.esten.uss.gbco2.dto.response.ScopeDto
import sk.esten.uss.gbco2.dto.response.detail.ScopeDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleScopeDto
import sk.esten.uss.gbco2.mapper.annotation.*
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.scope.Scope
import sk.esten.uss.gbco2.model.entity.scope.ScopeSuper
import sk.esten.uss.gbco2.model.entity.scope.VScopeTranslated
import sk.esten.uss.gbco2.model.entity.scope_denominator.VScopeDenominatorTranslated
import sk.esten.uss.gbco2.model.entity.scope_material_node.VScopeMaterialNodeTranslated
import sk.esten.uss.gbco2.service.ScopeFuelSpecService
import sk.esten.uss.gbco2.service.ScopeProcessSpecService
import sk.esten.uss.gbco2.service.ScopeTypeSpecService
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses =
        [
            ScopeProcessSpecMapper::class,
            ScopeTypeSpecMapper::class,
            ScopeFuelSpecMapper::class,
            ScopeMaterialNodeMapper::class,
            ScopeAnalysisParamMapper::class,
            ScopeDenominatorMapper::class,
            ScopeProcessSpecService::class,
            ScopeFuelSpecService::class,
            ScopeTypeSpecService::class,
        ]
)
interface ScopeMapper : TranslationMapper {

    @Mappings(
        Mapping(
            source = "nameTranslated",
            target = "name",
        ),
        Mapping(
            source = "scopeDenominators",
            target = "denominatorsCount",
            qualifiedByName = ["denominators-count"]
        )
    )
    @SimpleMapper
    @TranslatedLanguageMapper
    fun mapToSimple(entity: VScopeTranslated): SimpleScopeDto

    @Mappings(
        Mapping(
            source = "nameEn",
            target = "name",
        ),
        Mapping(
            source = "scopeDenominators",
            target = "denominatorsCount",
            qualifiedByName = ["denominators-count"]
        )
    )
    @SimpleMapper
    @EnglishLanguageMapper
    fun mapToSimpleEn(entity: VScopeTranslated): SimpleScopeDto

    @Mappings(
        Mapping(
            source = "nameTranslated",
            target = "name",
        ),
        Mapping(
            source = "scopeProcessSpec",
            target = "scopeProcessSpec",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "scopeTypeSpec",
            target = "scopeTypeSpec",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "scopeFuelSpec",
            target = "scopeFuelSpec",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "scopeMaterialNodes",
            target = "materialNodesCount",
            qualifiedByName = ["material-nodes-count"]
        ),
        Mapping(
            source = "scopeDenominators",
            target = "denominatorsCount",
            qualifiedByName = ["denominators-count"]
        ),
    )
    @TranslatedLanguageMapper
    @CommonMapper
    fun map(entity: VScopeTranslated): ScopeDto

    @Mappings(
        Mapping(
            source = "nameEn",
            target = "name",
        ),
        Mapping(
            source = "scopeProcessSpec",
            target = "scopeProcessSpec",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "scopeTypeSpec",
            target = "scopeTypeSpec",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            target = "scopeFuelSpec",
            source = "scopeFuelSpec",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "scopeMaterialNodes",
            target = "materialNodesCount",
            qualifiedByName = ["material-nodes-count"]
        ),
        Mapping(
            source = "scopeDenominators",
            target = "denominatorsCount",
            qualifiedByName = ["denominators-count"]
        ),
    )
    @EnglishLanguageMapper
    @CommonMapper
    fun mapEn(entity: VScopeTranslated): ScopeDto

    @BeanMapping(qualifiedByName = ["after-map-detail"])
    @Mappings(
        Mapping(
            source = "nameTranslated",
            target = "name",
        ),
        Mapping(target = "materialsIncluded", ignore = true),
        Mapping(
            source = "scopeProcessSpec",
            target = "scopeProcessSpec",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "scopeTypeSpec",
            target = "scopeTypeSpec",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            target = "scopeFuelSpec",
            source = "scopeFuelSpec",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "scopeMaterialNodes",
            target = "scopeMaterialNodes",
            qualifiedBy = [TranslatedLanguageMapper::class]
        ),
        Mapping(target = "scopeDenominators", ignore = true),
        Mapping(target = "scopeAnalysisParams", ignore = true),
        Mapping(target = "materialNodesCount", ignore = true),
        Mapping(target = "denominatorsCount", ignore = true),
        Mapping(target = "nodesIncluded", ignore = true),
    )
    @TranslatedLanguageMapper
    fun mapToDetail(entity: VScopeTranslated): ScopeDetailDto

    @BeanMapping(qualifiedByName = ["after-map-detail"])
    @Mappings(
        Mapping(
            source = "nameEn",
            target = "name",
        ),
        Mapping(target = "materialsIncluded", ignore = true),
        Mapping(
            source = "scopeProcessSpec",
            target = "scopeProcessSpec",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "scopeTypeSpec",
            target = "scopeTypeSpec",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "scopeFuelSpec",
            target = "scopeFuelSpec",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "scopeMaterialNodes",
            target = "scopeMaterialNodes",
            qualifiedBy = [EnglishLanguageMapper::class]
        ),
        Mapping(target = "scopeDenominators", ignore = true),
        Mapping(target = "scopeAnalysisParams", ignore = true),
        Mapping(target = "materialNodesCount", ignore = true),
        Mapping(target = "denominatorsCount", ignore = true),
        Mapping(target = "nodesIncluded", ignore = true),
    )
    @EnglishLanguageMapper
    fun mapToDetailEn(entity: VScopeTranslated): ScopeDetailDto

    @Named("denominators-count")
    fun getDenominatorsCount(denominators: Set<VScopeDenominatorTranslated>): Int {
        return denominators.size
    }

    @Named("material-nodes-count")
    fun getScopeMaterialNodeCount(materialNodes: Set<VScopeMaterialNodeTranslated>): Int {
        return materialNodes.size
    }

    @Named("after-map-detail")
    @AfterMapping
    fun getScopeMaterialNodeDistinctByMaterialId(@MappingTarget scopeDetail: ScopeDetailDto) {
        scopeDetail.materialsIncluded =
            scopeDetail.scopeMaterialNodes.distinctBy { it.materialNode?.material?.id }.mapNotNull {
                it.materialNode?.material
            }
        scopeDetail.nodesIncluded =
            scopeDetail.scopeMaterialNodes.distinctBy { it.materialNode?.node?.id }.mapNotNull {
                it.materialNode?.node
            }
    }

    @Mappings(
        Mapping(
            target = "nameK",
            constant = "${ScopeSuper.translationPrefix}name",
            qualifiedByName = ["scope-name-key"],
        ),
        Mapping(
            source = "scopeProcessSpecId",
            target = "scopeProcessSpec",
        ),
        Mapping(
            source = "scopeTypeSpecId",
            target = "scopeTypeSpec",
        ),
        Mapping(
            source = "scopeFuelSpecId",
            target = "scopeFuelSpec",
        ),
        Mapping(target = "nameTranslations", ignore = true),
        Mapping(target = "scopeMaterialNodes", ignore = true),
        Mapping(target = "scopeAnalysisParams", ignore = true),
        Mapping(target = "scopeDenominators", ignore = true),
        Mapping(target = "id", ignore = true),
    )
    @IgnoreBaseAttributes
    fun map(dto: CreateScopeDto): Scope

    @Mappings(
        Mapping(
            source = "scopeProcessSpecId",
            target = "scopeProcessSpec",
        ),
        Mapping(
            source = "scopeTypeSpecId",
            target = "scopeTypeSpec",
        ),
        Mapping(
            source = "scopeFuelSpecId",
            target = "scopeFuelSpec",
        ),
        Mapping(target = "nameTranslations", ignore = true),
        Mapping(target = "scopeMaterialNodes", ignore = true),
        Mapping(target = "scopeAnalysisParams", ignore = true),
        Mapping(target = "scopeDenominators", ignore = true),
        Mapping(target = "nameK", ignore = true),
        Mapping(target = "id", ignore = true),
    )
    @IgnoreBaseAttributes
    fun update(updateDto: CreateScopeDto, @MappingTarget entity: Scope)

    @Named("scope-name-key")
    fun generateKeyCustom(prefix: String): String {
        return this.generateKey(prefix)
    }
}
