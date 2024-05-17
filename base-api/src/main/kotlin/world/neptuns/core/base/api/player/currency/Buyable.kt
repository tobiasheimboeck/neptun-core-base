package world.neptuns.core.base.api.player.currency

import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.player.NeptunOfflinePlayer

interface Buyable<T : Currency> {

    val requiredCurrency: T
    val description: LineKey // gives back a description with the currency name and the ways to earn the currency
    val price: Long

    fun buy(offlinePlayer: NeptunOfflinePlayer) = requiredCurrency.onSpend(offlinePlayer)

}