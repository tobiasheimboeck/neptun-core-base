package world.neptuns.core.base.api.currency

import world.neptuns.core.base.api.player.NeptunOfflinePlayer

interface Buyable<T : Currency> {

    val requiredCurrency: T
    val price: Long

    fun buy(offlinePlayer: NeptunOfflinePlayer) = requiredCurrency.onSpend(offlinePlayer)

}