package sk.esten.uss.gbco2.service

import com.fathzer.soft.javaluator.DoubleEvaluator
import com.fathzer.soft.javaluator.StaticVariableSet
import org.apache.commons.lang3.NotImplementedException
import org.springframework.data.domain.Page
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.create.CreateAnalysisParamDto
import sk.esten.uss.gbco2.dto.request.create.CreateAnalysisParamExprDto
import sk.esten.uss.gbco2.dto.request.create.CreateUnitSetSettingsApDto
import sk.esten.uss.gbco2.dto.request.filter.AnalysisParamFilter
import sk.esten.uss.gbco2.dto.request.filter.AnalysisParamReadAllFilter
import sk.esten.uss.gbco2.dto.request.update.UpdateAnalysisParamDto
import sk.esten.uss.gbco2.dto.response.AnalysisParamDto
import sk.esten.uss.gbco2.dto.response.detail.AnalysisParamDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleAnalysisParamDto
import sk.esten.uss.gbco2.exceptions.NotFoundException
import sk.esten.uss.gbco2.exceptions.ValidationException
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.AnalysisParamMapper
import sk.esten.uss.gbco2.model.entity.analysis_param.AnalysisParam
import sk.esten.uss.gbco2.model.entity.analysis_param.VAnalysisParamTranslated
import sk.esten.uss.gbco2.model.entity.analysis_param.VAnalysisParamTranslated_
import sk.esten.uss.gbco2.model.entity.analysis_param_expr.AnalysisParamExpr
import sk.esten.uss.gbco2.model.entity.unit_set.UnitSet
import sk.esten.uss.gbco2.model.entity.unit_set_settings_ap.UnitSetSettingsAp
import sk.esten.uss.gbco2.model.repository.analysis_param.AdvAnalysisParamVRepository
import sk.esten.uss.gbco2.model.repository.analysis_param.AnalysisParamERepository
import sk.esten.uss.gbco2.model.repository.analysis_param.AnalysisParamVRepository
import sk.esten.uss.gbco2.model.repository.analysis_param_expr.AnalysisParamExprERepository
import sk.esten.uss.gbco2.model.repository.material_node_analysis_param.MaterialNodeAnalysisParamERepository
import sk.esten.uss.gbco2.model.repository.unit_set_settings_ap.UnitSetSettingsApERepository
import sk.esten.uss.gbco2.model.specification.AnalysisParamSpecification
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.principalLangOrEn
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
class AnalysisParamService(
    override val entityRepository: AnalysisParamERepository,
    override val viewRepository: AnalysisParamVRepository,
    val advAnalysisParamVRepository: AdvAnalysisParamVRepository,
    val materialNodeAnalysisParamRepository: MaterialNodeAnalysisParamERepository,
    val analysisParamExprERepository: AnalysisParamExprERepository,
    val unitSetSettingsApERepository: UnitSetSettingsApERepository,
    val mapper: AnalysisParamMapper,
    val unitSetService: UnitSetService,
    val unitService: UnitService
) :
    CrudServiceView<
        AnalysisParam,
        VAnalysisParamTranslated,
        AnalysisParamDto,
        AnalysisParamDetailDto,
        CreateAnalysisParamDto,
        UpdateAnalysisParamDto,
        Long,
        AnalysisParamFilter,
        AnalysisParamReadAllFilter>() {

    override fun viewToDto(
        entity: VAnalysisParamTranslated,
        translated: Boolean
    ): AnalysisParamDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun repositoryGetAllQuery(
        filter: AnalysisParamReadAllFilter?
    ): List<VAnalysisParamTranslated> {

        return filter?.let {
            val spec =
                Specification<VAnalysisParamTranslated> { root, _, builder ->
                    builder.equal(
                        root.get(VAnalysisParamTranslated_.languageId),
                        principalLangOrEn().id
                    )
                }
                    .and { root, _, builder ->
                        builder.or(
                            *advAnalysisParamVRepository
                                .getAnalysisParam(filter)
                                .chunked(999)
                                .map { ids -> root.get(VAnalysisParamTranslated_.id).`in`(ids) }
                                .toList()
                                .toTypedArray()
                        )
                    }

            viewRepository.findAll(spec, it.toSort())
        }
            ?: throw IllegalStateException("AnalysisParamFilterDto can not be null")
    }

    override fun repositoryPageQuery(filter: AnalysisParamFilter): Page<VAnalysisParamTranslated> {
        val specification = AnalysisParamSpecification(filter)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    override fun viewToDetailDto(
        entity: VAnalysisParamTranslated,
        translated: Boolean
    ): AnalysisParamDetailDto =
        if (translated) mapper.mapToDetail(entity) else mapper.mapToDetailEn(entity)

    @Transactional(readOnly = true)
    override fun createEntity(createDto: CreateAnalysisParamDto): AnalysisParam {
        val unitSets = validateUnitSetSettingsApsSizeAndReturnAllUnitSets(createDto)
        val entity = mapper.map(createDto)
        newEnTranslation(entity.nameK, createDto.name, entity.nameTranslations)

        createDto.analysisParamExpressions?.forEach { analysisParamExpr ->
            addNewAnalysisParamExpression(entity, analysisParamExpr)
        }

        createDto.unitSetSettingsAps?.forEach { unitSetSettingsApDto ->
            val unitSetSettingsAp = UnitSetSettingsAp()
            unitSetSettingsAp.analysisParam = entity
            unitSetSettingsAp.unitSet =
                unitSets.find { it.id == unitSetSettingsApDto.unitSetId }
                    ?: throw NotFoundException()
            validateAndSetUnitToUnitSetSettingsAp(
                unitSetSettingsApDto,
                entity.unitType?.id,
                unitSetSettingsAp
            )
            entity.unitSetSettingsAps.add(unitSetSettingsAp)
        }

        return entity
    }

    @Transactional
    override fun updateInternal(id: Long, updateDto: UpdateAnalysisParamDto): AnalysisParamDto {
        val entity = entityRepository.findById(id).get()
        validateUnitSetSettingsApsSizeAndReturnAllUnitSets(updateDto)
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.nameTranslations)

        entity.analysisParamExpressions.removeIf { analysisParamExpr ->
            updateDto.analysisParamExpressions?.map { it.id }?.contains(analysisParamExpr.id) ==
                false
        }
        updateDto.analysisParamExpressions?.forEach { analysisParamExpr ->
            if (analysisParamExpr.id != null) {
                validateAnalysisParamExpression(analysisParamExpr.expr)
                entity.analysisParamExpressions.find { it.id == analysisParamExpr.id }?.expr =
                    analysisParamExpr.expr
            } else {
                addNewAnalysisParamExpression(entity, analysisParamExpr)
            }
        }

        updateDto.unitSetSettingsAps?.forEach { unitSetSettingsApDto ->
            val unitSetSettingsAp =
                entity.unitSetSettingsAps.find { it.unitSet?.id == unitSetSettingsApDto.unitSetId }
                    ?: throw NotFoundException()
            if (unitSetSettingsApDto.unitId != unitSetSettingsAp.unit?.id) {
                validateAndSetUnitToUnitSetSettingsAp(
                    unitSetSettingsApDto,
                    entity.unitType?.id,
                    unitSetSettingsAp
                )
            }
        }

        return persistInternal(entity)
    }

    @Transactional
    override fun delete(id: Long) {
        materialNodeAnalysisParamRepository.deleteAllByAnalysisParamId(id)
        analysisParamExprERepository.deleteAllByAnalysisParamId(id)
        unitSetSettingsApERepository.deleteAllByAnalysisParamId(id)
        return super.delete(id)
    }

    override fun updateEntity(updateDto: UpdateAnalysisParamDto, entity: AnalysisParam) {
        throw NotImplementedException("method is overridden")
    }

    fun validateAnalysisParamExpression(inputExpression: String?) {
        val allAnalysisParamCodes = entityRepository.findAllAnalysisParamCodes()
        val analysisParamsAsVariables = StaticVariableSet<Double>()

        val expression = inputExpression?.replace(",", ".")?.filter { !it.isWhitespace() }
        Regex("[a-zA-Z_]+[0-9a-zA-Z_]*")
            .findAll((expression.toString())) // find all analysis param codes in expression
            .map { it.groupValues[0] }
            .toList()
            .forEach { analysisParamCode ->
                run { // validate if expression contains only analysis params in DB
                    if (!allAnalysisParamCodes.contains(analysisParamCode)) {
                        throw ValidationException(
                            description = "Analysis param inside expression does not exist"
                        )
                    } // substitute every existing analysis param code for number 1 as variable
                    analysisParamsAsVariables.set(analysisParamCode, 1.toDouble())
                }
            }

        try { // validate evaluation of expression - must be DOUBLE, for example: 1.5+3
            DoubleEvaluator().evaluate(expression, analysisParamsAsVariables)
        } catch (e: Exception) {
            throw ValidationException(description = "Analysis param expression is not valid")
        }
    }

    fun getAllSimpleByIds(ids: List<Long>): List<SimpleAnalysisParamDto> =
        viewRepository.findAllByIdInAndLanguageId(ids, principalLangOrEn().id).map {
            mapper.mapSimple(it)
        }

    private fun validateAndSetUnitToUnitSetSettingsAp(
        createUnitSetSettingsApDto: CreateUnitSetSettingsApDto,
        entityUnitTypeId: Long?,
        unitSetSettingsAp: UnitSetSettingsAp
    ) {
        val unit = unitService.get(createUnitSetSettingsApDto.unitId)
        if (unit?.unitType?.id != entityUnitTypeId) {
            throw ValidationException(
                description =
                    "Unit of unitSetSettingsAp object must be within the same unit type as analysis param entity"
            )
        }
        unitSetSettingsAp.unit = unit
    }

    private fun addNewAnalysisParamExpression(
        entity: AnalysisParam,
        createAnalysisParamExprDto: CreateAnalysisParamExprDto,
    ) {
        validateAnalysisParamExpression(createAnalysisParamExprDto.expr)
        val analysisParamExpr = AnalysisParamExpr()
        analysisParamExpr.analysisParam = entity
        analysisParamExpr.expr = createAnalysisParamExprDto.expr
        entity.analysisParamExpressions.add(analysisParamExpr)
    }

    private fun validateUnitSetSettingsApsSizeAndReturnAllUnitSets(
        updateDto: UpdateAnalysisParamDto
    ): List<UnitSet> {
        val unitSets = unitSetService.getAllUnitSets()
        val unitSetsSize = unitSets.size
        if (unitSetsSize != updateDto.unitSetSettingsAps?.size) {
            throw ValidationException(
                description =
                    "There must be unitSetSettingsAp object for each unit set record - total count: $unitSetsSize"
            )
        }
        return unitSets
    }

    fun getAllFromMaterialNodeAP(filter: AnalysisParamReadAllFilter): List<SimpleAnalysisParamDto> {
        return advAnalysisParamVRepository.getAllFromMaterialNodeAP(filter)
    }
}
