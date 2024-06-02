package world.neptuns.core.base.bukkit.listener

import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import world.neptuns.controller.api.NeptunControllerProvider
import world.neptuns.core.base.api.language.properties.LanguagePropertiesController
import world.neptuns.core.base.api.packet.NeptunPlayerDataLoadedPacket
import world.neptuns.core.base.api.player.NeptunOfflinePlayer
import world.neptuns.core.base.api.player.NeptunPlayerController
import world.neptuns.core.base.bukkit.NeptunBukkitPlugin
import world.neptuns.core.base.common.repository.language.LanguagePropertiesRepository
import world.neptuns.core.base.common.repository.player.OnlinePlayerCache
import world.neptuns.core.base.common.repository.player.OnlinePlayerRepository
import world.neptuns.streamline.api.NeptunStreamlineProvider

class BukkitPlayerListener(
    private val plugin: NeptunBukkitPlugin,
    private val playerController: NeptunPlayerController,
    private val languagePropertiesController: LanguagePropertiesController,
) : Listener {

    private val playerRepository = NeptunStreamlineProvider.api.repositoryLoader.get(OnlinePlayerRepository::class.java)!!
    private val languagePropertiesRepository = NeptunStreamlineProvider.api.repositoryLoader.get(LanguagePropertiesRepository::class.java)!!

    @EventHandler(priority = EventPriority.LOWEST)
    suspend fun onPlayerLogin(event: PlayerLoginEvent) {
        val player = event.player

        withContext(this.plugin.asyncDispatcher) {
            val onlinePlayer = playerController.getOnlinePlayer(player.uniqueId)

            if (onlinePlayer == null) {
                player.kick(Component.text("No online player with uuid ${player.uniqueId} found!"))
                return@withContext
            }

            onlinePlayer.currentServiceName = NeptunControllerProvider.api.podName()

            playerController.updateCachedEntry(NeptunOfflinePlayer.Update.CURRENT_SERVICE, player.uniqueId, onlinePlayer.currentServiceName)
            playerController.cacheEntry(player.uniqueId, onlinePlayer)

            val languageProperties = languagePropertiesController.getProperties(player.uniqueId)

            if (languageProperties == null) {
                player.kick(Component.text("No language properties with uuid ${player.uniqueId} found!"))
                return@withContext
            }

            languagePropertiesController.cacheEntry(player.uniqueId, languageProperties)
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    suspend fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        event.joinMessage(null)

        withContext(this.plugin.asyncDispatcher) {
            val onlinePlayer = playerRepository.get(player.uniqueId).await() ?: return@withContext
            val languageProperties = languagePropertiesRepository.get(player.uniqueId).await() ?: return@withContext

            val packet = NeptunPlayerDataLoadedPacket(player.uniqueId, onlinePlayer, languageProperties)
            NeptunStreamlineProvider.api.packetController.sendPacket(packet)
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    suspend fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player

        this.playerController.removeEntryFromCache(player.uniqueId)
        this.languagePropertiesController.removeEntryFromCache(player.uniqueId)
    }

}