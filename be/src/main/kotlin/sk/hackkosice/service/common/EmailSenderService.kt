package sk.esten.uss.gbco2.service.common

import java.nio.charset.StandardCharsets
import java.util.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import sk.esten.uss.gbco2.model.entity.param.Param
import sk.esten.uss.gbco2.service.ParamService
import sk.esten.uss.gbco2.utils.logger

@Component
class EmailSenderService(
    val javaMailSender: JavaMailSender,
    val paramService: ParamService,
    @Value("\${spring.mail.sender}") val from: String
) {

    companion object {
        const val HTML_NEW_LINE = "<br/>"
    }

    fun sendEmail(
        subject: String,
        body: String,
        to: List<String>,
        attachments: List<GbcoEmailAttachment> = listOf()
    ): Boolean {
        return sendEmail(subject, body, to, listOf(), listOf(), attachments)
    }

    fun sendEmail(
        subject: String,
        _body: String,
        _to: List<String>,
        cc: List<String> = listOf(),
        bcc: List<String> = listOf(),
        attachments: List<GbcoEmailAttachment> = listOf()
    ): Boolean {
        try {

            var body = _body
            var to = _to

            if ("1" != paramService.getParamByCode(Param.EMAIL_NOTI_ENABLED).value) {
                logger()
                    .info(
                        "Email notification is disabled, please set param: ${Param.EMAIL_NOTI_ENABLED} to 1"
                    )
                return false
            }

            val testEmailRecipients =
                paramService
                    .getParamByCode(Param.EMAIL_NOTI_TEST_ADDRESS)
                    .value
                    ?.split(";")
                    .orEmpty()
            if (testEmailRecipients.isNotEmpty()) {

                body =
                    """$body
                    $HTML_NEW_LINE
                    $HTML_NEW_LINE
                    This email was sent to test email address, original recipients:
                    $HTML_NEW_LINE
                    TO: ${to.joinToString("; ")}
                    $HTML_NEW_LINE
                    CC: ${cc.joinToString("; ")}
                    $HTML_NEW_LINE
                    BCC: ${bcc.joinToString("; ")}"""

                to = testEmailRecipients
            }

            val mimeMessage = javaMailSender.createMimeMessage()

            val mimeMessageHelper =
                MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED,
                    StandardCharsets.UTF_8.name()
                )

            mimeMessageHelper.setFrom(from)
            mimeMessageHelper.setTo(to.toTypedArray())
            mimeMessageHelper.setCc(cc.toTypedArray())
            mimeMessageHelper.setBcc(bcc.toTypedArray())
            mimeMessageHelper.setSubject(subject)
            mimeMessageHelper.setText(body, true)

            attachments.forEach { mimeMessageHelper.addAttachment(it.filename, it.inputStream) }

            javaMailSender.send(mimeMessageHelper.mimeMessage)

            return true
        } catch (e: Exception) {
            logger().error("Error sending email", e)
            return false
        }
    }

    class GbcoEmailAttachment(val filename: String, val inputStream: Resource)
}
