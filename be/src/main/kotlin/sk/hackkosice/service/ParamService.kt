package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.ErrorCode
import sk.esten.uss.gbco2.dto.response.ParamDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleParamDto
import sk.esten.uss.gbco2.exceptions.DatabaseException
import sk.esten.uss.gbco2.exceptions.ForbiddenException
import sk.esten.uss.gbco2.exceptions.NotFoundException
import sk.esten.uss.gbco2.exceptions.ValidationException
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.mapper.ParamMapper
import sk.esten.uss.gbco2.model.entity.param.Param
import sk.esten.uss.gbco2.model.repository.param.ParamRepository
import sk.esten.uss.gbco2.utils.logger

@Service
class ParamService(
    override val entityRepository: ParamRepository,
    private val mapper: ParamMapper,
) :
    CrudService<
        Param,
        ParamDto,
        ParamDto,
        ParamDto,
        ParamDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun entityToDto(entity: Param, translated: Boolean): ParamDto {
        return mapper.map(entity, translated)
    }

    override fun createEntity(createDto: ParamDto): Param {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun updateEntity(updateDto: ParamDto, entity: Param) {
        mapper.update(updateDto, entity)
    }

    override fun update(id: Long, updateDto: ParamDto): ParamDto {
        try {
            val entity = getInternal(id)
            if (!entity.editable) {
                throw ValidationException(
                    ErrorCode.CANNOT_BE_UPDATED,
                    "Param with id ${entity.id} cannot be updated"
                )
            }
            return super.update(id, updateDto)
        } catch (e: DataIntegrityViolationException) {
            throw DatabaseException(e)
        }
    }

    override fun entityToDetailDto(entity: Param, translated: Boolean): ParamDto =
        entityToDto(entity, translated)

    fun getParamByCode(code: String): Param =
        entityRepository.getParamByCode(code)
            ?: throw NotFoundException("Param with code [$code] not found!")

    fun getParamValueByCode(code: String): SimpleParamDto? =
        mapper.mapToSimple(getParamByCode(code))

    fun updateParam(param: Param) {
        try {
            entityRepository.save(param)
        } catch (e: ForbiddenException) {
            logger().info("error saving param")
        }
    }
}
