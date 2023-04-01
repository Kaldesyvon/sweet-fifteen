package sk.esten.uss.gbco2.mapper

import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import org.mapstruct.ReportingPolicy
import sk.esten.uss.gbco2.dto.response.LoginDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleLoginDto
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.model.entity.login.Login

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface LoginMapper {

    @SimpleMapper fun mapSimple(entity: Login): SimpleLoginDto

    fun map(entity: Login): LoginDto
}
