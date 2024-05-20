package world.neptuns.core.base.common.packet

import world.neptuns.controller.api.packet.NetworkChannelRegistry
import world.neptuns.controller.api.packet.NetworkPacket
import java.util.*

data class PlayerConnectToServicePacket(val uuid: UUID, val isLobbyRequest: Boolean, val serviceName: String?) : NetworkPacket(NetworkChannelRegistry.PROXY)