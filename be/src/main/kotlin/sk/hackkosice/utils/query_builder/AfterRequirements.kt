package sk.esten.uss.gbco2.utils.query_builder

import sk.esten.uss.gbco2.utils.CustomQueryBuilder

interface AfterRequirements {

    fun addYears(years: List<Int>?): AfterRequirements

    fun addYtd(ytd: Int?): AfterRequirements

    fun addScopeId(scopeId: Long?): AfterRequirements

    fun addCountryId(countryId: Long?): AfterRequirements

    fun addNodesWithSubNodes(nodesWithSubNodeIds: List<Long>?): AfterRequirements

    fun addMaterialIds(materialIds: List<Long>?): AfterRequirements

    fun addAnalysisParamIds(analysisParamIds: List<Long>?): AfterRequirements

    fun addScopeIds(scopeIds: List<Long>?): AfterRequirements

    fun generateQuery(): CustomQueryBuilder
}
