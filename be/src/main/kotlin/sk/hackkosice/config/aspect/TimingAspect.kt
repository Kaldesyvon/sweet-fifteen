package sk.esten.uss.gbco2.config.aspect

import io.micrometer.core.instrument.Tag
import java.time.Duration
import java.time.Instant
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import sk.esten.uss.gbco2.metrics.MetricsService

@Aspect
@Component
class TimingAspect(val metricsService: MetricsService) {

    @Around("@annotation(sk.esten.uss.gbco2.metrics.TimeExecution)")
    @Throws(Throwable::class)
    fun time(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature as MethodSignature
        val methodName = "${signature.declaringType.simpleName}#${signature.name}"
        val timer =
            metricsService.createTimer(
                MetricsService.MetricsName.EXECUTION,
                listOf(Tag.of("method", methodName))
            )
        val before = Instant.now()
        val result: Any? = joinPoint.proceed()
        val after = Instant.now()
        timer.record(Duration.between(before, after))
        return result
    }
}
