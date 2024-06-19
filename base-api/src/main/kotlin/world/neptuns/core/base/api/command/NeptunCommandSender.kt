package world.neptuns.core.base.api.command

import net.kyori.adventure.text.Component

interface NeptunCommandSender {

    fun isPlayer(): Boolean

    fun hasPermission(permission: String): Boolean

    fun <T> castTo(clazz: Class<T>): T

    fun sendMessage(component: Component)

}