package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.RoleDto
import sk.esten.uss.gbco2.exceptions.ForbiddenException
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.RoleMapper
import sk.esten.uss.gbco2.model.entity.role.Role
import sk.esten.uss.gbco2.model.entity.role.VRoleTranslated
import sk.esten.uss.gbco2.model.repository.role.RoleERepository
import sk.esten.uss.gbco2.model.repository.role.RoleVRepository

@Service
class RoleService(
    override val entityRepository: RoleERepository,
    override val viewRepository: RoleVRepository,
    private val mapper: RoleMapper,
) :
    CrudServiceView<
        Role,
        VRoleTranslated,
        RoleDto,
        RoleDto,
        RoleDto,
        RoleDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun viewToDto(entity: VRoleTranslated, translated: Boolean): RoleDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: RoleDto): Role {
        throw ForbiddenException("Create method for entity Role is not allowed")
    }

    override fun updateEntity(updateDto: RoleDto, entity: Role) {
        throw ForbiddenException("Update method for entity Role is not allowed")
    }

    override fun viewToDetailDto(entity: VRoleTranslated, translated: Boolean): RoleDto =
        viewToDto(entity, translated)
}
