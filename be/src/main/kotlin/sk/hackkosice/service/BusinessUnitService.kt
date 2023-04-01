package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.BusinessUnitDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.BusinessUnitMapper
import sk.esten.uss.gbco2.model.entity.business_unit.BusinessUnit
import sk.esten.uss.gbco2.model.entity.business_unit.VBusinessUnitTranslated
import sk.esten.uss.gbco2.model.repository.business_unit.BusinessUnitERepository
import sk.esten.uss.gbco2.model.repository.business_unit.BusinessUnitVRepository
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
class BusinessUnitService(
    override val entityRepository: BusinessUnitERepository,
    override val viewRepository: BusinessUnitVRepository,
    private val mapper: BusinessUnitMapper,
) :
    CrudServiceView<
        BusinessUnit,
        VBusinessUnitTranslated,
        BusinessUnitDto,
        BusinessUnitDto,
        BusinessUnitDto,
        BusinessUnitDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun viewToDto(entity: VBusinessUnitTranslated, translated: Boolean): BusinessUnitDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: BusinessUnitDto): BusinessUnit {
        val obj = mapper.map(createDto)
        newEnTranslation(obj.nameK, createDto.name, obj.translations)
        return obj
    }

    override fun updateEntity(updateDto: BusinessUnitDto, entity: BusinessUnit) {
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.translations)
    }

    override fun viewToDetailDto(
        entity: VBusinessUnitTranslated,
        translated: Boolean
    ): BusinessUnitDto = viewToDto(entity, translated)
}
