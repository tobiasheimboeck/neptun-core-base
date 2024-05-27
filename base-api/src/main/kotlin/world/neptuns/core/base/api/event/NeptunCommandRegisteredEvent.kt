package world.neptuns.core.base.api.event

import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.streamline.api.event.NetworkEvent

data class NeptunCommandRegisteredEvent(val command: NeptunCommand) : NetworkEvent()