package world.neptuns.core.base.api.player

import kotlinx.coroutines.Deferred
import world.neptuns.controller.api.service.NeptunService
import world.neptuns.controller.api.utils.Updatable
import java.util.*

interface NeptunPlayerController : Updatable<UUID, NeptunOfflinePlayer.Update, Any> {

    suspend fun isOnline(uuid: UUID): Deferred<Boolean>

    suspend fun getOnlinePlayerAsync(uuid: UUID): Deferred<NeptunOnlinePlayer?>

    suspend fun getOnlinePlayersFromServiceAsync(neptunService: NeptunService): Deferred<List<NeptunOfflinePlayer>>
    suspend fun getOnlinePlayersAsync(): Deferred<List<NeptunOnlinePlayer>>

    suspend fun getOfflinePlayerAsync(uuid: UUID): Deferred<NeptunOfflinePlayer?>
    suspend fun getOfflinePlayersAsync(): Deferred<List<NeptunOfflinePlayer>>

    suspend fun getGloballyRegisteredPlayersAmount(): Deferred<Int>

    suspend fun handlePlayerCreation(uuid: UUID, name: String, skinValue: String, skinSignature: String, proxyServiceName: String, minecraftServiceName: String)
    suspend fun handlePlayerDeletion(uuid: UUID)

}