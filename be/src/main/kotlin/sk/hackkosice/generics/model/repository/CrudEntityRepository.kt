package sk.esten.uss.gbco2.generics.model.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import sk.esten.uss.gbco2.generics.model.IdEntity

@NoRepositoryBean
interface CrudEntityRepository<ENTITY : IdEntity<ID>, ID : Any> :
    JpaRepository<ENTITY, ID>, JpaSpecificationExecutor<ENTITY>
