package world.neptuns.core.base.velocity.listener

import com.velocitypowered.api.event.Continuation
import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.LoginEvent
import world.neptuns.controller.api.NeptunControllerProvider
import world.neptuns.controller.api.NetworkControllerApi
import world.neptuns.core.base.api.player.NeptunPlayerController

class VelocityPlayerListener(private val playerController: NeptunPlayerController) {

    @Subscribe(order = PostOrder.FIRST)
    fun onPlayerLogin(event: LoginEvent, continuation: Continuation) {
        val player = event.player
        val property = player.gameProfile.properties.first()
        val podName = NeptunControllerProvider.api.podName()

        NetworkControllerApi.launch {
            playerController.loadPlayer(player.uniqueId, player.username, property.value, property.signature, podName, "-/-")
            continuation.resume()
        }
    }

    @Subscribe(order = PostOrder.LAST)
    fun onPlayerDisconnect(event: DisconnectEvent) {
        val player = event.player

        NetworkControllerApi.launch {
            playerController.unloadPlayer(player.uniqueId)
        }
    }

}