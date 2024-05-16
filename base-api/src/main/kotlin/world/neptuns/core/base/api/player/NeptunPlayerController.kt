package world.neptuns.core.base.api.player

import kotlinx.coroutines.Deferred
import world.neptuns.controller.api.service.NeptunService
import world.neptuns.core.base.api.player.model.NeptunOfflinePlayer
import world.neptuns.core.base.api.player.model.NeptunOnlinePlayer
import java.util.*

interface NeptunPlayerController {

    suspend fun isOnline(uuid: UUID): Deferred<Boolean>

    suspend fun getOnlinePlayerAsync(uuid: UUID): Deferred<NeptunOnlinePlayer?>
    fun getOnlinePlayerFromCurrentService(uuid: UUID): NeptunOnlinePlayer?

    suspend fun getOnlinePlayersFromServiceAsync(neptunService: NeptunService): Deferred<List<NeptunOfflinePlayer>>
    suspend fun getOnlinePlayersAsync(): Deferred<List<NeptunOnlinePlayer>>
    fun getOnlinePlayersFromCurrentService(): List<NeptunOnlinePlayer>

    suspend fun getOfflinePlayerAsync(uuid: UUID): Deferred<NeptunOfflinePlayer?>
    suspend fun getOfflinePlayersAsync(): Deferred<List<NeptunOfflinePlayer>>

    suspend fun getGloballyRegisteredPlayersAmount(): Deferred<Int>

    fun handlePlayerCreation(uuid: UUID)
    fun handlePlayerDeletion(uuid: UUID)

}