package world.neptuns.core.base.velocity.listener

import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.PostLoginEvent
import com.velocitypowered.api.plugin.PluginContainer
import world.neptuns.controller.api.NeptunControllerProvider
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.properties.LanguagePropertiesController
import world.neptuns.core.base.api.player.NeptunPlayerController
import world.neptuns.core.base.common.api.language.properties.LanguagePropertiesImpl
import world.neptuns.core.base.common.api.player.NeptunOfflinePlayerImpl

class VelocityPlayerListener(
    private val pluginContainer: PluginContainer,
    private val playerController: NeptunPlayerController,
    private val languagePropertiesController: LanguagePropertiesController,
) {

    @Subscribe
    suspend fun onPlayerLogin(event: PostLoginEvent) {
        val player = event.player
        val property = player.gameProfile.properties.first()
        val podName = NeptunControllerProvider.api.podName()

        this.playerController.createOrLoadEntry(player.uniqueId, NeptunOfflinePlayerImpl.create(player.uniqueId, player.username, property.value, property.signature), podName)
        this.languagePropertiesController.createOrLoadEntry(player.uniqueId, LanguagePropertiesImpl.create(player.uniqueId))
    }

    @Subscribe(order = PostOrder.LATE)
    suspend fun onPlayerDisconnect(event: DisconnectEvent) {
        val player = event.player

        val onlinePlayer = NeptunCoreProvider.api.playerController.getOnlinePlayer(player.uniqueId)

        if (onlinePlayer != null) {
            onlinePlayer.lastLogoutTimestamp = System.currentTimeMillis()
            onlinePlayer.updateOnlineTime()
        }

        this.playerController.unloadEntry(player.uniqueId)
        this.languagePropertiesController.unloadEntry(player.uniqueId)
    }
}