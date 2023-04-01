package sk.esten.uss.gbco2.generics.model

/** entity have to contain 'language' field */
interface IdViewEntity<ID : Any> : IdEntity<ID> {

    fun getLang(): Long?
}
