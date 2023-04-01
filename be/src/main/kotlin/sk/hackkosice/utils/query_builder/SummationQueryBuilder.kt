package sk.esten.uss.gbco2.utils.query_builder

import javax.management.BadAttributeValueExpException
import sk.esten.uss.gbco2.model.entity.node_level.NodeLevelSuper
import sk.esten.uss.gbco2.utils.CustomQueryBuilder
import sk.esten.uss.gbco2.utils.principalUnitSetIdOrMetricId

class SummationQueryBuilder(private val tableName: String) : AfterRequirements, AfterTable {

    // requirements
    val keys: MutableList<String> = mutableListOf()

    // params
    private var years: List<Int>? = null
    private var ytd: Int? = null
    private var scopeId: Long? = null
    private var countryId: Long? = null
    private var nodesWithSubNodeIds: List<Long>? = null
    private var materialIds: List<Long>? = null
    private var analysisParamIds: List<Long>? = null
    private var scopeIds: List<Long>? = null

    private var nodeLevel: Long? = null

    // after table init, functions to set key or column definitions with join tables if needed
    override fun addRegion(): AfterTable {
        keys.add("qm.ID_REGION")
        return this
    }

    override fun addMaterial(): AfterTable {
        keys.add("qm.ID_MATERIAL")
        return this
    }

    override fun addScope(): AfterTable {
        keys.add("${if (tableName == "VGBC_QUANTITY_MONTH_REPORT") "smn" else "qm"}.ID_SCOPE")
        return this
    }

    override fun addCountry(): AfterTable {
        keys.add("qm.ID_COUNTRY")
        return this
    }

    override fun addNode(nodeLevel: Long): AfterTable {
        this.nodeLevel = nodeLevel
        val selectedNode =
            when (nodeLevel) {
                NodeLevelSuper.NODE_LEVEL_SUPER_PLANT -> "qm.ID_SUPER_PLANT"
                NodeLevelSuper.NODE_LEVEL_PLANT -> "qm.ID_PLANT"
                else -> "qm.ID_NODE"
            }
        keys.add(selectedNode)
        return this
    }

    override fun addIO(): AfterTable {
        keys.add("qm.IO")
        return this
    }

    override fun addYear(): AfterTable {
        keys.add("EXTRACT(YEAR FROM qm.MONTH)")
        return this
    }

    override fun addNodeType(): AfterTable {
        keys.add("qm.ID_NODE_TYPE")
        return this
    }

    override fun addMonth(): AfterTable {
        keys.add("EXTRACT(MONTH FROM qm.MONTH)")
        return this
    }

    override fun where(): AfterRequirements = this

    // after requirements specifications, set conditions and parameters
    override fun addYears(years: List<Int>?): AfterRequirements {
        years?.let { this.years = it }
        return this
    }

    override fun addYtd(ytd: Int?): AfterRequirements {
        ytd?.let { this.ytd = it }
        return this
    }

    override fun addScopeId(scopeId: Long?): AfterRequirements {
        scopeId?.let { this.scopeId = it }
        return this
    }

    override fun addCountryId(countryId: Long?): AfterRequirements {
        countryId?.let { this.countryId = it }
        return this
    }

    override fun addNodesWithSubNodes(nodesWithSubNodeIds: List<Long>?): AfterRequirements {
        nodesWithSubNodeIds?.let { this.nodesWithSubNodeIds = it }
        return this
    }

    override fun addMaterialIds(materialIds: List<Long>?): AfterRequirements {
        materialIds?.let { this.materialIds = it }
        return this
    }

    override fun addAnalysisParamIds(analysisParamIds: List<Long>?): AfterRequirements {
        analysisParamIds?.let { this.analysisParamIds = it }
        return this
    }

    override fun addScopeIds(scopeIds: List<Long>?): AfterRequirements {
        scopeIds?.let { this.scopeIds = it }
        return this
    }

    // generate final query
    override fun generateQuery(): CustomQueryBuilder {
        val query =
            CustomQueryBuilder(
                """
            SELECT ('(' || ${
                    keys.joinToString(" || ')-(' || ")
                } || ')') as id, 
            ${
                    when (tableName) {
                        "VGBC_QUANTITY_MONTH_REPORT" -> """
                        SUM(qm.MONTH_QUANTITY) as value,
                        qm.UNIT_ABBR AS unitAbbr from VGBC_QUANTITY_MONTH_REPORT qm 
                    """.trimIndent()
                        "VGBC_RESULTS_MONTH_REPORT" -> """
                        SUM(qm.ANALYTICAL_VALUE) as value from VGBC_RESULTS_MONTH_REPORT qm
                    """.trimIndent()
                        else -> throw BadAttributeValueExpException("Table for summation query not specified!")
                    }
                }
        """.trimIndent()
            )

        if ((scopeId != null || !scopeIds.isNullOrEmpty()) &&
                tableName != "VGBC_RESULTS_MONTH_REPORT"
        ) {
            query +
                """ left join GBC_SCOPE_MATERIAL_NODE smn on qm.ID_MATERIAL_NODE = smn.ID_MATERIAL_NODE """
        }

        // create where clause
        query + """ where qm.ID_UNIT_SET = :unitSetId """
        query.addParam("unitSetId", principalUnitSetIdOrMetricId())

        if (!years.isNullOrEmpty()) {
            query + """ and EXTRACT(YEAR FROM qm.MONTH) IN :years """
            query.addParam("years", years)
        }

        ytd?.let {
            query + """ and EXTRACT(MONTH FROM qm.MONTH) <= :ytd """
            query.addParam("ytd", it)
        }

        scopeId?.let {
            query +
                if (tableName == "VGBC_RESULTS_MONTH_REPORT") {
                    """ AND qm.ID_SCOPE = :scopeId """
                } else {
                    """ AND smn.ID_SCOPE = :scopeId """
                }
            query.addParam("scopeId", it)
        }

        if (!nodesWithSubNodeIds.isNullOrEmpty()) {
            query.appendIdsIsIn(
                nodesWithSubNodeIds,
                "qm",
                "ID_NODE",
                when (this.nodeLevel) {
                    NodeLevelSuper.NODE_LEVEL_SUPER_PLANT -> "superPlants"
                    NodeLevelSuper.NODE_LEVEL_PLANT -> "plants"
                    else -> "nodes"
                }
            )
        }

        if (!materialIds.isNullOrEmpty()) {
            query.appendIdsIsIn(materialIds, "qm", "ID_MATERIAL")
        }

        if (!scopeIds.isNullOrEmpty()) {
            query.appendIdsIsIn(
                scopeIds,
                if (tableName == "VGBC_RESULTS_MONTH_REPORT") "qm" else "smn",
                "ID_SCOPE"
            )
        }

        if (!analysisParamIds.isNullOrEmpty()) {
            query.appendIdsIsIn(analysisParamIds, "qm", "ID_ANALYSIS_PARAM")
        }

        query +
            """ group by 
            ${
                    keys.joinToString(", ")
                }
            ${
                    if (tableName == "VGBC_QUANTITY_MONTH_REPORT") ", qm.UNIT_ABBR" else ""
                }
            """
        return query
    }
}
