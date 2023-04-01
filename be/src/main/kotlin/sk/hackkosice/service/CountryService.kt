package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.CountryDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.CountryMapper
import sk.esten.uss.gbco2.model.entity.country.Country
import sk.esten.uss.gbco2.model.entity.country.VCountryTranslated
import sk.esten.uss.gbco2.model.repository.country.CountryERepository
import sk.esten.uss.gbco2.model.repository.country.CountryVRepository
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
class CountryService(
    override val entityRepository: CountryERepository,
    override val viewRepository: CountryVRepository,
    private val mapper: CountryMapper
) :
    CrudServiceView<
        Country,
        VCountryTranslated,
        CountryDto,
        CountryDto,
        CountryDto,
        CountryDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun viewToDto(entity: VCountryTranslated, translated: Boolean): CountryDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: CountryDto): Country {
        val obj = mapper.map(createDto)
        newEnTranslation(obj.nameK, createDto.name, obj.nameTranslations)
        return obj
    }

    override fun updateEntity(updateDto: CountryDto, entity: Country) {
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.nameTranslations)
    }

    override fun viewToDetailDto(entity: VCountryTranslated, translated: Boolean): CountryDto =
        viewToDto(entity, translated)
}
