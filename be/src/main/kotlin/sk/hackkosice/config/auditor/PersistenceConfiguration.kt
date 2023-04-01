package sk.esten.uss.gbco2.config.auditor

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import sk.esten.uss.gbco2.config.aspect.NoLogging

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@NoLogging
class PersistenceConfiguration {

    @Bean(name = ["auditorProvider"])
    fun auditorProvider(): AuditorAware<String> {
        return AuditorAwareImpl()
    }
}
