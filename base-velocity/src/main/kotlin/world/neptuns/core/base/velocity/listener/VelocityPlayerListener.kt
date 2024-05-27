package world.neptuns.core.base.velocity.listener

import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.LoginEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import world.neptuns.controller.api.NeptunControllerProvider
import world.neptuns.core.base.api.player.NeptunPlayerController

class VelocityPlayerListener(private val playerController: NeptunPlayerController) {

    @Subscribe(order = PostOrder.EARLY)
    suspend fun onPlayerLogin(event: LoginEvent) {
        val player = event.player
        val property = player.gameProfile.properties.first()
        val podName = NeptunControllerProvider.api.podName()

        withContext(Dispatchers.IO) {
            playerController.loadPlayer(player.uniqueId, player.username, property.value, property.signature, podName, "-/-")
        }
    }

    @Subscribe(order = PostOrder.LATE)
    suspend fun onPlayerDisconnect(event: DisconnectEvent) {
        val player = event.player

        withContext(Dispatchers.IO) {
            playerController.unloadPlayer(player.uniqueId)
        }
    }

}