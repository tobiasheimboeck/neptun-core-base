package world.neptuns.core.base.api.player.model

import world.neptuns.core.base.api.player.PlayerSession
import world.neptuns.core.base.api.skin.SkinProfile
import java.util.*

interface NeptunOfflinePlayer {

    val uuid: UUID
    var name: String

    val session: PlayerSession
    val skinProfile: SkinProfile

    var crystals: Long
    var shards: Long

    fun isOnline(): Boolean

}