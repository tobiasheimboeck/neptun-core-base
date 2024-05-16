package world.neptuns.core.base.api.player.model

import kotlinx.coroutines.Deferred
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

    suspend fun isOnline(): Deferred<Boolean>

}