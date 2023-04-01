package sk.esten.uss.gbco2.model.entity.naics_codes

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "GBC_NAICS_CODES")
class NaicsCode : NaicsCodeSuper(), Serializable {
    override fun getPk(): Long? = id
}
