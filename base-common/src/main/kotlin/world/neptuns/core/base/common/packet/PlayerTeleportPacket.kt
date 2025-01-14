package world.neptuns.core.base.common.packet

import world.neptuns.streamline.api.packet.NetworkChannelRegistry
import world.neptuns.streamline.api.packet.NetworkPacket
import java.util.*

data class PlayerTeleportPacket(
    val uuid: UUID,
    val worldName: String?,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float,
) : NetworkPacket(NetworkChannelRegistry.SERVICE)