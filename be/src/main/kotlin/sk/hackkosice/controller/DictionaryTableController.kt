package sk.esten.uss.gbco2.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.DictionaryTableDto
import sk.esten.uss.gbco2.generics.controller.ReadAllCrudController
import sk.esten.uss.gbco2.model.entity.dictionary_table.DictionaryTable
import sk.esten.uss.gbco2.service.DictionaryTableService

@RestController
@RequestMapping("/dictionary-table")
class DictionaryTableController(override val crudService: DictionaryTableService) :
    ReadAllCrudController<
        DictionaryTable,
        DictionaryTableDto,
        DictionaryTableDto,
        DictionaryTableDto,
        DictionaryTableDto,
        DictionaryTableDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto> {

    override fun entityToSimpleDto(
        entity: DictionaryTable,
        translated: Boolean
    ): DictionaryTableDto {
        return crudService.entityToDto(entity)
    }
}
