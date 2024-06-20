package world.neptuns.core.base.api.util

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

object DataTypeUtil {

    inline fun <reified T> parseDataType(audience: Audience, dataType: Class<T>, string: String, result: (T) -> Unit) {
        try {
            val value: T = when (dataType) {
                Int::class.java -> string.toInt() as T
                Long::class.java -> string.toLong() as T
                Double::class.java -> string.toDouble() as T
                Boolean::class.java -> string.toBoolean() as T
                String::class.java -> string as T
                else -> throw IllegalArgumentException("Unsupported type")
            }
            result(value)
        } catch (e: Exception) {
            val expectedDataTypeName = dataType.name
            audience.sendMessage(Component.text("Type $expectedDataTypeName required!", NamedTextColor.RED))
        }
    }

}