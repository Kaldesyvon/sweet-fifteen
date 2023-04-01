package sk.esten.uss.gbco2.service

import java.time.LocalDate
import java.time.LocalDateTime
import org.apache.commons.lang3.NotImplementedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.request.PageableParamsFilterDto
import sk.esten.uss.gbco2.dto.request.ReadAllParamsFilterDto
import sk.esten.uss.gbco2.dto.request.update.node_datalock.NodeDataLockUpdateType
import sk.esten.uss.gbco2.dto.request.update.node_datalock.UpdateNodeDatalockDto
import sk.esten.uss.gbco2.dto.response.NodeDatalockDto
import sk.esten.uss.gbco2.generics.service.CrudServiceView
import sk.esten.uss.gbco2.mapper.NodeDatalockMapper
import sk.esten.uss.gbco2.model.entity.node_datalock.NodeDatalock
import sk.esten.uss.gbco2.model.entity.node_datalock.VNodeDatalockTranslated
import sk.esten.uss.gbco2.model.entity.param.Param
import sk.esten.uss.gbco2.model.repository.node_datalock.NodeDatalockERepository
import sk.esten.uss.gbco2.model.repository.node_datalock.NodeDatalockVRepository
import sk.esten.uss.gbco2.utils.principal

@Service
class NodeDataLockService(
    override val entityRepository: NodeDatalockERepository,
    override val viewRepository: NodeDatalockVRepository,
    private val paramService: ParamService,
    private val mapper: NodeDatalockMapper,
) :
    CrudServiceView<
        NodeDatalock,
        VNodeDatalockTranslated,
        NodeDatalockDto,
        NodeDatalockDto,
        NodeDatalockDto,
        NodeDatalockDto,
        Long,
        PageableParamsFilterDto,
        ReadAllParamsFilterDto>() {

    override fun viewToDto(entity: VNodeDatalockTranslated, translated: Boolean): NodeDatalockDto =
        mapper.map(entity)

    override fun viewToDetailDto(
        entity: VNodeDatalockTranslated,
        translated: Boolean,
    ): NodeDatalockDto = viewToDto(entity, translated)

    override fun createEntity(createDto: NodeDatalockDto): Nothing =
        throw NotImplementedException("functionality is overridden")

    override fun updateEntity(updateDto: NodeDatalockDto, entity: NodeDatalock): Nothing =
        throw NotImplementedException("functionality is overridden")

    fun getCorporationDataLock(): NodeDatalockDto {
        val corpLock = paramService.getParamByCode(Param.LOCK_DATE_USS)
        val corpCheck = paramService.getParamByCode(Param.CLOSE_DATE_USS)
        val corpAudit = paramService.getParamByCode(Param.AUDIT_DATE_USS)

        return NodeDatalockDto().apply {
            id = null

            checkDate = corpCheck.dateValue?.atStartOfDay()
            checkedBy = corpCheck.modifiedBy
            checkedDate = corpCheck.modified

            auditDate = corpAudit.dateValue?.atStartOfDay()
            auditedBy = corpAudit.modifiedBy
            auditedDate = corpAudit.modified

            lockDate = corpLock.dateValue?.atStartOfDay()
            lockedBy = corpLock.modifiedBy
            lockedDate = corpLock.modified
        }
    }

    fun updateNodeDataLockDate(
        id: Long,
        updateType: NodeDataLockUpdateType,
        updateDto: UpdateNodeDatalockDto,
    ): NodeDatalockDto {
        val dataLock = getInternal(id)
        val (year, month, objVersion) = updateDto

        val updatedDate = LocalDate.of(year, month, 1).atStartOfDay()
        val username = principal()?.username
        val now = LocalDateTime.now()

        when (updateType) {
            NodeDataLockUpdateType.AUDIT -> {
                dataLock.auditDate = updatedDate
                dataLock.auditedBy = username
                dataLock.auditedDate = now
                if (dataLock.lockDate?.isBefore(updatedDate) == true) {
                    dataLock.lockDate = updatedDate
                }
                if (dataLock.checkDate?.isBefore(updatedDate) == true) {
                    dataLock.checkDate = updatedDate
                }
            }
            NodeDataLockUpdateType.CHECK -> {
                dataLock.checkDate = updatedDate
                dataLock.checkedBy = username
                dataLock.checkedDate = now
                if (dataLock.lockDate?.isBefore(updatedDate) == true) {
                    dataLock.lockDate = updatedDate
                }
            }
            NodeDataLockUpdateType.LOCK -> {
                dataLock.lockDate = updatedDate
                dataLock.lockedBy = username
                dataLock.lockedDate = now
            }
        }

        dataLock.objVersion = objVersion
        entityRepository.save(dataLock)
        return viewToDto(getView(id))
    }

    @Transactional
    fun updateCorporationDataLockDate(
        updateType: NodeDataLockUpdateType,
        updateDto: UpdateNodeDatalockDto,
    ): NodeDatalockDto {
        val (year, month) = updateDto
        val updatedDate = LocalDate.of(year, month, 1)

        var shouldCheck = false

        if (updateType == NodeDataLockUpdateType.AUDIT) {
            val param = paramService.getParamByCode(Param.AUDIT_DATE_USS)
            param.dateValue = updatedDate
            shouldCheck = true
        }

        if (updateType == NodeDataLockUpdateType.CHECK || shouldCheck) {
            val param = paramService.getParamByCode(Param.CLOSE_DATE_USS)
            if (!shouldCheck || (param.dateValue?.isBefore(updatedDate) == true)) {
                param.dateValue = updatedDate
                paramService.persistInternal(param)
            }
            shouldCheck = true
        }

        if (updateType == NodeDataLockUpdateType.LOCK || shouldCheck) {
            val param = paramService.getParamByCode(Param.LOCK_DATE_USS)
            if (!shouldCheck || (param.dateValue?.isBefore(updatedDate) == true)) {
                param.dateValue = updatedDate
                paramService.persistInternal(param)
            }
        }

        if (updateType != NodeDataLockUpdateType.AUDIT) {
            entityRepository.updateLockDate(updatedDate.atStartOfDay())
            entityRepository.updateCheckDate(updatedDate.atStartOfDay())
        }

        return getCorporationDataLock()
    }

    fun getDataLockByNode(nodeId: Long): NodeDatalock? {
        return entityRepository.getFirstByNodeId(nodeId).orElse(null)
    }
}
