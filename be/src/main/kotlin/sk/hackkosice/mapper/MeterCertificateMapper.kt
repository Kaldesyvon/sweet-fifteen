package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import org.springframework.web.multipart.MultipartFile
import sk.esten.uss.gbco2.dto.response.MeterCertificateDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMeterCertificateDto
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.meter_certificate.MeterCertificate
import sk.esten.uss.gbco2.model.entity.meter_certificate.MeterCertificateWithAttachment
import sk.esten.uss.gbco2.service.MeterService
import sk.esten.uss.gbco2.utils.Constants

@Mapper(
    componentModel = Constants.MAPPING_COMPONENT_MODEL_SPRING_LAZY,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses = [MeterService::class]
)
interface MeterCertificateMapper {

    @IgnoreBaseAttributes
    @Mappings(Mapping(target = "id", ignore = true), Mapping(source = "meterId", target = "meter"))
    fun map(dto: MeterCertificateDto): MeterCertificate

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(source = "dto.meterId", target = "meter"),
        Mapping(source = "dto.name", target = "name"),
        Mapping(source = "file.originalFilename", target = "documentName"),
        Mapping(source = "file", target = "document", qualifiedByName = ["content"]),
        Mapping(source = "file.contentType", target = "documentContentType")
    )
    fun map(dto: MeterCertificateDto, file: MultipartFile): MeterCertificateWithAttachment

    @Mappings(
        Mapping(source = "entity.meter.id", target = "meterId"),
    )
    fun map(entity: MeterCertificate): MeterCertificateDto

    @Mappings(
        Mapping(source = "entity.meter.id", target = "meterId"),
    )
    fun map(entity: MeterCertificateWithAttachment): MeterCertificateDto

    @Mappings(
        Mapping(source = "entity.meter.id", target = "meterId"),
    )
    fun map(entity: Set<MeterCertificate>): List<MeterCertificateDto>

    @IgnoreBaseAttributes
    @Mappings(Mapping(target = "id", ignore = true), Mapping(source = "meterId", target = "meter"))
    fun update(dto: MeterCertificateDto, @MappingTarget entity: MeterCertificate)

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(source = "file.originalFilename", target = "documentName"),
        Mapping(source = "file", target = "document", qualifiedByName = ["content"]),
        Mapping(source = "file.contentType", target = "documentContentType"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "name", ignore = true),
        Mapping(target = "meter", ignore = true),
        Mapping(target = "validFrom", ignore = true),
        Mapping(target = "validTo", ignore = true),
        Mapping(target = "memo", ignore = true),
        Mapping(target = "objVersion", ignore = true)
    )
    fun update(file: MultipartFile, @MappingTarget entity: MeterCertificateWithAttachment)

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(target = "documentContentType", ignore = true),
        Mapping(target = "documentName", ignore = true),
        Mapping(target = "document", ignore = true),
        Mapping(target = "meter", source = "meterId")
    )
    fun update(dto: MeterCertificateDto, @MappingTarget entity: MeterCertificateWithAttachment)

    @SimpleMapper fun mapSimple(entity: MeterCertificate): SimpleMeterCertificateDto

    @IterableMapping(qualifiedBy = [SimpleMapper::class])
    @SimpleMapper
    fun mapSimple(entity: Set<MeterCertificate>): List<SimpleMeterCertificateDto>

    @Named("content")
    fun getContent(file: MultipartFile): ByteArray {
        return file.bytes
    }
}
