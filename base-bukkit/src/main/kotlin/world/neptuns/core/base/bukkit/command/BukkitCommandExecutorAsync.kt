package world.neptuns.core.base.bukkit.command

import com.github.shynixn.mccoroutine.bukkit.SuspendingCommandExecutor
import com.github.shynixn.mccoroutine.bukkit.SuspendingTabCompleter
import com.github.shynixn.mccoroutine.bukkit.setSuspendingExecutor
import com.github.shynixn.mccoroutine.bukkit.setSuspendingTabCompleter
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import world.neptuns.core.base.api.CoreBaseApi
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.language.LineKey

class BukkitCommandExecutorAsync(private val neptunCommand: NeptunCommand) : SuspendingCommandExecutor, SuspendingTabCompleter {

    private val neptunCommandInitializer = NeptunCoreProvider.api.commandController.getCommandInitializer(this.neptunCommand.name)!!

    init {
        val server = Bukkit.getServer()
        val pluginCommand: PluginCommand? = server.getPluginCommand(this.neptunCommand.name)
        pluginCommand?.setSuspendingExecutor(this)
        pluginCommand?.setSuspendingTabCompleter(this)
        pluginCommand?.setAliases(this.neptunCommand.aliases.toList())
        pluginCommand?.permission = this.neptunCommand.permission
        server.commandMap.register("", pluginCommand!!)
    }

    override suspend fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val neptunCommandSender = BukkitCommandSender(sender)

        if (checkPermission(neptunCommandSender, sender, this.neptunCommand.permission)) {
            val defaultLangProperties = CoreBaseApi.defaultLangProperties

            NeptunCoreProvider.api.languageController.getLanguage(defaultLangProperties.langKey)?.let {
                sender.sendMessage(it.line(defaultLangProperties, LineKey.key("core.base.no_permission")))
            }

            return false
        }

        if (args.isEmpty()){
            this.neptunCommandInitializer.defaultExecute(neptunCommandSender)
            return true
        }

        // subCommandParts removes the first element from a command: /perms group Admin info => group Admin info, because 'perms' is the main command!
        val subCommandParts = args.drop(0)
        val neptunSubCommandData = this.neptunCommandInitializer.findValidSubCommandData(subCommandParts)

        if (neptunSubCommandData == null) {
            this.neptunCommandInitializer.defaultExecute(neptunCommandSender)
            return true
        }

        val neptunSubCommandExecutor = neptunSubCommandData.first
        val neptunSubCommand = neptunSubCommandData.second

        if (checkPermission(neptunCommandSender, sender, neptunSubCommand.permission))
            return false

        neptunSubCommandExecutor.execute(neptunCommandSender, subCommandParts)
        return true
    }

    override suspend fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String> {
        val neptunCommandSender = BukkitCommandSender(sender)

        if (checkPermission(neptunCommandSender, sender, this.neptunCommand.permission)) return emptyList()

        if (args.isEmpty())
            return this.neptunCommandInitializer.onDefaultTabComplete(neptunCommandSender, args.toList())

        // subCommandParts removes the first element from a command: /perms group Admin info => group Admin info, because 'perms' is the main command!
        val subCommandParts = args.drop(0)
        val neptunSubCommandData = this.neptunCommandInitializer.findValidSubCommandData(subCommandParts)
            ?: return emptyList()

        val neptunSubCommandExecutor = neptunSubCommandData.first
        val neptunSubCommand = neptunSubCommandData.second

        if (checkPermission(neptunCommandSender, sender, neptunSubCommand.permission)) return emptyList()

        return neptunSubCommandExecutor.onTabComplete(neptunCommandSender, args.toList())
    }

    private fun checkPermission(neptunCommandSender: NeptunCommandSender, sender: CommandSender, permission: String): Boolean {
        return neptunCommandSender.isPlayer() && (permission != "") && (sender.hasPermission(permission))
    }

}