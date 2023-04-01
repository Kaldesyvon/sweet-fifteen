package sk.esten.uss.gbco2.controller

import org.springframework.web.bind.annotation.*
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.response.AdvStatusDto
import sk.esten.uss.gbco2.generics.controller.*
import sk.esten.uss.gbco2.mapper.AdvStatusMapper
import sk.esten.uss.gbco2.model.entity.adv_status.AdvStatus
import sk.esten.uss.gbco2.model.entity.adv_status.VAdvStatusTranslated
import sk.esten.uss.gbco2.service.AdvStatusService

@RestController
@RequestMapping(path = ["/adv-status"])
class AdvStatusController(
    override val crudService: AdvStatusService,
    private val mapper: AdvStatusMapper
) :
    ReadAllCrudControllerView<
        AdvStatus,
        VAdvStatusTranslated,
        AdvStatusDto,
        AdvStatusDto,
        AdvStatusDto,
        AdvStatusDto,
        AdvStatusDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto> {
    override fun entityToSimpleDto(
        entity: VAdvStatusTranslated,
        translated: Boolean
    ): AdvStatusDto {
        return if (translated) mapper.map(entity) else mapper.mapEn(entity)
    }
}
