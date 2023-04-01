package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.filter.UnitConversionFilter
import sk.esten.uss.gbco2.dto.response.UnitConversionDto
import sk.esten.uss.gbco2.mapper.UnitTypeMapper
import sk.esten.uss.gbco2.model.repository.unit.UnitVRepository
import sk.esten.uss.gbco2.utils.principalLangOrEn

@Service
class UnitConversionService(
    val repository: UnitVRepository,
    private val mapper: UnitTypeMapper,
) {

    fun getListOfConvertedUnits(filter: UnitConversionFilter): List<UnitConversionDto> {
        val listOfConvertedUnits =
            repository.getListOfConvertedUnits(filter.unitTypeId, principalLangOrEn().id)
        return listOfConvertedUnits.filter { it.id != filter.unitId }.map { unit ->
            mapper.mapConversion(
                filter.quantity,
                listOfConvertedUnits.find { it.id == filter.unitId },
                unit
            )
        }
    }
}
