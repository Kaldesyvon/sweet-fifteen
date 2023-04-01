package sk.esten.uss.gbco2.mapper

import org.mapstruct.*
import sk.esten.uss.gbco2.dto.request.create.CreateQuantityDto
import sk.esten.uss.gbco2.dto.request.update.UpdateQuantityDto
import sk.esten.uss.gbco2.dto.response.detail.QuantityDetailDto
import sk.esten.uss.gbco2.dto.response.quantity.QuantityDto
import sk.esten.uss.gbco2.mapper.annotation.CommonMapper
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.IgnoreQuantityAttributes
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.model.entity.quantity.Quantity
import sk.esten.uss.gbco2.model.entity.quantity.VQuantityTranslated
import sk.esten.uss.gbco2.service.MaterialConversionService
import sk.esten.uss.gbco2.service.MeterService
import sk.esten.uss.gbco2.service.UnitService

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR,
    uses =
        [
            MeterMapper::class,
            NodeMapper::class,
            MaterialMapper::class,
            MaterialNodeMapper::class,
            AnalysisParamMapper::class,
            UnitMapper::class,
            MeterService::class,
            MaterialConversionService::class,
            UnitService::class]
)
interface QuantityMapper {

    @Mappings(
        Mapping(source = "meter", target = "meter", qualifiedBy = [SimpleMapper::class]),
        Mapping(
            source = "node",
            target = "node",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "material",
            target = "material",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "materialNode",
            target = "materialNode",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "analysisParam",
            target = "analysisParam",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
    )
    @CommonMapper
    @TranslatedLanguageMapper
    fun map(entity: VQuantityTranslated): QuantityDto

    @Mappings(
        Mapping(source = "meter", target = "meter", qualifiedBy = [SimpleMapper::class]),
        Mapping(
            source = "node",
            target = "node",
            qualifiedBy = [SimpleMapper::class, EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "material",
            target = "material",
            qualifiedBy = [SimpleMapper::class, EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "materialNode",
            target = "materialNode",
            qualifiedBy = [SimpleMapper::class]
        ),
        Mapping(
            source = "analysisParam",
            target = "analysisParam",
            qualifiedBy = [SimpleMapper::class, EnglishLanguageMapper::class]
        ),
    )
    @CommonMapper
    @EnglishLanguageMapper
    fun mapEn(entity: VQuantityTranslated): QuantityDto

    @IterableMapping(qualifiedBy = [TranslatedLanguageMapper::class, CommonMapper::class])
    @TranslatedLanguageMapper
    fun mapQuantitiesToQuantitiesDto(entity: List<VQuantityTranslated?>): MutableSet<QuantityDto>

    @IterableMapping(qualifiedBy = [EnglishLanguageMapper::class, CommonMapper::class])
    @EnglishLanguageMapper
    fun mapQuantitiesToQuantitiesDtoEn(entity: List<VQuantityTranslated?>): MutableSet<QuantityDto>

    @Mappings(
        Mapping(
            source = "node",
            target = "node",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
        Mapping(
            source = "material",
            target = "material",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
        Mapping(source = "meter", target = "meter", qualifiedBy = [SimpleMapper::class]),
        Mapping(
            source = "unit",
            target = "unit",
            qualifiedBy = [SimpleMapper::class, TranslatedLanguageMapper::class]
        ),
    )
    @IgnoreBaseAttributes
    @IgnoreQuantityAttributes
    @TranslatedLanguageMapper
    fun mapDetail(entity: VQuantityTranslated): QuantityDetailDto

    @Mappings(
        Mapping(
            source = "node",
            target = "node",
            qualifiedBy = [SimpleMapper::class, EnglishLanguageMapper::class]
        ),
        Mapping(
            source = "material",
            target = "material",
            qualifiedBy = [SimpleMapper::class, EnglishLanguageMapper::class]
        ),
        Mapping(source = "meter", target = "meter", qualifiedBy = [SimpleMapper::class]),
        Mapping(
            source = "unit",
            target = "unit",
            qualifiedBy = [SimpleMapper::class, EnglishLanguageMapper::class]
        ),
    )
    @IgnoreBaseAttributes
    @IgnoreQuantityAttributes
    @EnglishLanguageMapper
    fun mapDetailEn(entity: VQuantityTranslated): QuantityDetailDto

    @Mappings(
        Mapping(source = "meterId", target = "meter"),
        Mapping(source = "remoteMaterialCodeId", target = "remoteMaterialCode"),
        Mapping(source = "unitId", target = "unit"),
        Mapping(target = "id", ignore = true),
        Mapping(target = "remoteCode", ignore = true),
        Mapping(target = "editable", ignore = true),
        Mapping(target = "objVersion", ignore = true),
        Mapping(target = "materialNode", ignore = true),
    )
    @IgnoreBaseAttributes
    fun map(createDto: CreateQuantityDto): Quantity

    @Mappings(
        Mapping(source = "meterId", target = "meter"),
        Mapping(source = "unitId", target = "unit"),
        Mapping(target = "remoteMaterialCode", ignore = true),
        Mapping(target = "id", ignore = true),
        Mapping(target = "remoteCode", ignore = true),
        Mapping(target = "editable", ignore = true),
        Mapping(target = "materialNode", ignore = true),
    )
    @IgnoreBaseAttributes
    fun update(updateDto: UpdateQuantityDto, @MappingTarget entity: Quantity)
}
