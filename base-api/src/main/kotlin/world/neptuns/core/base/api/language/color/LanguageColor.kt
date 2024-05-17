package world.neptuns.core.base.api.language.color

import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.player.currency.Buyable
import world.neptuns.core.base.api.player.currency.type.Crystals
import java.io.Serializable

interface LanguageColor : Buyable<Crystals>, Serializable {

    val name: LineKey
    val permission: String?
    val hexFormat: String

}