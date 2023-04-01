package sk.esten.uss.gbco2.service

import java.time.LocalDate
import org.apache.commons.lang3.NotImplementedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.PlantResultDto
import sk.esten.uss.gbco2.dto.response.ResultsMonthDto
import sk.esten.uss.gbco2.generics.service.CrudService
import sk.esten.uss.gbco2.model.entity.results_month.VResultsMonth
import sk.esten.uss.gbco2.model.repository.results_month.ResultsMonthERepository

@Service
class ResultsMonthService(
    override val entityRepository: ResultsMonthERepository,
    private val analysisParamService: AnalysisParamService,
    private val nodeService: NodeService
) :
    CrudService<
        VResultsMonth,
        ResultsMonthDto,
        ResultsMonthDto,
        ResultsMonthDto,
        ResultsMonthDto,
        String,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun entityToDetailDto(entity: VResultsMonth, translated: Boolean): ResultsMonthDto {
        throw NotImplementedException("This is only helper view")
    }

    override fun entityToDto(entity: VResultsMonth, translated: Boolean): ResultsMonthDto {
        throw NotImplementedException("This is only helper view")
    }

    override fun createEntity(createDto: ResultsMonthDto): VResultsMonth {
        throw NotImplementedException("This type of entity cannot be created")
    }

    override fun updateEntity(updateDto: ResultsMonthDto, entity: VResultsMonth) {
        throw NotImplementedException("This type of entity cannot be updated")
    }

    @Transactional(readOnly = true)
    fun findPlantResultsByCriteria(
        plantId: Long?,
        idScope: Long?,
        idUnitSet: Long?,
        displayToDate: LocalDate,
        analysisParamId: Long?,
        useSubNodes: Boolean
    ): List<PlantResultDto> {

        val analysisParam = analysisParamService.get(analysisParamId)
        val analysisParamParentId = analysisParam?.parent?.getPk()
        val analysisParamParentKoef = analysisParam?.parentKoef

        val plantResults = mutableListOf<PlantResultDto>()

        plantResults.addAll(
            entityRepository.findPlantResultsByCriteria(
                listOfNotNull(plantId),
                idScope,
                idUnitSet,
                displayToDate.withDayOfYear(1),
                displayToDate,
                analysisParamParentId ?: analysisParamId,
            )
        )

        if (useSubNodes) {
            val subNodesIdsChunked =
                nodeService
                    .getAllValidSubNodeIds(plantId, displayToDate.withDayOfYear(1), displayToDate)
                    .minus(plantId)
                    .filterNotNull()
                    .chunked(50)

            subNodesIdsChunked.forEach { subNodeIds ->
                plantResults.addAll(
                    entityRepository.findPlantResultsByCriteria(
                        subNodeIds,
                        idScope,
                        idUnitSet,
                        displayToDate.withDayOfYear(1),
                        displayToDate,
                        analysisParamParentId ?: analysisParamId,
                    )
                )
            }
        }

        if (analysisParamParentId != null && analysisParamParentKoef != null)
            plantResults.forEach {
                it.monthValue = it.monthValue?.multiply(analysisParamParentKoef.toBigDecimal())
            }

        return plantResults
    }
}
