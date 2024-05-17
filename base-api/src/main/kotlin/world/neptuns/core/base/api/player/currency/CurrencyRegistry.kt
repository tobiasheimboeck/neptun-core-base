package world.neptuns.core.base.api.player.currency

import world.neptuns.core.base.api.player.currency.type.Crystals
import world.neptuns.core.base.api.player.currency.type.Shards
import world.neptuns.core.base.api.utils.ClassRegistry

class CurrencyRegistry {

    object Default : ClassRegistry<Currency> {
        override val elements: MutableSet<Currency> = mutableSetOf()

        val CRYSTALS = Crystals()
        val SHARDS = Shards()

        init {
            elements.addAll(listOf(CRYSTALS, SHARDS))
        }
    }

}