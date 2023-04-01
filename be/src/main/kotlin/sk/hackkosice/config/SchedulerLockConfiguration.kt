package sk.esten.uss.gbco2.config

import javax.sql.DataSource
import net.javacrumbs.shedlock.core.LockProvider
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "30m")
class SchedulerLockConfiguration {

    @Bean
    fun lockProvider(dataSource: DataSource): LockProvider {
        return JdbcTemplateLockProvider(jdbcTemplate(dataSource))
    }

    fun jdbcTemplate(dataSource: DataSource): JdbcTemplateLockProvider.Configuration {
        return JdbcTemplateLockProvider.Configuration.builder()
            .withTableName("GBC_SCHEDULED_SEMAFOR")
            .withColumnNames(
                JdbcTemplateLockProvider.ColumnNames(
                    "JOB_NAME",
                    "LOCK_UNTIL",
                    "LOCKED_AT",
                    "LOCKED_BY"
                )
            )
            .usingDbTime()
            .withJdbcTemplate(JdbcTemplate(dataSource))
            .build()
    }
}
