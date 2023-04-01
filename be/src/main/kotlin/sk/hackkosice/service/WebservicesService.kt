package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.core.io.Resource
import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.WebserviceDataType
import sk.esten.uss.gbco2.dto.request.filter.WebservicesFilter
import sk.esten.uss.gbco2.dto.response.WebserviceDataDto
import sk.esten.uss.gbco2.dto.response.WebservicesDto
import sk.esten.uss.gbco2.exceptions.NotFoundException
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.mapper.WebservicesMapper
import sk.esten.uss.gbco2.model.entity.webservices.Webservices
import sk.esten.uss.gbco2.model.repository.webservices.WebservicesERepository
import sk.esten.uss.gbco2.model.specification.WebservicesSpecification
import sk.esten.uss.gbco2.service.common.FileManagementService
import sk.esten.uss.gbco2.utils.stringValue

@Service
class WebservicesService(
    override val entityRepository: WebservicesERepository,
    private val mapper: WebservicesMapper,
    private val fileManagementService: FileManagementService,
) :
    CrudService<
        Webservices,
        WebservicesDto,
        WebservicesDto,
        WebservicesDto,
        WebservicesDto,
        Long,
        WebservicesFilter,
        ReadAllParamsFilterDto>() {

    override fun entityToDetailDto(entity: Webservices, translated: Boolean): WebservicesDto =
        entityToDto(entity, translated)

    override fun entityToDto(entity: Webservices, translated: Boolean): WebservicesDto =
        mapper.map(entity)

    override fun createEntity(createDto: WebservicesDto): Webservices =
        throw NotImplementedException("This type of entity cannot be created")

    override fun updateEntity(updateDto: WebservicesDto, entity: Webservices) =
        throw NotImplementedException("This type of entity cannot be updated")

    override fun repositoryPageQuery(filter: WebservicesFilter): Page<Webservices> {
        val specification = WebservicesSpecification(filter)
        return entityRepository.findAll(specification, filter.toPageable())
    }

    fun getData(id: Long): WebserviceDataDto {
        val data =
            entityRepository.findWebservicesData(id)
                ?: throw NotFoundException("Webservices data with id: $id was not found")

        val wsData = WebserviceDataDto()
        wsData.dataSent = data.getDataSent().stringValue()
        wsData.dataReceived = data.getDataReceived().stringValue()
        return wsData
    }

    fun getWebserviceXmlData(id: Long, dataType: WebserviceDataType): ResponseEntity<Resource> {
        val wsData = getInternal(id)

        val dataInputStream =
            when (dataType) {
                WebserviceDataType.SENT -> wsData.dataSent?.byteInputStream()
                WebserviceDataType.RECEIVED -> wsData.dataReceived?.byteInputStream()
            }

        dataInputStream?.use { inputStream ->
            val fileName = "${id}_$dataType.xml"

            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .headers(fileManagementService.getHttpHeaders(fileName))
                .body(fileManagementService.getByteArrayResource(fileName, inputStream.readBytes()))
        }
        throw NotFoundException("Webservices data with id: $id was not found")
    }
}
