package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateMaterialNodeAnalysisParamDto
import sk.esten.uss.gbco2.dto.response.MaterialNodeAnalysisParamDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.material_node_analysis_param.MaterialNodeAnalysisParam
import sk.esten.uss.gbco2.model.entity.material_node_analysis_param.VMaterialNodeAnalysisParamTranslated
import sk.esten.uss.gbco2.service.AnalysisParamExprService
import sk.esten.uss.gbco2.service.AnalysisParamService
import sk.esten.uss.gbco2.service.MaterialNodeService
import sk.esten.uss.gbco2.service.MaterialService
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses =
        [
            AnalysisParamMapper::class,
            AnalysisParamExprMapper::class,
            MaterialNodeMapper::class,
            AnalysisParamExprService::class,
            MaterialService::class,
            AnalysisParamService::class,
            MaterialNodeService::class]
)
interface MaterialNodeAnalysisParamMapper {

    @Mappings(
        Mapping(
            source = "analysisParam",
            target = "analysisParam",
            qualifiedBy = [TranslatedLanguageMapper::class, SimpleMapper::class]
        ),
        Mapping(
            source = "materialNodeAdvBasis",
            target = "materialNodeAdvBasis",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "analysisParamExpr",
            target = "analysisParamExpr",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(target = "materialNode", ignore = true)
    )
    @TranslatedLanguageMapper
    fun map(entity: VMaterialNodeAnalysisParamTranslated): MaterialNodeAnalysisParamDto

    @Mappings(
        Mapping(
            source = "analysisParam",
            target = "analysisParam",
            qualifiedBy = [SimpleMapper::class, EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "materialNodeAdvBasis",
            target = "materialNodeAdvBasis",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "analysisParamExpr",
            target = "analysisParamExpr",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(target = "materialNode", ignore = true)
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VMaterialNodeAnalysisParamTranslated): MaterialNodeAnalysisParamDto

    @IterableMapping(qualifiedBy = [TranslatedLanguageMapper::class])
    @TranslatedLanguageMapper
    fun map(entity: Set<VMaterialNodeAnalysisParamTranslated>): List<MaterialNodeAnalysisParamDto>

    @IterableMapping(qualifiedBy = [EnglishLanguageMapper::class])
    @EnglishLanguageMapper
    fun mapEn(entity: Set<VMaterialNodeAnalysisParamTranslated>): List<MaterialNodeAnalysisParamDto>

    @Named("material-node-adv-basis")
    fun materialNodeAdvBasis(materialNodeAdvBasisId: Long?, @Context processAdv: Boolean): Long? {
        if (processAdv) {
            return materialNodeAdvBasisId
        }
        return null
    }

    @Mappings(
        Mapping(source = "analysisParamId", target = "analysisParam"),
        Mapping(
            source = "materialNodeAdvBasisId",
            target = "materialNodeAdvBasis",
            qualifiedByName = ["material-node-adv-basis"]
        ),
        Mapping(source = "analysisParamExprId", target = "analysisParamExpr"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "objVersion", ignore = true),
        Mapping(target = "materialNode", ignore = true),
    )
    @IgnoreBaseAttributes
    fun map(
        createDto: CreateMaterialNodeAnalysisParamDto,
        @Context processAdv: Boolean
    ): MaterialNodeAnalysisParam
}
