package sk.esten.uss.gbco2.mapper.annotation.ignore_mapping

import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Retention
@Target(AnnotationTarget.FUNCTION)
@Mappings(
    Mapping(target = "analysisParamExpressions", ignore = true),
    Mapping(target = "unitSetSettingsAps", ignore = true),
    Mapping(target = "parentKoef", ignore = true),
    Mapping(target = "parent", ignore = true),
)
annotation class IgnoreAnalysisParamAttributes
