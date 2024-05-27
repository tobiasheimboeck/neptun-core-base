package world.neptuns.core.base.api.packet

import world.neptuns.streamline.api.packet.NetworkChannelRegistry
import world.neptuns.streamline.api.packet.NetworkPacket
import java.util.*

data class NeptunPlayerDataLoadedPacket(val uuid: UUID) : NetworkPacket(NetworkChannelRegistry.SERVICE)