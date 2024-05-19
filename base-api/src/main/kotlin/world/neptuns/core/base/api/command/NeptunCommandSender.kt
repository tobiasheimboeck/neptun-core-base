package world.neptuns.core.base.api.command

interface NeptunCommandSender {

    fun isPlayer(): Boolean

    fun hasPermission(permission: String): Boolean

    fun <T> castTo(clazz: Class<T>): T

}