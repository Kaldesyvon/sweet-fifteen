package sk.esten.uss.gbco2.generics.model

import sk.esten.uss.gbco2.model.entity.dictionary.Dictionary

interface TranslatedEntity {

    fun translationFieldList(): Collection<Dictionary>
}
