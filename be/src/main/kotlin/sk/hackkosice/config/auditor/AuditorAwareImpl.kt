package sk.esten.uss.gbco2.config.auditor

import java.util.*
import org.springframework.data.domain.AuditorAware
import sk.esten.uss.gbco2.utils.principal

open class AuditorAwareImpl : AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> {
        return Optional.ofNullable(principal()?.loginAd)
    }
}
