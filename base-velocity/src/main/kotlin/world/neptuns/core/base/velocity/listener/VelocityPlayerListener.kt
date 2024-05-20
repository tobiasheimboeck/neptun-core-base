package world.neptuns.core.base.velocity.listener

import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.proxy.Player
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import world.neptuns.controller.api.NeptunControllerProvider
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.player.NeptunPlayerController
import world.neptuns.core.base.api.utils.NeptunPluginAdapter
import java.util.*

@OptIn(DelicateCoroutinesApi::class)
class VelocityPlayerListener(private val neptunPlugin: NeptunPluginAdapter, private val playerController: NeptunPlayerController) {

    @Subscribe(order = PostOrder.FIRST)
    fun onPlayerLogin(event: LoginEvent) {
        val player = event.player
        val property = player.gameProfile.properties.first()
        val podName = NeptunControllerProvider.api.podName()

        GlobalScope.launch(NeptunCoreProvider.api.minecraftDispatcher) {
            playerController.loadPlayer(player.uniqueId, player.username, property.value, property.signature, podName, "-/-")
        }
    }

    @Subscribe(order = PostOrder.LAST)
    fun onPlayerDisconnect(event: DisconnectEvent) {
        val player = event.player
        val playerAdapter = NeptunCoreProvider.api.getPlayerAdapter(Player::class.java)

        GlobalScope.launch(NeptunCoreProvider.api.minecraftDispatcher) {
            for (friendUniqueId in listOf(UUID.randomUUID())) {
                playerAdapter.sendGlobalMessage(friendUniqueId, neptunPlugin.key("friend.quitMessage"), listOf(Pair("name", player.username)))
            }

            playerController.unloadPlayer(player.uniqueId)
        }
    }

}