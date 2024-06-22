package world.neptuns.core.base.bukkit.command

import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.player.extension.uuid
import java.util.*

class BukkitCommandSender(private val commandSender: CommandSender) : NeptunCommandSender {

    override val uuid: UUID
        get() = this.commandSender.uuid

    override fun isPlayer(): Boolean = this.commandSender is Player
    override fun hasPermission(permission: String): Boolean = this.commandSender.hasPermission(permission)

    override fun <T> castTo(clazz: Class<T>): T? {
        return if (isPlayer()) clazz.cast(commandSender) else null
    }

    override fun sendMessage(component: Component) {
        this.commandSender.sendMessage(component)
    }

}