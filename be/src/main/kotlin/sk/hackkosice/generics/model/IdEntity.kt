package sk.esten.uss.gbco2.generics.model

interface IdEntity<ID : Any> {

    fun getPk(): ID?
}
