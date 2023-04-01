package sk.esten.uss.gbco2.config.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import sk.esten.uss.gbco2.utils.logger

@Component
@Aspect
class LoggingAspect {

    val log = logger()

    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    private fun scheduled() {
        // do nothing
    }

    @Pointcut("execution(* sk.esten.uss.gbco2..*(..))")
    private fun mainPackage() {
        // do nothing
    }

    @Pointcut(
        "@annotation(sk.esten.uss.gbco2.config.aspect.NoLogging) || @within(sk.esten.uss.gbco2.config.aspect.NoLogging)"
    )
    private fun noLogging() {
        // do nothing
        // annotatedMethod || annotatedClass || implementedInterface
    }

    @Around("(mainPackage() && !noLogging()) || scheduled()")
    @Throws(Throwable::class)
    fun logAround(pjp: ProceedingJoinPoint): Any? {
        val start = System.currentTimeMillis()
        log.trace(
            "Method starts: {}::{}",
            pjp.signature.declaringType.simpleName,
            pjp.signature.name,
        )

        val result: Any? = pjp.proceed()

        val duration = System.currentTimeMillis() - start
        log.trace(
            "Method ends: {}::{}, duration: {}ms",
            pjp.signature.declaringType.simpleName,
            pjp.signature.name,
            duration
        )
        return result
    }
}
