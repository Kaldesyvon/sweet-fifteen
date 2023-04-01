package sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic

import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Retention
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Mappings(
    Mapping(target = "nameTranslations", ignore = true),
    Mapping(target = "nameK", ignore = true)
)
annotation class IgnoreNameTranslationsAndNameKeys
