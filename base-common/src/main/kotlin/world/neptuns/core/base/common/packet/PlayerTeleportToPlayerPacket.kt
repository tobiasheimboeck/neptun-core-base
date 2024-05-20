package world.neptuns.core.base.common.packet

import world.neptuns.controller.api.packet.NetworkChannelRegistry
import world.neptuns.controller.api.packet.NetworkPacket
import java.util.*

data class PlayerTeleportToPlayerPacket(val uuid: UUID, val targetUniqueId: UUID) : NetworkPacket(NetworkChannelRegistry.SERVICE)