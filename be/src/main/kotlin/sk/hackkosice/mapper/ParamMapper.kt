package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.ParamDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleParamDto
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.param.Param

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface ParamMapper : TranslationMapper {

    @Mapping(
        target = "memo",
        expression = "java(translate(entity.getCode(), entity.getTranslations(), translated))"
    )
    fun map(entity: Param, translated: Boolean): ParamDto

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(target = "translations", ignore = true)
    )
    fun update(dto: ParamDto, @MappingTarget entity: Param)

    fun mapToSimple(entity: Param): SimpleParamDto
}
