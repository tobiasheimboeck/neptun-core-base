package world.neptuns.core.base.api.language.color

import world.neptuns.core.base.api.player.currency.Buyable
import world.neptuns.core.base.api.player.currency.CurrencyRegistry

interface LanguageColor : Buyable<CurrencyRegistry.Crystals> {

    val name: String
    val permission: String?
    val hexFormat: String

}