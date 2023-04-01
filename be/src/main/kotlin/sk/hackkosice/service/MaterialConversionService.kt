package sk.esten.uss.gbco2.service

import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import sk.esten.uss.gbco2.dto.request.create.CreateMaterialConversionDto
import sk.esten.uss.gbco2.dto.request.filter.MaterialConversionFilter
import sk.esten.uss.gbco2.dto.request.filter.MaterialConversionReadAllFilter
import sk.esten.uss.gbco2.dto.response.MaterialConversionDto
import sk.esten.uss.gbco2.dto.response.detail.MaterialConversionDetailDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.MaterialConversionMapper
import sk.esten.uss.gbco2.model.entity.material_conversion.MaterialConversion
import sk.esten.uss.gbco2.model.entity.material_conversion.VMaterialConversionTranslated
import sk.esten.uss.gbco2.model.repository.material_conversion.MaterialConversionERepository
import sk.esten.uss.gbco2.model.repository.material_conversion.MaterialConversionVRepository
import sk.esten.uss.gbco2.model.specification.MaterialConversionReadAllSpecification
import sk.esten.uss.gbco2.model.specification.MaterialConversionSpecification

@Service
class MaterialConversionService(
    override val entityRepository: MaterialConversionERepository,
    override val viewRepository: MaterialConversionVRepository,
    private val mapper: MaterialConversionMapper,
) :
    CrudServiceView<
        MaterialConversion,
        VMaterialConversionTranslated,
        MaterialConversionDto,
        MaterialConversionDetailDto,
        CreateMaterialConversionDto,
        CreateMaterialConversionDto,
        Long,
        MaterialConversionFilter,
        MaterialConversionReadAllFilter>() {

    override fun repositoryGetAllQuery(
        filter: MaterialConversionReadAllFilter?
    ): List<VMaterialConversionTranslated> {
        return (filter?.let {
            viewRepository.findAll(MaterialConversionReadAllSpecification(it), it.toSort())
        }
            ?: throw IllegalStateException("MaterialConversionReadAllFilter can not be null"))
    }

    override fun repositoryPageQuery(
        filter: MaterialConversionFilter
    ): Page<VMaterialConversionTranslated> {
        val specification = MaterialConversionSpecification(filter)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    override fun updateEntity(updateDto: CreateMaterialConversionDto, entity: MaterialConversion) {
        mapper.update(updateDto, entity)
    }

    override fun viewToDto(
        entity: VMaterialConversionTranslated,
        translated: Boolean
    ): MaterialConversionDto = if (translated) mapper.map(entity) else mapper.mapEn(entity)

    override fun viewToDetailDto(
        entity: VMaterialConversionTranslated,
        translated: Boolean
    ): MaterialConversionDetailDto = if (translated) mapper.map(entity) else mapper.mapEn(entity)

    override fun createEntity(createDto: CreateMaterialConversionDto): MaterialConversion {
        return mapper.map(createDto)
    }
}
