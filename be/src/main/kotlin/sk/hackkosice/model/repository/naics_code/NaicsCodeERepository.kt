package sk.esten.uss.gbco2.model.repository.naics_code

import org.springframework.stereotype.Repository
import sk.esten.uss.gbco2.generics.model.repository.CrudEntityRepository
import sk.esten.uss.gbco2.model.entity.naics_codes.NaicsCode

@Repository interface NaicsCodeERepository : CrudEntityRepository<NaicsCode, Long>
