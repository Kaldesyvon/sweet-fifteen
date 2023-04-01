package sk.esten.uss.gbco2.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.CountryDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleCountryDto
import sk.esten.uss.gbco2.generics.controller.*
import sk.esten.uss.gbco2.mapper.CountryMapper
import sk.esten.uss.gbco2.model.entity.country.Country
import sk.esten.uss.gbco2.model.entity.country.VCountryTranslated
import sk.esten.uss.gbco2.service.CountryService

@RestController
@RequestMapping("/country")
class CountryController(
    override val crudService: CountryService,
    private val mapper: CountryMapper
) :
    CreateCrudControllerView<
        Country,
        VCountryTranslated,
        CountryDto,
        CountryDto,
        CountryDto,
        CountryDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>,
    PageableCrudControllerView<
        Country,
        VCountryTranslated,
        CountryDto,
        CountryDto,
        CountryDto,
        CountryDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>,
    ReadDetailCrudControllerView<
        Country,
        VCountryTranslated,
        CountryDto,
        CountryDto,
        CountryDto,
        CountryDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>,
    ReadAllCrudControllerView<
        Country,
        VCountryTranslated,
        CountryDto,
        SimpleCountryDto,
        CountryDto,
        CountryDto,
        CountryDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>,
    UpdateCrudControllerView<
        Country,
        VCountryTranslated,
        CountryDto,
        CountryDto,
        CountryDto,
        CountryDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>,
    DeleteCrudControllerView<
        Country,
        VCountryTranslated,
        CountryDto,
        CountryDto,
        CountryDto,
        CountryDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto> {

    override fun entityToSimpleDto(
        entity: VCountryTranslated,
        translated: Boolean
    ): SimpleCountryDto {
        return if (translated) mapper.mapSimple(entity) else mapper.mapSimpleEn(entity)
    }
}
