package sk.esten.uss.gbco2.mapper.annotation.ignore_mapping

import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Retention
@Target(AnnotationTarget.FUNCTION)
@Mappings(
    Mapping(target = "id", ignore = true),
    Mapping(target = "gpsPosition", ignore = true),
    Mapping(target = "objDiscrim", ignore = true),
    Mapping(target = "lockEnabled", ignore = true),
    Mapping(target = "tier", ignore = true),
    Mapping(target = "city", ignore = true),
    Mapping(target = "timezone", ignore = true),
    Mapping(target = "stateProvince", ignore = true),
    Mapping(target = "materialNodes", ignore = true),
    Mapping(target = "translations", ignore = true),
    Mapping(target = "fuelType", ignore = true),
    Mapping(target = "subNode", ignore = true)
)
annotation class IgnoreNodeAttributes
