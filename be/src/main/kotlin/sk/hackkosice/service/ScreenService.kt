package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.ScreenDto
import sk.esten.uss.gbco2.exceptions.ForbiddenException
import sk.esten.uss.gbco2.exceptions.NotFoundException
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.ScreenMapper
import sk.esten.uss.gbco2.model.entity.screen.Screen
import sk.esten.uss.gbco2.model.entity.screen.VScreenTranslated
import sk.esten.uss.gbco2.model.repository.screen.ScreenERepository
import sk.esten.uss.gbco2.model.repository.screen.ScreenVRepository
import sk.esten.uss.gbco2.utils.principalLangOrEn

@Service
class ScreenService(
    override val entityRepository: ScreenERepository,
    override val viewRepository: ScreenVRepository,
    private val mapper: ScreenMapper,
) :
    CrudServiceView<
        Screen,
        VScreenTranslated,
        ScreenDto,
        ScreenDto,
        ScreenDto,
        ScreenDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun viewToDto(entity: VScreenTranslated, translated: Boolean): ScreenDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }

    override fun createEntity(createDto: ScreenDto): Screen {
        throw ForbiddenException("Create method for entity Screen is not allowed")
    }

    override fun updateEntity(updateDto: ScreenDto, entity: Screen) {
        throw ForbiddenException("Update method for entity Screen is not allowed")
    }

    @Transactional(readOnly = true)
    fun getByScreenCode(code: String): ScreenDto? {
        return mapper.map(
            viewRepository.findByScreenCode(code, principalLangOrEn().id).orElse(null)
                ?: throw NotFoundException("Screen with code: $code is not found")
        )
    }

    override fun viewToDetailDto(entity: VScreenTranslated, translated: Boolean): ScreenDto =
        viewToDto(entity, translated)
}
