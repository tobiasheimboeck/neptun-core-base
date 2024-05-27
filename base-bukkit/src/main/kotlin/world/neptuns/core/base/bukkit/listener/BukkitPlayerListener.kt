package world.neptuns.core.base.bukkit.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import world.neptuns.core.base.api.player.NeptunPlayerController

class BukkitPlayerListener(private val playerController: NeptunPlayerController) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

    }

}