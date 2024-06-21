package world.neptuns.core.base.velocity.command.impl.test

import com.velocitypowered.api.proxy.Player
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandPlatform
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.command.NeptunMainCommandExecutor
import world.neptuns.core.base.api.command.extension.generateSimpleSuggestions
import world.neptuns.core.base.api.command.extension.generateSuggestions
import world.neptuns.core.base.api.command.extension.sendUsageFormatted
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommandExecutor

@NeptunCommand(NeptunCommandPlatform.VELOCITY, name = "hello", title = "Test System")
class HelloMainCommand : NeptunMainCommandExecutor() {

    private val playerAdapter = NeptunCoreProvider.api.getPlayerAdapter()

    override suspend fun defaultExecute(sender: NeptunCommandSender) {
        val player = sender.castTo(Player::class.java) ?: return
        val properties = NeptunCoreProvider.api.languagePropertiesService.getProperties(player.uniqueId) ?: return

        sendUsageFormatted(sender, properties)
    }

    override suspend fun onDefaultTabComplete(sender: NeptunCommandSender, args: List<String>): List<String> {
        return buildList {
            addAll(generateSimpleSuggestions(args, 0) { add("world") })
            addAll(generateSuggestions(args, 1) { add("world") })
        }
    }

    override fun initSubCommands(subCommandExecutors: MutableList<NeptunSubCommandExecutor>) {
        subCommandExecutors.add(HelloWorldSubCommand())
        subCommandExecutors.add(HelloWorldInfoSubCommand())
        subCommandExecutors.add(HelloWorldCreateSubCommand())
    }

}