package sk.esten.uss.gbco2.generics.model.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import sk.esten.uss.gbco2.generics.model.IdViewEntity

@NoRepositoryBean
interface CrudViewRepository<VIEW : IdViewEntity<ID>, ID : Any> :
    JpaRepository<VIEW, ID>, JpaSpecificationExecutor<VIEW>
