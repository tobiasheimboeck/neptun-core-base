package world.neptuns.core.base.api.player

import world.neptuns.core.base.api.player.model.NeptunOfflinePlayer
import world.neptuns.core.base.api.player.model.NeptunPlayer
import java.util.*

interface NeptunPlayerController {

    val cachedOnlinePlayers: MutableMap<UUID, NeptunPlayer>

    fun getOnlinePlayer(uuid: UUID): NeptunPlayer?
    fun getOfflinePlayer(uuid: UUID): NeptunOfflinePlayer?

    fun createOfflinePlayer(uuid: UUID)
    fun deleteOfflinePlayer(uuid: UUID)

    fun cacheOnlinePlayer(uuid: UUID)
    fun removeCachedOnlinePlayer(uuid: UUID)

}