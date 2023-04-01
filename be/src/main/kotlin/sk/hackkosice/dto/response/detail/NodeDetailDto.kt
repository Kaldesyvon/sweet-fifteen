package sk.esten.uss.gbco2.dto.response.detail

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import sk.esten.uss.gbco2.dto.response.NodeDto
import sk.esten.uss.gbco2.dto.response.simple.*

@Schema(description = "nodeDetailDto")
class NodeDetailDto : NodeDto() {

    @Schema(description = "nodeType") var nodeType: SimpleNodeTypeDto? = null

    @Schema(description = "fuelType") var fuelType: SimpleFuelTypeDto? = null

    @Schema(description = "landfill") var landfill: SimpleLandfillDto? = null

    @Schema(description = "businessUnit") var businessUnit: SimpleBusinessUnitDto? = null

    @Schema(description = "gpsPosition") var gpsPosition: String? = null

    @Schema(description = "objDiscrim") var objDiscrim: String? = null

    @Schema(description = "qaEnabled") var qaEnabled: Boolean? = null

    @Schema(description = "lockEnabled") var lockEnabled: Boolean? = null

    @Schema(description = "rolesEnabled") var rolesEnabled: Boolean? = null

    @Schema(description = "prNaicsCode") var prNaicsCode: SimpleNaicsCodeDto? = null

    @Schema(description = "seNaicsCode") var seNaicsCode: SimpleNaicsCodeDto? = null

    @Schema(description = "epaCode") var epaCode: String? = null

    @Schema(description = "maxCapacity") var maxCapacity: Long? = null

    @Schema(description = "capacityUnit") var capacityUnit: String? = null

    @Schema(description = "tier") var tier: String? = null

    @Schema(description = "support") var support: String? = null

    @Schema(description = "note1") var note1: String? = null

    @Schema(description = "note2") var note2: String? = null

    @Schema(description = "note3") var note3: String? = null

    @Schema(description = "usepaTriFacilityId") var usepaTriFacilityId: String? = null

    @Schema(description = "usepaRcraFacilityId") var usepaRcraFacilityId: String? = null

    @Schema(description = "dunBradstreetNumber") var dunBradstreetNumber: Long? = null

    @Schema(description = "thermalCapacityOperator") var thermalCapacityOperator: String? = null

    @Schema(description = "usepaThermalCapTier") var usepaThermalCapTier: Long? = null

    @Schema(description = "usepaCuTypeCod") var usepaCuTypeCod: String? = null

    @Schema(description = "addressLine1") var addressLine1: String? = null

    @Schema(description = "addressLine2") var addressLine2: String? = null

    @Schema(description = "addressLine3") var addressLine3: String? = null

    @Schema(description = "timezone") var timezone: String? = null

    @Schema(description = "postalCode") var postalCode: String? = null

    @Schema(description = "stateProvincePostCode")
    var stateProvincePostCode: SimpleStatePostCodeDto? = null

    @Schema(description = "singleCombustionUnit") var singleCombustionUnit: Boolean? = null

    @Schema(description = "gpsLatitude") var gpsLatitude: String? = null

    @Schema(description = "gpsLongitude") var gpsLongitude: String? = null

    @Schema(description = "gpsElevation") var gpsElevation: BigDecimal? = null

    @Schema(description = "gpsElevationUnits") var gpsElevationUnits: String? = null

    @JsonIgnore override var regionId: Long? = null
}
