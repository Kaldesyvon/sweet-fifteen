package sk.esten.uss.gbco2.controller

import org.springframework.web.bind.annotation.*
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.filter.AdvDetailsFilter
import sk.esten.uss.gbco2.dto.response.AdvDetailDto
import sk.esten.uss.gbco2.generics.controller.*
import sk.esten.uss.gbco2.model.entity.adv_details.AdvDetails
import sk.esten.uss.gbco2.model.entity.adv_details.VAdvDetailsTranslated
import sk.esten.uss.gbco2.service.AdvDetailsService

@RestController
@RequestMapping(path = ["/adv-details"])
class AdvDetailsController(
    override val crudService: AdvDetailsService,
) :
    PageableCrudControllerView<
        AdvDetails,
        VAdvDetailsTranslated,
        AdvDetailDto,
        AdvDetailDto,
        AdvDetailDto,
        AdvDetailDto,
        Long,
        AdvDetailsFilter,
        ReadAllParamsFilterDto>
