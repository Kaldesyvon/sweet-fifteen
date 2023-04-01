package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.UnitTypeDto
import sk.esten.uss.gbco2.generics.model.repository.CrudViewRepository
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.UnitTypeMapper
import sk.esten.uss.gbco2.model.entity.unit_type.UnitType
import sk.esten.uss.gbco2.model.entity.unit_type.VUnitTypeTranslated
import sk.esten.uss.gbco2.model.repository.unit_type.UnitTypeERepository
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
class UnitTypeService(
    override val entityRepository: UnitTypeERepository,
    override val viewRepository: CrudViewRepository<VUnitTypeTranslated, Long>,
    private val mapper: UnitTypeMapper,
) :
    CrudServiceView<
        UnitType,
        VUnitTypeTranslated,
        UnitTypeDto,
        UnitTypeDto,
        UnitTypeDto,
        UnitTypeDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {
    override fun createEntity(createDto: UnitTypeDto): UnitType {
        val entity = mapper.map(createDto)
        newEnTranslation(entity.nameK, createDto.name, entity.nameTranslations)
        newEnTranslation(entity.memoK, createDto.memo, entity.memoTranslations)
        return entity
    }

    override fun updateEntity(updateDto: UnitTypeDto, entity: UnitType) {
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.nameTranslations)
        updateEnTranslation(updateDto.memo, entity.memoTranslations)
    }

    override fun viewToDto(entity: VUnitTypeTranslated, translated: Boolean): UnitTypeDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun viewToDetailDto(entity: VUnitTypeTranslated, translated: Boolean): UnitTypeDto =
        viewToDto(entity, translated)
}
