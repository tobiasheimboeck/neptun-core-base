package world.neptuns.core.base.common.packet

import world.neptuns.controller.api.packet.NetworkPacket
import world.neptuns.core.base.api.event.PlayerColorChangeEvent
import world.neptuns.core.base.api.language.properties.LanguageProperties
import java.util.*

data class PlayerColorChangePacket(val uuid: UUID, val languageProperties: LanguageProperties) : NetworkPacket(
    "network:message-service:all-services",
    setOf(PlayerColorChangeEvent(uuid, languageProperties))
)