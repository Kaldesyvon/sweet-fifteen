package sk.esten.uss.gbco2.mapper.annotation.ignore_mapping

import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mappings(
    Mapping(target = "materialNode", ignore = true),
    Mapping(target = "analysisParam", ignore = true),
    Mapping(target = "mdlA", ignore = true),
    Mapping(target = "formattedFactorA", ignore = true),
    Mapping(target = "formattedFactorB", ignore = true),
    Mapping(target = "formattedFactorC", ignore = true),
    Mapping(target = "formattedUnitAbbrTo", ignore = true),
    Mapping(target = "materialAnalysisParams", ignore = true),
)
annotation class IgnoreQuantityAttributes
