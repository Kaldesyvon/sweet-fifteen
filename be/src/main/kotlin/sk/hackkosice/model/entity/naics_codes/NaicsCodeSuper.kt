package sk.esten.uss.gbco2.model.entity.naics_codes

import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import sk.esten.uss.gbco2.generics.model.IdEntity

@MappedSuperclass
open class NaicsCodeSuper : IdEntity<Long> {

    @Id @Column(name = "ID") var id: Long? = null

    @Column(name = "CODE") var code: String? = null

    @Column(name = "DESCRIPTION") var description: String? = null

    override fun getPk(): Long? = id
}
