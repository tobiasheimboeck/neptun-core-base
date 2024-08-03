package world.neptuns.core.base.bukkit.command

import com.github.shynixn.mccoroutine.bukkit.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import world.neptuns.core.base.api.CoreBaseApi
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommand
import world.neptuns.core.base.api.language.LineKey

class BukkitCommandExecutorAsync(private val neptunCommand: NeptunCommand, private val plugin: SuspendingJavaPlugin) : SuspendingCommandExecutor, SuspendingTabCompleter {

    private val mainCommandExecutor = NeptunCoreProvider.api.commandController.getCommandInitializer(this.neptunCommand.name)!!

    init {
        val pluginCommand = this.plugin.getCommand(this.neptunCommand.name)
            ?: throw NullPointerException("Command ${this.neptunCommand.name} is not registered in the plugin.yml of plugin ${this.plugin.name}")

        pluginCommand.setSuspendingExecutor(this)
        pluginCommand.setSuspendingTabCompleter(this)
        pluginCommand.setAliases(this.neptunCommand.aliases.toList())
        pluginCommand.permission = this.neptunCommand.permission

        this.plugin.server.commandMap.register("", pluginCommand)
    }

    override suspend fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val neptunCommandSender = BukkitCommandSender(sender)

        if (!checkPermission(neptunCommandSender, sender, this.neptunCommand.permission)) {
            val defaultLangProperties = CoreBaseApi.defaultLangProperties

            NeptunCoreProvider.api.languageController.getLanguage(defaultLangProperties.langKey)?.let {
                sender.sendMessage(it.line(defaultLangProperties, LineKey.key("core.base.no_permission")))
            }

            return false
        }

        val arguments = args.toList()

        if (arguments.isEmpty()) {
            this.mainCommandExecutor.defaultExecute(neptunCommandSender)
            return true
        }

        // subCommandParts removes the first element from a command: /perms group Admin info => group Admin info, because 'perms' is the main command!
        val subCommandParts = arguments.drop(0)
        val neptunSubCommandData = this.mainCommandExecutor.findValidSubCommandData(subCommandParts)

        if (neptunSubCommandData == null) {
            this.mainCommandExecutor.defaultExecute(neptunCommandSender)
            return true
        }

        val neptunSubCommandExecutor = neptunSubCommandData.first
        val neptunSubCommand = neptunSubCommandData.second

        if (!checkPermission(neptunCommandSender, sender, neptunSubCommand.permission))
            return false

        neptunSubCommandExecutor.execute(neptunCommandSender, subCommandParts)
        return true
    }

    override suspend fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String> {
        val neptunCommandSender = BukkitCommandSender(sender)
        if (!checkPermission(neptunCommandSender, sender, this.neptunCommand.permission)) return emptyList()

        val arguments = args.toList()

        if (arguments.isEmpty() || arguments.size == 1)
            return this.mainCommandExecutor.onDefaultTabComplete(neptunCommandSender, arguments.toList())

        // subCommandParts removes the first element from a command: /perms group Admin info => group Admin info, because 'perms' is the main command!
        val subCommandArgs = arguments.drop(0)

        val suggestions = mutableListOf<String>()

        for (subCommandExecutor in this.mainCommandExecutor.subCommandExecutors) {
            val subCommandAnnotation = subCommandExecutor::class.java.getAnnotation(NeptunSubCommand::class.java)!!
            if (!checkPermission(neptunCommandSender, sender, subCommandAnnotation.permission)) return emptyList()

            suggestions.addAll(subCommandExecutor.onTabComplete(neptunCommandSender, subCommandArgs))
        }

        return suggestions
    }

    private fun checkPermission(neptunCommandSender: NeptunCommandSender, sender: CommandSender, permission: String): Boolean {
        return neptunCommandSender.isPlayer() && (permission == "" || sender.hasPermission(permission))
    }

}