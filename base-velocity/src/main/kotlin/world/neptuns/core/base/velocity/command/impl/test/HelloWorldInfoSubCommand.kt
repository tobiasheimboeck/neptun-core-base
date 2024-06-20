package world.neptuns.core.base.velocity.command.impl.test

import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.command.extension.findArgument
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommand
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommandExecutor

@NeptunSubCommand(length = 3, parts = "world info {message}")
class HelloWorldInfoSubCommand : NeptunSubCommandExecutor {

    override suspend fun execute(sender: NeptunCommandSender, args: List<String>) {
        val player = sender.castTo(Player::class.java) ?: return

        val message: String? = findArgument(sender, "message", String::class.java)

        player.sendMessage(Component.text("Hello World info $message"))
    }

    override suspend fun onTabComplete(sender: NeptunCommandSender, args: List<String>): List<String> = emptyList()

}