package sk.esten.uss.gbco2.model.repository.naics_code

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sk.esten.uss.gbco2.model.entity.naics_codes.NaicsCode

@Repository interface NaicsCodeRepository : JpaRepository<NaicsCode, Long>
