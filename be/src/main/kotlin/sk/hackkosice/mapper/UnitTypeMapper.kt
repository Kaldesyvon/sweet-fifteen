package sk.esten.uss.gbco2.mapper

import java.math.BigDecimal
import java.math.MathContext
import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.UnitConversionDto
import sk.esten.uss.gbco2.dto.response.UnitTypeDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleUnitTypeDto
import sk.esten.uss.gbco2.mapper.annotation.*
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreNameTranslationsAndNameKeys
import sk.esten.uss.gbco2.model.entity.unit.VGbcUnitTranslated
import sk.esten.uss.gbco2.model.entity.unit_type.UnitType
import sk.esten.uss.gbco2.model.entity.unit_type.UnitTypeSuper
import sk.esten.uss.gbco2.model.entity.unit_type.VUnitTypeTranslated

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface UnitTypeMapper : TranslationMapper {

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(
            target = "nameK",
            constant = "${UnitTypeSuper.translationPrefix}name",
            qualifiedBy = [TranslationKeyMapper::class]
        ),
        Mapping(
            target = "memoK",
            constant = "${UnitTypeSuper.translationPrefix}memo",
            qualifiedBy = [TranslationKeyMapper::class]
        ),
        Mapping(target = "id", ignore = true),
        Mapping(target = "nameTranslations", ignore = true),
        Mapping(target = "memoTranslations", ignore = true)
    )
    fun map(dto: UnitTypeDto): UnitType

    @Mappings(
        Mapping(source = "nameTranslated", target = "name"),
        Mapping(
            source = "memoTranslated",
            target = "memo",
            qualifiedBy = [NullableTranslationMapper::class]
        )
    )
    @TranslatedLanguageMapper
    fun map(entity: VUnitTypeTranslated): UnitTypeDto

    @Mappings(
        Mapping(source = "nameEn", target = "name"),
        Mapping(
            source = "memoEn",
            target = "memo",
            qualifiedBy = [NullableTranslationMapper::class]
        )
    )
    @EnglishLanguageMapper
    fun mapEn(entity: VUnitTypeTranslated): UnitTypeDto

    @IgnoreBaseAttributes
    @IgnoreNameTranslationsAndNameKeys
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(target = "memoK", ignore = true),
        Mapping(target = "memoTranslations", ignore = true)
    )
    fun update(dto: UnitTypeDto, @MappingTarget entity: UnitType)

    @Mapping(source = "nameTranslated", target = "name")
    @TranslatedLanguageMapper
    @SimpleMapper
    fun mapSimple(entity: VUnitTypeTranslated): SimpleUnitTypeDto

    @Mapping(source = "nameEn", target = "name")
    @EnglishLanguageMapper
    @SimpleMapper
    fun mapSimpleEn(entity: VUnitTypeTranslated): SimpleUnitTypeDto

    @Mappings(
        Mapping(source = "unitFrom.unitType.nameTranslated", target = "unitTypeName"),
        Mapping(source = "quantity", target = "quantityFrom"),
        Mapping(source = "unitFrom.abbr", target = "unitFromAbbr"),
        Mapping(
            target = "quantityTo",
            expression = "java(convertValueFromUnitToAnotherUnit(quantity,unitFrom, unitTo))"
        )
    )
    @Mapping(target = "unitToAbbr", source = "unitTo.abbr")
    fun mapConversion(
        quantity: BigDecimal?,
        unitFrom: VGbcUnitTranslated?,
        unitTo: VGbcUnitTranslated?
    ): UnitConversionDto

    fun convertValueFromUnitToAnotherUnit(
        quantity: BigDecimal?,
        unitFrom: VGbcUnitTranslated?,
        unitTo: VGbcUnitTranslated?
    ): BigDecimal? {
        return unitTo
            ?.k
            ?.divide(unitFrom?.k, MathContext(30))
            ?.multiply(quantity)
            ?.add(unitTo.q)
            ?.subtract(unitFrom?.q?.multiply(unitTo.k)?.divide(unitFrom.k, MathContext(30)))
            ?.round(MathContext(8))
    }
}
