package world.neptuns.core.base.bukkit.listener

import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import kotlinx.coroutines.withContext
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import world.neptuns.controller.api.NeptunControllerProvider
import world.neptuns.core.base.api.language.properties.LanguagePropertiesService
import world.neptuns.core.base.api.player.NeptunOfflinePlayer
import world.neptuns.core.base.api.player.NeptunPlayerService
import world.neptuns.core.base.bukkit.NeptunBukkitPlugin

class BukkitPlayerListener(
    private val plugin: NeptunBukkitPlugin,
    private val playerController: NeptunPlayerService,
    private val languagePropertiesService: LanguagePropertiesService,
) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    suspend fun onPlayerLogin(event: PlayerLoginEvent) {
        val player = event.player

        withContext(this.plugin.asyncDispatcher) {
            val onlinePlayer = playerController.getOnlinePlayer(player.uniqueId)

            if (onlinePlayer == null) {
                withContext(plugin.minecraftDispatcher) {
                    player.kick(Component.text("No online player with uuid ${player.uniqueId} found!"))
                }
                return@withContext
            }

            onlinePlayer.currentServiceName = NeptunControllerProvider.api.podName()

            playerController.updateCachedEntry(NeptunOfflinePlayer.Update.CURRENT_SERVICE, player.uniqueId, onlinePlayer.currentServiceName)
            playerController.cacheOnlinePlayer(player.uniqueId, onlinePlayer)

            val languageProperties = languagePropertiesService.getProperties(player.uniqueId)

            if (languageProperties == null) {
                player.kick(Component.text("No language properties with uuid ${player.uniqueId} found!"))
                return@withContext
            }

            languagePropertiesService.cacheLanguageProperties(player.uniqueId, languageProperties)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    suspend fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player

        this.playerController.removeCachedOnlinePlayer(player.uniqueId)
        this.languagePropertiesService.removeLanguageProperties(player.uniqueId)
    }

}