package world.neptuns.core.base.api.util

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

object DataTypeUtil {

    inline fun <reified T> parseDataTypeNullable(dataType: Class<T>, string: String, result: (Pair<T?, String?>) -> Unit) {
        try {
            val value: T = when (dataType) {
                Int::class.java -> string.toInt() as T
                Long::class.java -> string.toLong() as T
                Double::class.java -> string.toDouble() as T
                Boolean::class.java -> string.toBoolean() as T
                String::class.java -> string as T
                TextColor::class.java -> {
                    if (string.matches(Regex("^#[0-9a-fA-F]{6}$"))) {
                        TextColor.fromHexString(string) as T
                    } else {
                        throw IllegalArgumentException("Invalid Hex color format")
                    }
                }
                else -> throw IllegalArgumentException("Unsupported type")
            }

            result(value to null)
        } catch (e: Exception) {
            val textColorClass = TextColor::class.java
            val dataTypeName = if (dataType.simpleName == textColorClass.simpleName) "hexString #[0-9a-fA-F]" else dataType.simpleName
            result(null to dataTypeName)
        }
    }

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