package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.UnitParamsFilter
import sk.esten.uss.gbco2.dto.response.UnitDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleUnitDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.UnitMapper
import sk.esten.uss.gbco2.model.entity.unit.GbcUnit
import sk.esten.uss.gbco2.model.entity.unit.VGbcUnitTranslated
import sk.esten.uss.gbco2.model.repository.unit.UnitERepository
import sk.esten.uss.gbco2.model.repository.unit.UnitVRepository
import sk.esten.uss.gbco2.model.specification.UnitSpecification
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.principalLangOrEn
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
class UnitService(
    override val entityRepository: UnitERepository,
    override val viewRepository: UnitVRepository,
    private val mapper: UnitMapper
) :
    CrudServiceView<
        GbcUnit,
        VGbcUnitTranslated,
        UnitDto,
        UnitDto,
        UnitDto,
        UnitDto,
        Long,
        PageableParamsFilterDto,
        UnitParamsFilter>() {

    override fun createEntity(createDto: UnitDto): GbcUnit {
        val entity = mapper.map(createDto)
        newEnTranslation(entity.nameK, createDto.name, entity.nameTranslations)
        return entity
    }

    override fun updateEntity(updateDto: UnitDto, entity: GbcUnit) {
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.nameTranslations)
    }

    override fun viewToDto(entity: VGbcUnitTranslated, translated: Boolean): UnitDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun repositoryGetAllQuery(filter: UnitParamsFilter?): List<VGbcUnitTranslated> {
        return filter?.let { viewRepository.findAll(UnitSpecification(it), it.toSort()) }
            ?: throw IllegalStateException("AnalysisReadAllFilter can not be null")
    }

    override fun viewToDetailDto(entity: VGbcUnitTranslated, translated: Boolean): UnitDto =
        viewToDto(entity, translated)

    fun getUnitByAbbr(abbr: String?): SimpleUnitDto? =
        viewRepository.findByAbbrAndLanguageId(abbr, principalLangOrEn().id)?.let {
            mapper.mapSimple(it)
        }
}
