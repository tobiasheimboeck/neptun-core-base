package world.neptuns.core.base.velocity.command.impl.test

import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommand
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommandExecutor

@NeptunSubCommand(length = 1, parts = "world")
class HelloWorldSubCommand : NeptunSubCommandExecutor {

    override suspend fun execute(sender: NeptunCommandSender, args: List<String>) {
        if (!sender.isPlayer()) return
        val player = sender.castTo(Player::class.java)

        player.sendMessage(Component.text("Hello world!", NamedTextColor.GREEN))
    }

    override suspend fun onTabComplete(sender: NeptunCommandSender, args: List<String>): List<String> = emptyList()

}