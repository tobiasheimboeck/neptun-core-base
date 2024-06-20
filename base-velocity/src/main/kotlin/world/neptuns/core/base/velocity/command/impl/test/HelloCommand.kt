package world.neptuns.core.base.velocity.command.impl.test

import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandInitializer
import world.neptuns.core.base.api.command.NeptunCommandPlatform
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommandExecutor

@NeptunCommand(NeptunCommandPlatform.VELOCITY, name = "hello")
class HelloCommand : NeptunCommandInitializer() {

    override suspend fun defaultExecute(sender: NeptunCommandSender) {
        val player = sender.castTo(Player::class.java) ?: return

        player.sendMessage(Component.text("Use: /hello world"))
        player.sendMessage(Component.text("Use: /hello world info <name> <age>"))
        player.sendMessage(Component.text("Use: /hello world create <name> [permission]"))
    }

    override suspend fun onDefaultTabComplete(sender: NeptunCommandSender): List<String> = emptyList()

    override fun initSubCommands(subCommandExecutors: MutableList<NeptunSubCommandExecutor>) {
        subCommandExecutors.add(HelloWorldSubCommand())
        subCommandExecutors.add(HelloWorldInfoSubCommand())
        subCommandExecutors.add(HelloWorldCreateSubCommand())
    }

}