package world.neptuns.core.base.api.event

import world.neptuns.controller.api.event.NetworkEvent
import world.neptuns.core.base.api.language.properties.LanguageProperties
import java.util.*

class PlayerColorChangeEvent(val uuid: UUID, val languageProperties: LanguageProperties) : NetworkEvent()