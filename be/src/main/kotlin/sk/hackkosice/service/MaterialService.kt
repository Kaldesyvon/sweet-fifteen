package sk.esten.uss.gbco2.service

import org.apache.commons.lang3.NotImplementedException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.create.CreateMaterialDto
import sk.esten.uss.gbco2.dto.request.filter.MaterialFilter
import sk.esten.uss.gbco2.dto.request.filter.MaterialFilterTree
import sk.esten.uss.gbco2.dto.request.filter.MaterialGroupFilter
import sk.esten.uss.gbco2.dto.request.filter.MaterialReadAllFilter
import sk.esten.uss.gbco2.dto.request.update.UpdateMaterialDto
import sk.esten.uss.gbco2.dto.response.MaterialDto
import sk.esten.uss.gbco2.dto.response.detail.MaterialDetailDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleMaterialDto
import sk.esten.uss.gbco2.dto.response.tree.MaterialTreeDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.MaterialMapper
import sk.esten.uss.gbco2.mapper.UnitSetSettingsMapper
import sk.esten.uss.gbco2.model.entity.material.Material
import sk.esten.uss.gbco2.model.entity.material.VMaterialTranslated
import sk.esten.uss.gbco2.model.entity.material.VMaterialTranslated_
import sk.esten.uss.gbco2.model.entity.unit_type.UnitTypeSuper.Companion.MASS_TYPE_ID
import sk.esten.uss.gbco2.model.repository.material.AdvMaterialVRepository
import sk.esten.uss.gbco2.model.repository.material.MaterialERepository
import sk.esten.uss.gbco2.model.repository.material.MaterialVRepository
import sk.esten.uss.gbco2.model.specification.MaterialSpecification
import sk.esten.uss.gbco2.utils.*

@Service
class MaterialService(
    override val entityRepository: MaterialERepository,
    override val viewRepository: MaterialVRepository,
    private val advMaterialVRepository: AdvMaterialVRepository,
    private val unitSetSettingsMapper: UnitSetSettingsMapper,
    private val mapper: MaterialMapper
) :
    CrudServiceView<
        Material,
        VMaterialTranslated,
        MaterialDto,
        MaterialDetailDto,
        CreateMaterialDto,
        UpdateMaterialDto,
        Long,
        MaterialFilter,
        MaterialReadAllFilter>() {

    override fun createEntity(createDto: CreateMaterialDto): Material {
        val obj = mapper.map(createDto)
        obj.unitSetSettings.forEach { it.material = obj }
        newEnTranslation(obj.nameK, createDto.name, obj.translations)
        return obj
    }

    override fun updateInternal(id: Long, updateDto: UpdateMaterialDto): MaterialDto {
        val entity = entityRepository.findById(id).get()
        mapper.update(updateDto, entity)

        entity.unitSetSettings =
            entity
                .unitSetSettings
                .filter { m -> updateDto.unitSetSettings.any { m.id == it.id } }
                .toMutableSet()
        for (unitSetSetting in updateDto.unitSetSettings) {
            if (entity.unitSetSettings.any { it.id == unitSetSetting.id }) {
                entity.unitSetSettings.find { it.id == unitSetSetting.id }?.let {
                    unitSetSettingsMapper.update(unitSetSetting, it)
                }
            } else {
                val newEntity = unitSetSettingsMapper.map(unitSetSetting)
                newEntity.material = entity
                entity.unitSetSettings.add(newEntity)
            }
        }
        updateEnTranslation(updateDto.name, entity.translations)
        return persistInternal(entity)
    }

    override fun updateEntity(updateDto: UpdateMaterialDto, entity: Material) {
        throw NotImplementedException("method is overridden")
    }

    @Transactional(readOnly = true)
    fun getMassMaterials(parentMaterialId: Long?): List<SimpleMaterialDto> {
        return viewRepository.findAllByUnitTypeIdAndParentMaterial(
                principalLangOrEn().id,
                MASS_TYPE_ID,
                parentMaterialId
            )
            .map { mapper.mapToSimple(it) }
    }

    @Transactional(readOnly = true)
    fun getMassMaterialGroups(): List<SimpleMaterialDto> {
        return viewRepository.findAllMaterialGroupsByUnitTypeId(
                principalLangOrEn().id,
                MASS_TYPE_ID
            )
            .map { mapper.mapToSimple(it) }
    }

    override fun viewToDto(entity: VMaterialTranslated, translated: Boolean): MaterialDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    fun viewToTreeDto(entity: VMaterialTranslated, translated: Boolean): MaterialTreeDto {
        return if (translated) mapper.mapToTree(entity) else mapper.mapToTreeEn(entity)
    }

    override fun repositoryPageQuery(filter: MaterialFilter): Page<VMaterialTranslated> {
        val specification = MaterialSpecification(filter, false)
        return viewRepository.findAll(specification, filter.toPageable())
    }

    @Transactional(readOnly = true)
    fun getMaterialTree(filter: MaterialFilterTree): List<MaterialTreeDto> {
        return viewRepository
            .findAll(
                advMaterialVRepository
                    .getMaterialTree(filter)
                    .idsInSpecification(VMaterialTranslated_.idView),
                filter.toSort()
            )
            .toMaterialTree()
            .map { material: VMaterialTranslated -> viewToTreeDto(material, true) }
    }

    override fun repositoryGetAllQuery(filter: MaterialReadAllFilter?): List<VMaterialTranslated> {
        return filter?.let {
            viewRepository.findAll(
                advMaterialVRepository
                    .getAllFilteredMaterials(it)
                    .idsInSpecification(VMaterialTranslated_.idView),
                it.toSort()
            )
        }
            ?: throw IllegalStateException("MaterialReadAllFilterDto can not be null")
    }

    @Transactional(readOnly = true)
    fun getAllMaterialGroups(filter: MaterialGroupFilter): List<SimpleMaterialDto>? {
        return viewRepository.findAll(
                advMaterialVRepository
                    .getMaterialGroups(filter)
                    .idsInSpecification(VMaterialTranslated_.idView),
                filter.toSort()
            )
            .map { material: VMaterialTranslated -> mapper.mapToSimple(material) }
    }

    fun getAllMaterialByCodes(codes: List<String>?): List<MaterialDto> {
        return viewRepository.findAllByCodeInAndLanguageIdOrderById(codes, principalLangOrEn().id)
            .map { mapper.map(it) }
    }

    override fun viewToDetailDto(
        entity: VMaterialTranslated,
        translated: Boolean
    ): MaterialDetailDto =
        if (translated) mapper.mapToDetail(entity) else mapper.mapToDetailEn(entity)

    @Transactional(readOnly = true)
    fun getTranslatedName(id: Long, languageId: Long = principalLangOrEn().id ?: 1): String? {
        return viewRepository.findTranslationByIdAndLanguage(id, languageId)
    }

    @Transactional(readOnly = true)
    fun getMaterialsInMaterialNode(nodeIds: List<Long>?): List<SimpleMaterialDto> {
        return advMaterialVRepository.getMaterialsInMaterialNode(nodeIds)
    }
}
