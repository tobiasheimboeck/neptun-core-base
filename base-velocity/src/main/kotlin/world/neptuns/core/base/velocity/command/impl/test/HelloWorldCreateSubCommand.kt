package world.neptuns.core.base.velocity.command.impl.test

import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.command.extension.findArgument
import world.neptuns.core.base.api.command.extension.generateSuggestions
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommand
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommandExecutor

@NeptunSubCommand(minLength = 3, maxLength = 4, parts = "world create {name} {permission}")
class HelloWorldCreateSubCommand : NeptunSubCommandExecutor {

    override suspend fun execute(sender: NeptunCommandSender, args: List<String>) {
        val player = sender.castTo(Player::class.java) ?: return

        val name = findArgument(player, "name", args, String::class.java)
        val permission = findArgument(player, "permission", args, String::class.java) ?: "-/-"

        player.sendMessage(Component.text("Create world $name with permission $permission"))
    }

    override suspend fun onTabComplete(sender: NeptunCommandSender, args: List<String>): List<String> {
        return generateSuggestions(args, 2, listOf(Pair(0, "world"))) {
            add("create")
        }
    }

}