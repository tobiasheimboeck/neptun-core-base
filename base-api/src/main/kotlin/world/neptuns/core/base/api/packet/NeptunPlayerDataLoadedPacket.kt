package world.neptuns.core.base.api.packet

import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.core.base.api.player.NeptunOnlinePlayer
import world.neptuns.streamline.api.packet.NetworkChannelRegistry
import world.neptuns.streamline.api.packet.NetworkPacket
import java.util.*

data class NeptunPlayerDataLoadedPacket(
    val uuid: UUID,
    val onlinePlayer: NeptunOnlinePlayer,
    val languageProperties: LanguageProperties
) : NetworkPacket(NetworkChannelRegistry.SERVICE)