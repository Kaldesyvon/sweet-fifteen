package sk.esten.uss.gbco2.service

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.MeterCertificateDto
import sk.esten.uss.gbco2.exceptions.DatabaseException
import sk.esten.uss.gbco2.exceptions.NotFoundException
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.mapper.MeterCertificateMapper
import sk.esten.uss.gbco2.model.entity.meter_certificate.MeterCertificate
import sk.esten.uss.gbco2.model.entity.meter_certificate.MeterCertificateWithAttachment
import sk.esten.uss.gbco2.model.repository.meter_certificate.MeterCertificateRepository
import sk.esten.uss.gbco2.model.repository.meter_certificate.MeterCertificateWithAttachmentRepository
import sk.esten.uss.gbco2.model.specification.MeterCertificateSpecification

@Service
class MeterCertificateService(
    override val entityRepository: MeterCertificateRepository,
    private val entityWithAttachmentRepository: MeterCertificateWithAttachmentRepository,
    private val mapper: MeterCertificateMapper
) :
    CrudService<
        MeterCertificate,
        MeterCertificateDto,
        MeterCertificateDto,
        MeterCertificateDto,
        MeterCertificateDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun entityToDto(entity: MeterCertificate, translated: Boolean): MeterCertificateDto {
        return mapper.map(entity)
    }

    override fun createEntity(createDto: MeterCertificateDto): MeterCertificate {
        return mapper.map(createDto)
    }

    override fun updateEntity(updateDto: MeterCertificateDto, entity: MeterCertificate) {
        return mapper.update(updateDto, entity)
    }

    fun getCertificatesByMeter(meterId: Long): List<MeterCertificate> {
        return entityRepository.findAll(
            MeterCertificateSpecification(meterId),
            Sort.by("id").ascending()
        )
    }

    fun getEntityWithAttachment(id: Long): MeterCertificateWithAttachment {
        return entityWithAttachmentRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Entity: $id not found")
    }

    @Transactional
    fun uploadFileContent(
        meterCertificateId: Long,
        file: MultipartFile
    ): MeterCertificateWithAttachment {
        try {
            val meterCertificateWithAttachment = getEntityWithAttachment(meterCertificateId)
            mapper.update(file, meterCertificateWithAttachment)
            return entityWithAttachmentRepository.save(meterCertificateWithAttachment)
        } catch (e: DataIntegrityViolationException) {
            throw DatabaseException(e)
        }
    }

    override fun entityToDetailDto(
        entity: MeterCertificate,
        translated: Boolean
    ): MeterCertificateDto = entityToDto(entity, translated)
}
