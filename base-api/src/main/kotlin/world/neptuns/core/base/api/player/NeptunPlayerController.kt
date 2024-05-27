package world.neptuns.core.base.api.player

import kotlinx.coroutines.Deferred
import world.neptuns.controller.api.service.NeptunService
import world.neptuns.streamline.api.cache.container.Cachable
import world.neptuns.streamline.api.utils.Updatable
import java.util.*

interface NeptunPlayerController : Cachable<UUID, NeptunOnlinePlayer>, Updatable<UUID, NeptunOfflinePlayer.Update, Any> {

    suspend fun isOnline(uuid: UUID): Boolean

    suspend fun getOnlinePlayer(uuid: UUID): NeptunOnlinePlayer?

    suspend fun getOnlinePlayersFromService(neptunService: NeptunService): List<NeptunOfflinePlayer>
    suspend fun getOnlinePlayers(): List<NeptunOnlinePlayer>

    suspend fun getOfflinePlayerAsync(uuid: UUID): Deferred<NeptunOfflinePlayer?>
    suspend fun getOfflinePlayersAsync(): Deferred<List<NeptunOfflinePlayer>>

    suspend fun getGloballyRegisteredPlayersAmount(): Deferred<Int>

    suspend fun loadPlayer(uuid: UUID, name: String, skinValue: String, skinSignature: String, proxyServiceName: String, minecraftServiceName: String)
    suspend fun unloadPlayer(uuid: UUID)

}