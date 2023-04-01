package sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic

import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Retention
@Target(AnnotationTarget.FUNCTION)
@Mappings(
    Mapping(target = "created", ignore = true),
    Mapping(target = "createdBy", ignore = true),
    Mapping(target = "modified", ignore = true),
    Mapping(target = "modifiedBy", ignore = true)
)
annotation class IgnoreBaseAttributes
