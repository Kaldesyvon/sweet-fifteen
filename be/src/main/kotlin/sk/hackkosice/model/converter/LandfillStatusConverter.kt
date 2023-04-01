package sk.esten.uss.gbco2.model.converter

import javax.persistence.AttributeConverter
import javax.persistence.Converter
import sk.esten.uss.gbco2.model.entity.landfill.LandfillStatus

@Converter(autoApply = true)
class LandfillStatusConverter : AttributeConverter<LandfillStatus, String> {

    override fun convertToDatabaseColumn(status: LandfillStatus): String {
        return status.dbValue
    }

    override fun convertToEntityAttribute(dbData: String): LandfillStatus {
        for (status in LandfillStatus.values()) {
            if (status.dbValue == dbData) {
                return status
            }
        }
        throw IllegalArgumentException("Unknown database value:$dbData")
    }
}
