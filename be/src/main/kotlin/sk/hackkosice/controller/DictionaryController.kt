package sk.esten.uss.gbco2.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateDictionaryDto
import sk.esten.uss.gbco2.dto.request.filter.DictionaryFilter
import sk.esten.uss.gbco2.dto.response.DictionaryDto
import sk.esten.uss.gbco2.generics.controller.*
import sk.esten.uss.gbco2.metrics.TimeExecution
import sk.esten.uss.gbco2.model.entity.dictionary.DictionaryFullyMapped
import sk.esten.uss.gbco2.model.entity.dictionary.VDictionaryTranslated
import sk.esten.uss.gbco2.service.DictionaryService

@RestController
@RequestMapping("/dictionary")
class DictionaryController(override val crudService: DictionaryService) :
    PageableCrudControllerView<
        DictionaryFullyMapped,
        VDictionaryTranslated,
        DictionaryDto,
        DictionaryDto,
        CreateDictionaryDto,
        CreateDictionaryDto,
        Long,
        DictionaryFilter,
        ReadAllParamsFilterDto>,
    CreateCrudControllerView<
        DictionaryFullyMapped,
        VDictionaryTranslated,
        DictionaryDto,
        DictionaryDto,
        CreateDictionaryDto,
        CreateDictionaryDto,
        Long,
        DictionaryFilter,
        ReadAllParamsFilterDto>,
    UpdateCrudControllerView<
        DictionaryFullyMapped,
        VDictionaryTranslated,
        DictionaryDto,
        DictionaryDto,
        CreateDictionaryDto,
        CreateDictionaryDto,
        Long,
        DictionaryFilter,
        ReadAllParamsFilterDto>,
    ReadDetailCrudControllerView<
        DictionaryFullyMapped,
        VDictionaryTranslated,
        DictionaryDto,
        DictionaryDto,
        CreateDictionaryDto,
        CreateDictionaryDto,
        Long,
        DictionaryFilter,
        ReadAllParamsFilterDto>,
    DeleteCrudControllerView<
        DictionaryFullyMapped,
        VDictionaryTranslated,
        DictionaryDto,
        DictionaryDto,
        CreateDictionaryDto,
        CreateDictionaryDto,
        Long,
        DictionaryFilter,
        ReadAllParamsFilterDto> {

    @TimeExecution
    @Operation(summary = "get all missing language ids by ID of specified Dictionary")
    @GetMapping(
        value = ["/missing-lang-ids/{dictionaryId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getMissingLanguageIds(@PathVariable dictionaryId: Long): ResponseEntity<List<Long>> {
        return ResponseEntity.ok().body(crudService.getMissingLanguageIds(dictionaryId))
    }
}
