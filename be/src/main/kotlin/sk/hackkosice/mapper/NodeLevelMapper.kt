package sk.esten.uss.gbco2.mapper

import java.util.Base64
import org.mapstruct.*
import sk.esten.uss.gbco2.dto.response.NodeLevelDto
import sk.esten.uss.gbco2.dto.response.simple.SimpleNodeLevelDto
import sk.esten.uss.gbco2.mapper.annotation.EnglishLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.SimpleMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslatedLanguageMapper
import sk.esten.uss.gbco2.mapper.annotation.TranslationKeyMapper
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreBaseAttributes
import sk.esten.uss.gbco2.mapper.annotation.ignore_mapping.generic.IgnoreTranslationsAndNameKeys
import sk.esten.uss.gbco2.model.entity.node_level.NodeLevel
import sk.esten.uss.gbco2.model.entity.node_level.NodeLevelSuper
import sk.esten.uss.gbco2.model.entity.node_level.VNodeLevelTranslated
import sk.esten.uss.gbco2.utils.detectContentType

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    typeConversionPolicy = ReportingPolicy.ERROR
)
interface NodeLevelMapper : TranslationMapper {

    @IgnoreBaseAttributes
    @Mappings(
        Mapping(
            target = "nameK",
            constant = "${NodeLevelSuper.translationPrefix}name",
            qualifiedBy = [TranslationKeyMapper::class]
        ),
        Mapping(target = "translations", ignore = true)
    )
    @Mapping(target = "id", ignore = true)
    fun map(dto: NodeLevelDto): NodeLevel

    @IgnoreBaseAttributes
    @IgnoreTranslationsAndNameKeys
    @Mapping(target = "id", ignore = true)
    fun update(dto: NodeLevelDto, @MappingTarget entity: NodeLevel)

    @Mapping(source = "nameTranslated", target = "name")
    @TranslatedLanguageMapper
    fun map(entity: VNodeLevelTranslated): NodeLevelDto

    @Mapping(source = "nameEn", target = "name")
    @EnglishLanguageMapper
    fun mapEn(entity: VNodeLevelTranslated): NodeLevelDto

    @Mapping(source = "nameTranslated", target = "name")
    @TranslatedLanguageMapper
    @SimpleMapper
    fun mapToSimple(entity: VNodeLevelTranslated): SimpleNodeLevelDto

    @Mapping(source = "nameEn", target = "name")
    @EnglishLanguageMapper
    @SimpleMapper
    fun mapToSimpleEn(entity: VNodeLevelTranslated): SimpleNodeLevelDto

    /** map byte array to base64 string with mime type in data property */
    fun mapIconToBase64(bytes: ByteArray?): String? {
        return bytes?.let {
            "data:${it.detectContentType()};base64,${Base64.getEncoder().encodeToString(it)}"
        }
    }

    fun mapToByteArray(base64Icon: String?): ByteArray? {
        return base64Icon?.let { Base64.getDecoder().decode(it.substringAfterLast(",")) }
    }
}
