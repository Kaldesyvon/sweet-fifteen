package sk.esten.uss.gbco2.config.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import sk.esten.uss.gbco2.dto.response.ErrorCode
import sk.esten.uss.gbco2.dto.response.ErrorDto

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = "application/json; charset=utf-8"
        response.setHeader("Accept-Encoding", "identity")
        response.setHeader("Accept", "application/json")
        response.writer.write(
            ObjectMapper()
                .writeValueAsString(
                    ErrorDto().apply {
                        code = ErrorCode.UNAUTHORIZED
                        description = "Unauthorized"
                    }
                )
        )
    }
}
