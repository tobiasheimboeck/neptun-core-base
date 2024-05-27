package world.neptuns.core.base.velocity.listener

import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.LoginEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import world.neptuns.controller.api.NeptunControllerProvider
import world.neptuns.core.base.api.language.properties.LanguagePropertiesController
import world.neptuns.core.base.api.packet.NeptunPlayerDataLoadedPacket
import world.neptuns.core.base.api.player.NeptunPlayerController
import world.neptuns.core.base.common.api.language.properties.LanguagePropertiesImpl
import world.neptuns.core.base.common.api.player.NeptunOfflinePlayerImpl
import world.neptuns.streamline.api.NeptunStreamlineProvider

class VelocityPlayerListener(
    private val playerController: NeptunPlayerController,
    private val languagePropertiesController: LanguagePropertiesController,
) {

    @Subscribe(order = PostOrder.EARLY)
    suspend fun onPlayerLogin(event: LoginEvent) {
        val player = event.player
        val property = player.gameProfile.properties.first()
        val podName = NeptunControllerProvider.api.podName()

        withContext(Dispatchers.IO) {
            val playerResult = playerController.createOrLoadEntry(player.uniqueId, NeptunOfflinePlayerImpl.create(player.uniqueId, player.username, property.value, property.signature), podName)
            val propertiesResult = languagePropertiesController.createOrLoadEntry(player.uniqueId, LanguagePropertiesImpl.create(player.uniqueId))

            if (playerResult.await() && propertiesResult.await()) {
                NeptunStreamlineProvider.api.packetController.sendPacket(NeptunPlayerDataLoadedPacket(player.uniqueId))
                println("==> Sent player data loaded packet")
            }
        }
    }

    @Subscribe(order = PostOrder.LATE)
    suspend fun onPlayerDisconnect(event: DisconnectEvent) {
        val player = event.player

        withContext(Dispatchers.IO) {
            playerController.unloadEntry(player.uniqueId)
            languagePropertiesController.unloadEntry(player.uniqueId)
        }
    }

}