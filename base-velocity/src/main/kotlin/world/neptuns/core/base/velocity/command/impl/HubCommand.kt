package world.neptuns.core.base.velocity.command.impl

import com.velocitypowered.api.proxy.Player
import world.neptuns.controller.api.NeptunControllerProvider
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandPlatform
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.command.NeptunMainCommandExecutor
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommandExecutor
import world.neptuns.core.base.api.player.NeptunPlayerService

@NeptunCommand(NeptunCommandPlatform.VELOCITY, name = "hub", permission = "neptun.hub", title = "Server System", aliases = ["lobby", "l"])
class HubCommand(private val playerService: NeptunPlayerService) : NeptunMainCommandExecutor() {

    override suspend fun defaultExecute(sender: NeptunCommandSender) {
        val player = sender.castTo(Player::class.java) ?: return

        val playerAdapter = NeptunCoreProvider.api.getPlayerAdapter()

        val onlinePlayer = this.playerService.getOnlinePlayer(player.uniqueId) ?: return

        val currentServiceName = onlinePlayer.currentServiceName
        val neptunService = NeptunControllerProvider.api.serviceController.getService(currentServiceName) ?: return

        if (neptunService.type.isHubService()) {
            playerAdapter.sendMessage(player, "hub.already_connected")
            return
        }

        playerAdapter.sendMessage(player, "hub.send_player")
        playerAdapter.transferPlayerToLobby(player.uniqueId)
    }

    override suspend fun onDefaultTabComplete(sender: NeptunCommandSender, args: List<String>): List<String> =
        emptyList()

    override fun initSubCommands(subCommandExecutors: MutableList<NeptunSubCommandExecutor>) {

    }

}