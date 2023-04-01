package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.FormulaCalculationFilter
import sk.esten.uss.gbco2.dto.response.FormulaCalculationDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleFormulaCalculationDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.FormulaCalculationMapper
import sk.esten.uss.gbco2.model.entity.formula_calculation.FormulaCalculation
import sk.esten.uss.gbco2.model.entity.formula_calculation.VFormulaCalculationTranslated
import sk.esten.uss.gbco2.model.repository.formula_calculation.FormulaCalculationERepository
import sk.esten.uss.gbco2.model.repository.formula_calculation.FormulaCalculationVRepository
import sk.esten.uss.gbco2.model.specification.FormulaCalculationSpecification
import sk.esten.uss.gbco2.utils.principalLangOrEn
import sk.esten.uss.gbco2.utils.principalUnitSetIdOrMetricId

@Service
class FormulaCalculationService(
    override val entityRepository: FormulaCalculationERepository,
    override val viewRepository: FormulaCalculationVRepository,
    private val mapper: FormulaCalculationMapper,
) :
    CrudServiceView<
        FormulaCalculation,
        VFormulaCalculationTranslated,
        FormulaCalculationDto,
        FormulaCalculationDto,
        FormulaCalculationDto,
        FormulaCalculationDto,
        Long,
        FormulaCalculationFilter,
        ReadAllParamsFilterDto>() {
    override fun viewToDto(
        entity: VFormulaCalculationTranslated,
        translated: Boolean
    ): FormulaCalculationDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: FormulaCalculationDto): FormulaCalculation {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun updateEntity(updateDto: FormulaCalculationDto, entity: FormulaCalculation) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    override fun repositoryPageQuery(
        filter: FormulaCalculationFilter
    ): Page<VFormulaCalculationTranslated> {
        val specification = FormulaCalculationSpecification(filter)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    override fun viewToDetailDto(
        entity: VFormulaCalculationTranslated,
        translated: Boolean
    ): FormulaCalculationDto = viewToDto(entity, translated)

    fun formulaCalculationsByAnalysisCalculatedIds(
        analysisFromIds: List<Long>?
    ): List<SimpleFormulaCalculationDto> {
        return viewRepository.findAllByAnalysisComputedId(
                analysisFromIds,
                principalLangOrEn().id,
                principalUnitSetIdOrMetricId()
            )
            .map { mapper.mapSimple(it) }
    }
}
