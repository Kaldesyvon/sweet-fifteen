package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.filter.EmissionFactorFilter
import sk.esten.uss.gbco2.dto.response.emission_factor.EmissionFactorDto
import sk.esten.uss.gbco2.model.repository.emission_factor.AdvEmissionFactorRepository

@Service
class EmissionFactorService(
    private val advEmissionFactorRepository: AdvEmissionFactorRepository,
) {

    fun getEmissionFactors(filter: EmissionFactorFilter): EmissionFactorDto =
        advEmissionFactorRepository.getEmissionFactorsPageAndSum(filter)
}
