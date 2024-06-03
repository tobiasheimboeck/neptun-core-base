package world.neptuns.core.base.velocity.command.impl

import com.velocitypowered.api.proxy.Player
import world.neptuns.controller.api.NeptunControllerProvider
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandExecutor
import world.neptuns.core.base.api.command.NeptunCommandPlatform
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.player.NeptunPlayerController

@NeptunCommand(NeptunCommandPlatform.VELOCITY, name = "hub", aliases = ["lobby", "l"])
class HubCommand(
    private val playerController: NeptunPlayerController
) : NeptunCommandExecutor {

    override suspend fun execute(sender: NeptunCommandSender, args: List<String>) {
        if (!sender.isPlayer()) return

        val player = sender.castTo(Player::class.java)
        val playerAdapter = NeptunCoreProvider.api.getPlayerAdapter(Player::class.java)

        val onlinePlayer = this.playerController.getOnlinePlayer(player.uniqueId) ?: return

        val currentServiceName = onlinePlayer.currentServiceName
        val neptunService = NeptunControllerProvider.api.serviceController.getService(currentServiceName) ?: return

        if (neptunService.type.isHubService()) {
            playerAdapter.sendMessage(player, "hub.already_connected")
            return
        }

        playerAdapter.sendMessage(player, "hub.send_player")
        playerAdapter.transferPlayerToLobby(player.uniqueId)
    }

    override fun sendUsage(sender: NeptunCommandSender) {

    }

    override suspend fun onTabComplete(sender: NeptunCommandSender, args: List<String>): List<String> {
        return emptyList()
    }

}