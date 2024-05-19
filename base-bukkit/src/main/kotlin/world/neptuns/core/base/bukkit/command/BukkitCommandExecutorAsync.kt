package world.neptuns.core.base.bukkit.command

import com.github.shynixn.mccoroutine.bukkit.SuspendingCommandExecutor
import com.github.shynixn.mccoroutine.bukkit.SuspendingTabCompleter
import com.github.shynixn.mccoroutine.bukkit.setSuspendingExecutor
import com.github.shynixn.mccoroutine.bukkit.setSuspendingTabCompleter
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandSender

class BukkitCommandExecutorAsync(private val neptunCommand: NeptunCommand) : SuspendingCommandExecutor, SuspendingTabCompleter {

    private val neptunCommandExecutor = NeptunCoreProvider.api.commandController.getCommandExecutor(this.neptunCommand.name)!!

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

        if (checkPermission(neptunCommandSender, sender)) {
            //TODO: Send no permission warning
            return false
        }

        this.neptunCommandExecutor.execute(neptunCommandSender, args.toList())
        return true
    }

    override suspend fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String>? {
        val neptunCommandSender = BukkitCommandSender(sender)

        if (checkPermission(neptunCommandSender, sender)) return emptyList()
        return this.neptunCommandExecutor.onTabComplete(neptunCommandSender, args.toList())
    }

    private fun checkPermission(neptunCommandSender: NeptunCommandSender, sender: CommandSender): Boolean {
        return neptunCommandSender.isPlayer() && (this.neptunCommand.permission != "") && (sender.hasPermission(this.neptunCommand.permission))
    }

}