package world.neptuns.core.base.api.command.extension

import net.kyori.adventure.audience.Audience
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommand
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommandExecutor
import world.neptuns.core.base.api.util.DataTypeUtil

inline fun <reified T> NeptunSubCommandExecutor.findArgument(audience: Audience, key: String, args: List<String>, dataType: Class<T>): T? {
    var resultData: T? = null

    findArgument(audience, key, args, dataType) { resultData = it }

    return resultData
}

inline fun <reified T> NeptunSubCommandExecutor.findArgument(audience: Audience, key: String, args: List<String>, dataType: Class<T>, result: (T?) -> Unit) {
    val subCommandAnnotation = this::class.java.getAnnotation(NeptunSubCommand::class.java) ?: return
    val parts = subCommandAnnotation.parts.split(" ")

    val placeholderIndex = parts.indexOf("{$key}")
    val element = args.getOrNull(placeholderIndex)

    if (element == null) {
        result(null)
        return
    }

    DataTypeUtil.parseDataType(audience, dataType, element, result)
}
