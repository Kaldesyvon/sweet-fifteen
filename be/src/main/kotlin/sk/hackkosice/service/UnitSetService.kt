package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateUnitSetDto
import sk.esten.uss.gbco2.dto.response.UnitSetDto
import sk.esten.uss.gbco2.exceptions.ValidationException
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.UnitSetMapper
import sk.esten.uss.gbco2.model.entity.unit_set.UnitSet
import sk.esten.uss.gbco2.model.entity.unit_set.VUnitSetTranslated
import sk.esten.uss.gbco2.model.entity.unit_set_settings.UnitSetSettings
import sk.esten.uss.gbco2.model.entity.unit_set_settings_ap.UnitSetSettingsAp
import sk.esten.uss.gbco2.model.repository.unit_set.UnitSetERepository
import sk.esten.uss.gbco2.model.repository.unit_set.UnitSetVRepository
import sk.esten.uss.gbco2.model.repository.unit_set_settings.UnitSetSettingsERepository
import sk.esten.uss.gbco2.model.repository.unit_set_settings_ap.UnitSetSettingsApERepository
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
class UnitSetService(
    override val entityRepository: UnitSetERepository,
    override val viewRepository: UnitSetVRepository,
    private val unitSetSettingsERepository: UnitSetSettingsERepository,
    private val unitSetSettingsAPERepository: UnitSetSettingsApERepository,
    private val mapper: UnitSetMapper,
) :
    CrudServiceView<
        UnitSet,
        VUnitSetTranslated,
        UnitSetDto,
        UnitSetDto,
        CreateUnitSetDto,
        UnitSetDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    @Transactional
    override fun createEntity(createDto: CreateUnitSetDto): UnitSet {
        val unitSet = entityRepository.saveAndFlush(mapper.map(createDto))

        val originUnitSetSettings: List<UnitSetSettings> =
            createDto.unitSetId?.let { unitSetSettingsERepository.findAllByUnitSetId(it) }
                ?: throw ValidationException(description = "Id must not be null")

        val originUnitSetSettingsAP: List<UnitSetSettingsAp> =
            createDto.unitSetId?.let { unitSetSettingsAPERepository.findAllByUnitSetId(it) }
                ?: throw ValidationException(description = "Id must not be null")

        val unitSetSettings: List<UnitSetSettings> =
            originUnitSetSettings.map {
                UnitSetSettings().apply {
                    this.material = it.material
                    this.unitSet = unitSet
                    this.unit = it.unit
                }
            }
        val unitSetSettingsAp: List<UnitSetSettingsAp> =
            originUnitSetSettingsAP.map {
                UnitSetSettingsAp().apply {
                    this.unitSet = unitSet
                    this.analysisParam = it.analysisParam
                    this.unit = it.unit
                }
            }
        unitSetSettingsERepository.saveAll(unitSetSettings)
        unitSetSettingsAPERepository.saveAll(unitSetSettingsAp)
        newEnTranslation(unitSet.nameK, createDto.name, unitSet.nameTranslations)
        newEnTranslation(unitSet.memoK, createDto.memo, unitSet.memoTranslations)
        return unitSet
    }

    override fun updateEntity(updateDto: UnitSetDto, entity: UnitSet) {
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.nameTranslations)
        updateEnTranslation(updateDto.memo, entity.memoTranslations)
    }

    override fun viewToDto(entity: VUnitSetTranslated, translated: Boolean): UnitSetDto =
        if (translated) mapper.map(entity) else mapper.mapEn(entity)

    override fun viewToDetailDto(entity: VUnitSetTranslated, translated: Boolean): UnitSetDto =
        viewToDto(entity, translated)

    fun getAllUnitSets(): MutableList<UnitSet> = entityRepository.findAll()
}
