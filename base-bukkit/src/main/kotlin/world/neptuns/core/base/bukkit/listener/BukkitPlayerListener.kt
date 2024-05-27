package world.neptuns.core.base.bukkit.listener

import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import world.neptuns.core.base.api.language.properties.LanguagePropertiesController
import world.neptuns.core.base.api.player.NeptunPlayerController

class BukkitPlayerListener(
    private val playerController: NeptunPlayerController,
    private val languagePropertiesController: LanguagePropertiesController,
) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    suspend fun onPlayerLogin(event: PlayerLoginEvent) {
        val player = event.player

        val onlinePlayer = this.playerController.getOnlinePlayer(player.uniqueId)
        if (onlinePlayer == null) {
            player.kick(Component.text("No online player with uuid ${player.uniqueId} found!"))
            return
        }

        this.playerController.cacheEntry(player.uniqueId, onlinePlayer)

        val languageProperties = this.languagePropertiesController.getProperties(player.uniqueId)
        if (languageProperties == null) {
            player.kick(Component.text("No language properties with uuid ${player.uniqueId} found!"))
            return
        }

        this.languagePropertiesController.cacheEntry(player.uniqueId, languageProperties)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    suspend fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player

        this.playerController.removeEntryFromCache(player.uniqueId)
        this.languagePropertiesController.removeEntryFromCache(player.uniqueId)
    }

}