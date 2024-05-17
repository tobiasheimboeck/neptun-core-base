package world.neptuns.core.base.api.player

import kotlinx.coroutines.Deferred
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.skin.SkinProfile
import java.util.*

interface NeptunOfflinePlayer {

    val uuid: UUID
    var name: String

    val firstLoginTimestamp: Long
    var lastLoginTimestamp: Long
    var lastLogoutTimestamp: Long

    var onlineTime: Long
    val skinProfile: SkinProfile

    var crystals: Long
    var shards: Long

    fun updateOnlineTime() {
        val playedDuration: Long = this.lastLogoutTimestamp - this.lastLoginTimestamp
        this.onlineTime += playedDuration
    }

    suspend fun isOnline(): Deferred<Boolean> {
        return NeptunCoreProvider.api.playerController.isOnline(uuid)
    }

    enum class Update {
        ALL,
        NAME,
        LAST_LOGIN_TIMESTAMP,
        LAST_LOGOUT_TIMESTAMP,
        ONLINE_TIME,
        SKIN_VALUE,
        SKIN_SIGNATURE,
        CRYSTALS,
        SHARDS
    }

    enum class Reward {
        CRYSTALS,
        SHARDS
    }

}