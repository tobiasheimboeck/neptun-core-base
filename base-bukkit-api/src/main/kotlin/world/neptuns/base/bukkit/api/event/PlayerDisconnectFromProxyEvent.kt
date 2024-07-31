package world.neptuns.base.bukkit.api.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import java.util.*

class PlayerDisconnectFromProxyEvent(val uuid: UUID) : Event() {

    override fun getHandlers(): HandlerList = handlerList

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

}