package world.neptuns.core.base.velocity.command

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.player.extension.getLanguage
import world.neptuns.core.base.api.player.extension.getLanguageProperties
import world.neptuns.core.base.api.player.extension.uuid
import java.util.*

class VelocityCommandSender(private val commandSource: CommandSource) : NeptunCommandSender {

    override val uuid: UUID
        get() = this.commandSource.uuid

    override fun isPlayer(): Boolean = this.commandSource is Player
    override fun hasPermission(permission: String): Boolean = this.commandSource.hasPermission(permission)

    override fun <T> castTo(clazz: Class<T>): T? {
        return if (isPlayer()) clazz.cast(commandSource) else null
    }
    override fun sendMessage(component: Component) {
        this.commandSource.sendMessage(component)
    }

}