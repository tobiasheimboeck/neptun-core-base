package world.neptuns.core.base.common.packet

import world.neptuns.controller.api.packet.NetworkPacket
import java.util.*

data class PlayerPerformCommandPacket(override val channelName: String, val uuid: UUID, val command: String) : NetworkPacket(channelName)