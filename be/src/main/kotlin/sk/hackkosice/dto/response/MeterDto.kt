package sk.esten.uss.gbco2.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigInteger
import java.time.LocalDate
import sk.esten.uss.gbco2.dto.response.simple.*

@Schema(description = "meterDto")
class MeterDto : SimpleMeterDto() {
    @Schema(description = "nameShort") var nameShort: String? = null

    @Schema(description = "factoryNumber") var factoryNumber: String? = null

    @Schema(description = "meterId") var meterid: String? = null

    @Schema(description = "model") var model: String? = null

    @Schema(description = "location") var location: String? = null

    @Schema(description = "description") var description: String? = null

    @Schema(description = "note1") var note1: String? = null

    @Schema(description = "note2") var note2: String? = null

    @Schema(description = "note3") var note3: String? = null

    @Schema(description = "validFrom") var validFrom: LocalDate? = null

    @Schema(description = "validTo") var validTo: LocalDate? = null

    @Schema(description = "objVersion") var objVersion: Long = 0

    @Schema(description = "gpsLatitude") var gpsLatitude: String? = null

    @Schema(description = "gpsLongitude") var gpsLongitude: String? = null

    @Schema(description = "meterModel") var meterModel: String? = null

    @Schema(description = "ratedAccuracy") var ratedAccuracy: String? = null

    @Schema(description = "gpsElevation") var gpsElevation: BigInteger? = null

    @Schema(description = "gpsElevationUnits") var gpsElevationUnits: String? = null

    @Schema(description = "simpleUnitTypeDto") var unitType: SimpleUnitTypeDto? = null

    @Schema(description = "simpleFuelTypeDto") var fuelType: SimpleFuelTypeDto? = null

    @Schema(description = "simpleMeterTypeDto") var meterType: SimpleMeterTypeDto? = null

    @Schema(description = "meterUncertaintyDto") var meterUncertainty: MeterUncertaintyDto? = null

    @Schema(description = "simpleNodeLocationDto") var nodeLocation: SimpleNodeDto? = null

    @Schema(description = "max certificate valid to") var maxValidDate: LocalDate? = null
}
