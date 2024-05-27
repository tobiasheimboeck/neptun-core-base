package world.neptuns.core.base.common.packet

import world.neptuns.streamline.api.packet.NetworkChannelRegistry
import world.neptuns.streamline.api.packet.NetworkPacket
import java.util.*

data class MessageToPlayerPacket(val uuid: UUID, val key: String, val toReplace: List<Pair<String, String>>) : NetworkPacket(NetworkChannelRegistry.PROXY)