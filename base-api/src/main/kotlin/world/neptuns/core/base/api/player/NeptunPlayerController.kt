package world.neptuns.core.base.api.player

import kotlinx.coroutines.Deferred
import world.neptuns.controller.api.service.NeptunService
import world.neptuns.streamline.api.utils.Controller
import world.neptuns.streamline.api.utils.Updatable
import java.util.*

interface NeptunPlayerController : Controller<UUID, NeptunOfflinePlayer>, Updatable<UUID, NeptunOfflinePlayer.Update, Any> {

    suspend fun isOnline(uuid: UUID): Boolean

    suspend fun getOnlinePlayer(uuid: UUID): NeptunOnlinePlayer?
    suspend fun getOnlinePlayersFromService(neptunService: NeptunService): List<NeptunOfflinePlayer>
    suspend fun getOnlinePlayers(): List<NeptunOnlinePlayer>

    suspend fun getOfflinePlayerAsync(uuid: UUID): Deferred<NeptunOfflinePlayer?>
    suspend fun getOfflinePlayersAsync(): Deferred<List<NeptunOfflinePlayer>>

    suspend fun getGloballyRegisteredPlayersAmount(): Deferred<Int>

}