package sk.esten.uss.gbco2.service

import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.create.CreateLandfillDto
import sk.esten.uss.gbco2.dto.request.filter.LandfillFilter
import sk.esten.uss.gbco2.dto.response.LandfillDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.LandfillMapper
import sk.esten.uss.gbco2.model.entity.landfill.Landfill
import sk.esten.uss.gbco2.model.entity.landfill.VLandfillTranslated
import sk.esten.uss.gbco2.model.repository.landfill.LandfillERepository
import sk.esten.uss.gbco2.model.repository.landfill.LandfillVRepository
import sk.esten.uss.gbco2.model.specification.LandfillSpecification

@Service
class LandfillService(
    override val entityRepository: LandfillERepository,
    override val viewRepository: LandfillVRepository,
    private val mapper: LandfillMapper,
) :
    CrudServiceView<
        Landfill,
        VLandfillTranslated,
        LandfillDto,
        LandfillDto,
        CreateLandfillDto,
        CreateLandfillDto,
        Long,
        LandfillFilter,
        ReadAllParamsFilterDto>() {

    override fun createEntity(createDto: CreateLandfillDto): Landfill = mapper.map(createDto)

    override fun updateEntity(updateDto: CreateLandfillDto, entity: Landfill) =
        mapper.update(updateDto, entity)

    override fun viewToDto(entity: VLandfillTranslated, translated: Boolean): LandfillDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun repositoryPageQuery(filter: LandfillFilter): Page<VLandfillTranslated> {
        val specification = LandfillSpecification(filter)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    override fun viewToDetailDto(entity: VLandfillTranslated, translated: Boolean): LandfillDto =
        viewToDto(entity, translated)
}
