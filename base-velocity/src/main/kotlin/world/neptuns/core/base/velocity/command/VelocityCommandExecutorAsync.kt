package world.neptuns.core.base.velocity.command

import com.github.shynixn.mccoroutine.velocity.SuspendingSimpleCommand
import com.github.shynixn.mccoroutine.velocity.registerSuspend
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import world.neptuns.core.base.api.CoreBaseApi
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommand
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.velocity.NeptunVelocityPlugin

class VelocityCommandExecutorAsync(private val neptunCommand: NeptunCommand) : SuspendingSimpleCommand {

    private val neptunCommandInitializer = NeptunCoreProvider.api.commandController.getCommandInitializer(this.neptunCommand.name)!!

    init {
        val plugin = NeptunVelocityPlugin.instance
        val commandManager = plugin.proxyServer.commandManager
        commandManager.registerSuspend(commandManager.metaBuilder(this.neptunCommand.name).aliases(*this.neptunCommand.aliases).build(), this, plugin)
    }

    override suspend fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val neptunCommandSender = VelocityCommandSender(sender)

        if (checkPermission(neptunCommandSender, sender, this.neptunCommand.permission)) {
            val defaultLangProperties = CoreBaseApi.defaultLangProperties

            NeptunCoreProvider.api.languageController.getLanguage(defaultLangProperties.langKey)?.let {
                sender.sendMessage(it.line(defaultLangProperties, LineKey.key("core.base.no_permission")))
            }

            return
        }

        val args = invocation.arguments().toList()

        if (args.isEmpty()) {
            this.neptunCommandInitializer.defaultExecute(neptunCommandSender)
            return
        }

        // subCommandParts removes the first element from a command: /perms group Admin info => group Admin info, because 'perms' is the main command!
        val subCommandArgs = args.drop(0)

        val neptunSubCommandData = this.neptunCommandInitializer.findValidSubCommandData(subCommandArgs)

        if (neptunSubCommandData == null) {
            this.neptunCommandInitializer.defaultExecute(neptunCommandSender)
            return
        }

        val neptunSubCommandExecutor = neptunSubCommandData.first
        val neptunSubCommand = neptunSubCommandData.second

        if (checkPermission(neptunCommandSender, sender, neptunSubCommand.permission))
            return

        neptunSubCommandExecutor.execute(neptunCommandSender, subCommandArgs)
        return
    }

    override suspend fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val sender = invocation.source()
        val neptunCommandSender = VelocityCommandSender(sender)

        if (checkPermission(neptunCommandSender, sender, this.neptunCommand.permission)) return emptyList()

        val args = invocation.arguments().toList()

        if (args.isEmpty() || args.size == 1)
            return this.neptunCommandInitializer.onDefaultTabComplete(neptunCommandSender, args)

        // subCommandParts removes the first element from a command: /perms group Admin info => group Admin info, because 'perms' is the main command!
        val subCommandArgs = args.drop(0)

        val suggestions = mutableListOf<String>()

        for (subCommandExecutor in this.neptunCommandInitializer.subCommandExecutors) {
            val subCommandAnnotation = subCommandExecutor::class.java.getAnnotation(NeptunSubCommand::class.java)!!
            if (checkPermission(neptunCommandSender, sender, subCommandAnnotation.permission)) return emptyList()

            suggestions.addAll(subCommandExecutor.onTabComplete(neptunCommandSender, subCommandArgs))
        }

        return suggestions
    }

    private fun checkPermission(neptunCommandSender: NeptunCommandSender, sender: CommandSource, permission: String): Boolean {
        return neptunCommandSender.isPlayer() && (permission != "") && (sender.hasPermission(permission))
    }

}