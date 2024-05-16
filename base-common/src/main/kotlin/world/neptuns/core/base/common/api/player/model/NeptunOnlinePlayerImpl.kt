package world.neptuns.core.base.common.api.player.model

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.player.PlayerAdapter
import world.neptuns.core.base.api.player.PlayerSession
import world.neptuns.core.base.api.player.model.NeptunOnlinePlayer
import world.neptuns.core.base.api.skin.SkinProfile
import java.util.*

class NeptunOnlinePlayerImpl(
    override val uuid: UUID,
    override var currentProxyName: String,
    override var currentServiceName: String,
    override var name: String,
    override val session: PlayerSession,
    override val skinProfile: SkinProfile,
    override var crystals: Long,
    override var shards: Long,
) : NeptunOnlinePlayer {

    override fun adapter(): PlayerAdapter<*> = NeptunCoreProvider.api.playerAdapter

    override suspend fun isOnline(): Deferred<Boolean> = withContext(Dispatchers.IO) {
        NeptunCoreProvider.api.getPlayerController().isOnline(uuid)
    }

}