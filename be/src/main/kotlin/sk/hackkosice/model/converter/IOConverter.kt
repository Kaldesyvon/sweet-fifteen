package sk.esten.uss.gbco2.model.converter

import javax.persistence.AttributeConverter
import javax.persistence.Converter
import sk.esten.uss.gbco2.model.entity.quantity.IOEnum

@Converter(autoApply = true)
class IOConverter : AttributeConverter<IOEnum, Int> {

    override fun convertToDatabaseColumn(attribute: IOEnum): Int = attribute.dbValue

    override fun convertToEntityAttribute(dbData: Int?): IOEnum = IOEnum.fromDbValue(dbData)
}
