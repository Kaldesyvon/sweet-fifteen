package sk.esten.uss.gbco2.metrics

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Service

@Service
class MetricsService(private val registry: MeterRegistry) {
    /**
     * Returns either an existing [io.micrometer.core.instrument.Timer] or creates a new one based
     * on the name provided. The name of the timer will in both cases be prefixed with
     * [MetricsService.METRICS_NAME_PREFIX]. This method additionally allows to add
     * [io.micrometer.core.instrument.Tag]s to the timer.
     *
     * @param tags [io.micrometer.core.instrument.Tag]s to be applied to the timer
     * @return existing [io.micrometer.core.instrument.Timer] or newly created one
     */
    fun createTimer(name: MetricsName = MetricsName.EXECUTION, tags: List<Tag>): Timer {
        val fullName = METRICS_NAME_PREFIX + name.name.lowercase()
        return registry.find(fullName).tags(tags).timer()
            ?: Timer.builder(fullName).tags(tags).register(registry)
    }

    /**
     * Returns either an existing [Counter] or creates a new one based on the name provided. The
     * name of the counter will in both cases be prefixed with [MetricsService.METRICS_NAME_PREFIX]
     *
     * @param name the [MetricsName] of the counter
     * @return existing [Counter] or newly created one
     */
    fun getOrCreateCounter(name: MetricsName = MetricsName.ERROR, tags: List<Tag>): Counter {
        val fullName = METRICS_NAME_PREFIX + name.name.lowercase()
        return registry.find(fullName).tags(tags).counter()
            ?: Counter.builder(fullName).tags(tags).register(registry)
    }

    enum class MetricsName {
        EXECUTION,
        ERROR
    }

    companion object {
        private const val METRICS_NAME_PREFIX = "sk.esten.uss.gbco2."
    }
}
