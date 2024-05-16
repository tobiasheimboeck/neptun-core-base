package world.neptuns.core.base.common.api.player

import world.neptuns.core.base.api.player.NeptunOfflinePlayer
import world.neptuns.core.base.api.player.NeptunOnlinePlayer
import world.neptuns.core.base.api.skin.SkinProfile
import java.util.*

class NeptunOnlinePlayerImpl(
    override val uuid: UUID,
    override var name: String,
    override val firstLoginTimestamp: Long,
    override var lastLoginTimestamp: Long,
    override var lastLogoutTimestamp: Long,
    override var onlineTime: Long,
    override val skinProfile: SkinProfile,
    override var crystals: Long,
    override var shards: Long,
    override var currentProxyName: String,
    override var currentServiceName: String,
) : NeptunOnlinePlayer {

    companion object {
        fun create(offlinePlayer: NeptunOfflinePlayer, proxyServiceName: String, minecraftServiceName: String) = NeptunOnlinePlayerImpl(
            offlinePlayer.uuid,
            offlinePlayer.name,
            offlinePlayer.firstLoginTimestamp,
            offlinePlayer.lastLoginTimestamp,
            offlinePlayer.lastLogoutTimestamp,
            offlinePlayer.onlineTime,
            offlinePlayer.skinProfile,
            offlinePlayer.crystals,
            offlinePlayer.shards,
            proxyServiceName,
            minecraftServiceName
        )
    }

}