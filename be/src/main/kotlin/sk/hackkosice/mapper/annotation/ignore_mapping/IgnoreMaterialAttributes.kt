package sk.esten.uss.gbco2.mapper.annotation.ignore_mapping

import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Retention
@Target(AnnotationTarget.FUNCTION)
@Mappings(
    Mapping(target = "id", ignore = true),
    Mapping(target = "materialNodes", ignore = true),
    Mapping(target = "subMaterial", ignore = true)
)
annotation class IgnoreMaterialAttributes
