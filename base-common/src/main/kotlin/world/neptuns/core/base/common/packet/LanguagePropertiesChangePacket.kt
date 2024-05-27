package world.neptuns.core.base.common.packet

import world.neptuns.core.base.api.event.LanguagePropertiesChangeEvent
import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.streamline.api.packet.NetworkChannelRegistry
import world.neptuns.streamline.api.packet.NetworkPacket
import java.util.*

data class LanguagePropertiesChangePacket(val uuid: UUID, val languageProperties: LanguageProperties) : NetworkPacket(
    NetworkChannelRegistry.PROXY_AND_SERVICE,
    setOf(LanguagePropertiesChangeEvent(uuid, languageProperties))
)