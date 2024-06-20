package world.neptuns.core.base.velocity.command.impl.test

import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommand
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommandExecutor
import world.neptuns.core.base.api.extension.findArgument
import world.neptuns.core.base.api.extension.getArgument

@NeptunSubCommand(length = 3, parts = "world info {woldName}")
class HelloWorldInfoSubCommand : NeptunSubCommandExecutor {

    override suspend fun execute(sender: NeptunCommandSender, args: List<String>) {
        if (!sender.isPlayer()) return
        val player = sender.castTo(Player::class.java)

        val name = getArgument(sender, Int::class.java, args[1]) { integer ->

        }

        findArgument(sender, "worldName", Int::class.java) {

        }

        player.sendMessage(Component.text("World info $name"))
    }

    override suspend fun onTabComplete(sender: NeptunCommandSender, args: List<String>): List<String> = emptyList()

}