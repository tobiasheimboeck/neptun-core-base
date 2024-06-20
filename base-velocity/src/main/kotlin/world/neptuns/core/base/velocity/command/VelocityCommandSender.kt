package world.neptuns.core.base.velocity.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import world.neptuns.core.base.api.command.NeptunCommandSender

@Suppress("KotlinConstantConditions")
class VelocityCommandSender(private val commandSource: CommandSource) : NeptunCommandSender {

    override fun isPlayer(): Boolean = this.commandSource is Player
    override fun hasPermission(permission: String): Boolean = this.commandSource.hasPermission(permission)

    override fun <T> castTo(clazz: Class<T>): T? {
        return if (isPlayer()) clazz.cast(commandSource) else null
    }
    override fun sendMessage(component: Component) {
        this.commandSource.sendMessage(component)
    }

}