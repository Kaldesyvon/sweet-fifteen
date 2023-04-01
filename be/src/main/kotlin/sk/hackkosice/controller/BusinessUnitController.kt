package sk.esten.uss.gbco2.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.BusinessUnitDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleBusinessUnitDto
import sk.esten.uss.gbco2.generics.controller.*
import sk.esten.uss.gbco2.mapper.BusinessUnitMapper
import sk.esten.uss.gbco2.model.entity.business_unit.BusinessUnit
import sk.esten.uss.gbco2.model.entity.business_unit.VBusinessUnitTranslated
import sk.esten.uss.gbco2.service.BusinessUnitService

@RestController
@RequestMapping(path = ["/business-unit"])
class BusinessUnitController(
    override val crudService: BusinessUnitService,
    val mapper: BusinessUnitMapper
) :
    CreateCrudControllerView<
        BusinessUnit,
        VBusinessUnitTranslated,
        BusinessUnitDto,
        BusinessUnitDto,
        BusinessUnitDto,
        BusinessUnitDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>,
    ReadAllCrudControllerView<
        BusinessUnit,
        VBusinessUnitTranslated,
        BusinessUnitDto,
        SimpleBusinessUnitDto,
        BusinessUnitDto,
        BusinessUnitDto,
        BusinessUnitDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>,
    PageableCrudControllerView<
        BusinessUnit,
        VBusinessUnitTranslated,
        BusinessUnitDto,
        BusinessUnitDto,
        BusinessUnitDto,
        BusinessUnitDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>,
    ReadDetailCrudControllerView<
        BusinessUnit,
        VBusinessUnitTranslated,
        BusinessUnitDto,
        BusinessUnitDto,
        BusinessUnitDto,
        BusinessUnitDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>,
    UpdateCrudControllerView<
        BusinessUnit,
        VBusinessUnitTranslated,
        BusinessUnitDto,
        BusinessUnitDto,
        BusinessUnitDto,
        BusinessUnitDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>,
    DeleteCrudControllerView<
        BusinessUnit,
        VBusinessUnitTranslated,
        BusinessUnitDto,
        BusinessUnitDto,
        BusinessUnitDto,
        BusinessUnitDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto> {

    override fun entityToSimpleDto(
        entity: VBusinessUnitTranslated,
        translated: Boolean
    ): SimpleBusinessUnitDto {
        return if (translated) mapper.mapToSimple(entity) else mapper.mapToSimpleEn(entity)
    }
}
