package sk.esten.uss.gbco2.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.EmailFilter
import sk.esten.uss.gbco2.dto.response.EmailDto
import sk.esten.uss.gbco2.exceptions.NotFoundException
import sk.esten.uss.gbco2.generics.controller.PageableCrudController
import sk.esten.uss.gbco2.generics.controller.ReadDetailCrudController
import sk.esten.uss.gbco2.metrics.TimeExecution
import sk.esten.uss.gbco2.model.entity.email.Email
import sk.esten.uss.gbco2.service.EmailService
import sk.esten.uss.gbco2.service.common.FileManagementService

@RestController
@RequestMapping("/email")
class EmailController(
    override val crudService: EmailService,
    private val fileService: FileManagementService
) :
    PageableCrudController<
        Email, EmailDto, EmailDto, EmailDto, EmailDto, Long, EmailFilter, ReadAllParamsFilterDto>,
    ReadDetailCrudController<
        Email, EmailDto, EmailDto, EmailDto, EmailDto, Long, EmailFilter, ReadAllParamsFilterDto> {

    @TimeExecution
    @Operation(summary = "get attachment content")
    @GetMapping(
        value = ["/{id}/attachment"],
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    fun getAttachment(@PathVariable id: Long): ResponseEntity<Resource> {
        val email = crudService.getAttachment(id)

        if (email.hasAttachment == true) {
            return fileService.getFileContentToDownload(email)
        }
        throw NotFoundException("Attachment not found!")
    }
}
