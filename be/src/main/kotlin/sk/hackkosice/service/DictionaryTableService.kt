package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.DictionaryTableDto
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.mapper.DictionaryTableMapper
import sk.esten.uss.gbco2.model.entity.dictionary_table.DictionaryTable
import sk.esten.uss.gbco2.model.repository.dictionary_table.DictionaryTableRepository

@Service
class DictionaryTableService(
    override val entityRepository: DictionaryTableRepository,
    private val mapper: DictionaryTableMapper,
) :
    CrudService<
        DictionaryTable,
        DictionaryTableDto,
        DictionaryTableDto,
        DictionaryTableDto,
        DictionaryTableDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun entityToDto(entity: DictionaryTable, translated: Boolean): DictionaryTableDto {
        return mapper.map(entity)
    }

    override fun createEntity(createDto: DictionaryTableDto): DictionaryTable {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun updateEntity(updateDto: DictionaryTableDto, entity: DictionaryTable) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    override fun entityToDetailDto(
        entity: DictionaryTable,
        translated: Boolean
    ): DictionaryTableDto = entityToDto(entity, translated)
}
