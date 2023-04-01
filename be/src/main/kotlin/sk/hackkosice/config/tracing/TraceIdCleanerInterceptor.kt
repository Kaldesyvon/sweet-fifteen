package sk.esten.uss.gbco2.config.tracing

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.web.servlet.HandlerInterceptor
import sk.esten.uss.gbco2.config.aspect.NoLogging

@NoLogging
/** Interceptor for cleaning MDC at the end of the request. */
open class TraceIdCleanerInterceptor : HandlerInterceptor {

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        MDC.remove(TraceIdFilter.TRACE_ID_KEY)
    }
}
