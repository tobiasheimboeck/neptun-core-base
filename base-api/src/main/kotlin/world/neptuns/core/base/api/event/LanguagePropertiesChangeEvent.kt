package world.neptuns.core.base.api.event

import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.streamline.api.event.NetworkEvent
import java.util.*

data class LanguagePropertiesChangeEvent(val uuid: UUID, val languageProperties: LanguageProperties) : NetworkEvent()