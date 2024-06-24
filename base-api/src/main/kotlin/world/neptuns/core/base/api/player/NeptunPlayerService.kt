package world.neptuns.core.base.api.player

import kotlinx.coroutines.Deferred
import world.neptuns.controller.api.service.NeptunService
import world.neptuns.streamline.api.utils.Updatable
import java.util.*

interface NeptunPlayerService : Updatable<UUID, NeptunOfflinePlayer.Update, Any> {

    suspend fun isOnline(uuid: UUID): Boolean

    suspend fun getOnlinePlayer(uuid: UUID): NeptunOnlinePlayer?
    suspend fun getOnlinePlayer(name: String): NeptunOnlinePlayer?

    suspend fun getOnlinePlayersFromService(neptunService: NeptunService): List<NeptunOfflinePlayer>
    suspend fun getOnlinePlayers(): List<NeptunOnlinePlayer>

    suspend fun getOfflinePlayerAsync(uuid: UUID): Deferred<NeptunOfflinePlayer?>

    suspend fun getGloballyRegisteredPlayersAmount(): Deferred<Int>

    suspend fun createOfflinePlayer(uuid: UUID, name: String, skinValue: String, skinSignature: String, currentProxyName: String)

    suspend fun loadOnlinePlayer(uuid: UUID, name: String, skinValue: String, skinSignature: String, currentProxyName: String): Deferred<NeptunOfflinePlayer?>

    suspend fun unloadOnlinePlayer(uuid: UUID)

    suspend fun cacheOnlinePlayer(uuid: UUID, onlinePlayer: NeptunOnlinePlayer)

    suspend fun removeCachedOnlinePlayer(uuid: UUID)

}