package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateRegionDto
import sk.esten.uss.gbco2.dto.response.RegionDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleRegionDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.RegionMapper
import sk.esten.uss.gbco2.model.entity.region.Region
import sk.esten.uss.gbco2.model.entity.region.VRegionTranslated
import sk.esten.uss.gbco2.model.repository.region.RegionERepository
import sk.esten.uss.gbco2.model.repository.region.RegionVRepository
import sk.esten.uss.gbco2.utils.newEnTranslation
import sk.esten.uss.gbco2.utils.principalLangOrEn
import sk.esten.uss.gbco2.utils.updateEnTranslation

@Service
class RegionService(
    override val entityRepository: RegionERepository,
    override val viewRepository: RegionVRepository,
    private val mapper: RegionMapper,
) :
    CrudServiceView<
        Region,
        VRegionTranslated,
        RegionDto,
        RegionDto,
        CreateRegionDto,
        CreateRegionDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun viewToDto(entity: VRegionTranslated, translated: Boolean): RegionDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: CreateRegionDto): Region {
        val obj = mapper.map(createDto)
        newEnTranslation(obj.nameK, createDto.name, obj.translations)
        return obj
    }

    override fun updateEntity(updateDto: CreateRegionDto, entity: Region) {
        mapper.update(updateDto, entity)
        updateEnTranslation(updateDto.name, entity.translations)
    }

    override fun viewToDetailDto(entity: VRegionTranslated, translated: Boolean): RegionDto =
        viewToDto(entity, translated)

    fun getAllWithNode(): List<SimpleRegionDto> =
        viewRepository.findAllByNodes(principalLangOrEn().id).map { mapper.mapSimple(it) }
}
