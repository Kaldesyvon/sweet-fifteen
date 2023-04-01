package sk.esten.uss.gbco2.config.tracing

import java.io.IOException
import java.util.*
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import org.slf4j.MDC
import sk.esten.uss.gbco2.utils.logger

/**
 * Put key {@value TRACE_ID_KEY} to Mapped Diagnostic Context (MDC) with value from request header.
 * If {@value TRACE_ID_KEY} header is not present generated UUID value is used.
 *
 * @see [MDC](https://logback.qos.ch/manual/mdc.html) for a more information about MDC.
 */
class TraceIdFilter : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest: HttpServletRequest = request as HttpServletRequest

        // ignore  for actuator endpoints
        if (!httpRequest.requestURI.startsWith("/actuator") &&
                !httpRequest.requestURI.contains("swagger")
        ) {
            val traceId: String
            if (httpRequest.getHeader(TRACE_ID_KEY) != null) {
                traceId = httpRequest.getHeader(TRACE_ID_KEY)
            } else {
                traceId = UUID.randomUUID().toString()
                logger()
                    .trace(
                        "The header '{}' is not present in this request, use generated: {}",
                        TRACE_ID_KEY,
                        traceId
                    )
            }
            MDC.put(TRACE_ID_KEY, traceId)
        }
        chain.doFilter(httpRequest, response)
    }

    companion object {
        const val TRACE_ID_KEY = "gbco2-trace-id"
    }
}
