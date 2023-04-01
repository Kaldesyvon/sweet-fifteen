package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.EmailFilter
import sk.esten.uss.gbco2.dto.response.EmailDto
import sk.esten.uss.gbco2.exceptions.NotFoundException
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.mapper.EmailMapper
import sk.esten.uss.gbco2.model.entity.email.Email
import sk.esten.uss.gbco2.model.entity.email.EmailWithAttachment
import sk.esten.uss.gbco2.model.repository.email.EmailRepository
import sk.esten.uss.gbco2.model.repository.email.EmailWithAttachmentRepository
import sk.esten.uss.gbco2.model.specification.EmailSpecification
import sk.esten.uss.gbco2.service.common.EmailSenderService

@Service
class EmailService(
    override val entityRepository: EmailRepository,
    val emailWithAttachmentRepository: EmailWithAttachmentRepository,
    val mapper: EmailMapper
) :
    CrudService<
        Email,
        EmailDto,
        EmailDto,
        EmailDto,
        EmailDto,
        Long,
        EmailFilter,
        ReadAllParamsFilterDto>() {

    override fun entityToDto(entity: Email, translated: Boolean): EmailDto {
        return mapper.map(entity)
    }

    override fun createEntity(createDto: EmailDto): Email {
        TODO("Not yet implemented")
    }

    override fun updateEntity(updateDto: EmailDto, entity: Email) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    override fun repositoryPageQuery(filter: EmailFilter): Page<Email> {
        return entityRepository.findAll(EmailSpecification(filter), filter.toPageable())
    }

    @Transactional(readOnly = true)
    fun getAttachment(id: Long): EmailWithAttachment {
        return getAttachmentInternal(id)
    }
    private fun getAttachmentInternal(id: Long): EmailWithAttachment {
        return emailWithAttachmentRepository.findByIdOrNull(id) ?: throw NotFoundException()
    }

    override fun entityToDetailDto(entity: Email, translated: Boolean): EmailDto =
        entityToDto(entity, translated)

    @Transactional
    fun saveEmail(
        email: String,
        subject: String,
        body: String,
        attachment: EmailSenderService.GbcoEmailAttachment,
        attachmentName: String,
        loginAd: String?
    ) {
        val emailWithAttachment =
            EmailWithAttachment().apply {
                this.email = email
                this.subject = subject
                this.body = body
                this.login = loginAd
                this.attachmentName = attachmentName
                this.attachment = attachment.inputStream.file.readBytes()
            }
        emailWithAttachmentRepository.save(emailWithAttachment)
    }
}
