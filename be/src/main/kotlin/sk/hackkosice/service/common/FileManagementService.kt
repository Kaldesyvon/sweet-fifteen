package sk.esten.uss.gbco2.service.common

import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.model.entity.generic.EntityWithAttachment
import sk.esten.uss.gbco2.utils.detectContentType
import sk.esten.uss.gbco2.utils.getInputStream

@Service
class FileManagementService {
    fun getFileContentToDownload(entity: EntityWithAttachment): ResponseEntity<Resource> {
        val fileName = entity.getFileName().orEmpty()
        val bytes = entity.getContent() ?: byteArrayOf()

        val contentType =
            (entity.getContentType() ?: "application/octet-stream").detectContentType()
        val headers = getHttpHeaders(fileName)
        val content: ByteArrayResource = getByteArrayResource(fileName, bytes)

        return ResponseEntity.ok().contentType(contentType).headers(headers).body(content)
    }

    fun getStaticFile(fileName: String): ResponseEntity<Resource> {
        val fileInputStream = "static/$fileName".getInputStream(this::class.java.classLoader)

        fileInputStream.use { inputStream ->
            val bytes: ByteArray = inputStream.readBytes()

            val contentType: MediaType = bytes.detectContentType()
            val headers = getHttpHeaders(fileName)
            val content = getByteArrayResource(fileName, bytes)

            return ResponseEntity.ok().contentType(contentType).headers(headers).body(content)
        }
    }

    internal fun getHttpHeaders(fileName: String): HttpHeaders =
        HttpHeaders().apply {
            contentDisposition = ContentDisposition.attachment().filename(fileName).build()
        }

    internal fun getByteArrayResource(fileName: String, bytes: ByteArray): ByteArrayResource =
        object : ByteArrayResource(bytes) {
            override fun getFilename(): String = fileName
        }
}
