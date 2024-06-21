package world.neptuns.core.base.velocity.command.impl.test

import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.command.extension.findArgument
import world.neptuns.core.base.api.command.extension.generateSuggestions
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommand
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommandExecutor

@NeptunSubCommand(length = 4, parts = "world info [name] [age]")
class HelloWorldInfoSubCommand : NeptunSubCommandExecutor {

    override suspend fun execute(sender: NeptunCommandSender, args: List<String>) {
        val player = sender.castTo(Player::class.java) ?: return

        val name: String? = findArgument(player, "name", args, String::class.java)
        val age: Int? = findArgument(player, "age", args, Int::class.java)

        player.sendMessage(Component.text("$name is $age years old!", NamedTextColor.BLUE))
    }

    override suspend fun onTabComplete(sender: NeptunCommandSender, args: List<String>): List<String> {
        return generateSuggestions(args, 2, listOf(Pair(0, "world"))) {
            add("info")
        }
    }

}