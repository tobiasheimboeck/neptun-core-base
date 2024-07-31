package world.neptuns.core.base.common.packet

import world.neptuns.streamline.api.packet.NetworkChannelRegistry
import world.neptuns.streamline.api.packet.NetworkPacket
import java.util.*

data class PlayerDisconnectFromProxyPacket(val uuid: UUID): NetworkPacket(NetworkChannelRegistry.SERVICE)