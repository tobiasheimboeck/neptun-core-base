package world.neptuns.core.base.bukkit.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import world.neptuns.core.base.api.command.NeptunCommandSender

class BukkitCommandSender(private val commandSender: CommandSender) : NeptunCommandSender {

    override fun isPlayer(): Boolean = this.commandSender is Player
    override fun hasPermission(permission: String): Boolean = this.commandSender.hasPermission(permission)

    override fun <T> castTo(clazz: Class<T>): T = clazz.cast(commandSender)

}