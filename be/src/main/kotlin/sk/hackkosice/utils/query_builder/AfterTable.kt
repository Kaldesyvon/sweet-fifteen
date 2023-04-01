package sk.esten.uss.gbco2.utils.query_builder

interface AfterTable {

    fun addRegion(): AfterTable

    fun addMaterial(): AfterTable

    fun addScope(): AfterTable

    fun addCountry(): AfterTable

    fun addNode(nodeLevel: Long): AfterTable

    fun addIO(): AfterTable

    fun addYear(): AfterTable

    fun addMonth(): AfterTable

    fun addNodeType(): AfterTable

    fun where(): AfterRequirements
}
