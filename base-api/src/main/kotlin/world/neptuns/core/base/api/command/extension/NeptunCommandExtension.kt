package world.neptuns.core.base.api.command.extension

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.command.NeptunMainCommandExecutor
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommand
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommandExecutor
import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.core.base.api.util.DataTypeUtil

fun NeptunMainCommandExecutor.sendUsageFormatted(sender: NeptunCommandSender, properties: LanguageProperties) {
    val language = NeptunCoreProvider.api.languageController.getLanguage(properties.langKey) ?: return
    val subCommandParts: List<String> = findSubCommandParts()

    val mainCommandAnnotation = this::class.java.getAnnotation(NeptunCommand::class.java) ?: return

    val titlePlaceholder = Placeholder.parsed("title", mainCommandAnnotation.title)
    val usage = language.usage(properties, subCommandParts, titlePlaceholder)

    sender.sendMessage(usage.first)
    usage.second.forEach(sender::sendMessage)
}

inline fun <reified T> NeptunSubCommandExecutor.findArgument(audience: Audience, key: String, args: List<String>, dataType: Class<T>): T? {
    var resultData: T? = null

    findArgument(audience, key, args, dataType) { resultData = it }

    return resultData
}

inline fun <reified T> NeptunSubCommandExecutor.findArgument(audience: Audience, key: String, args: List<String>, dataType: Class<T>, result: (T?) -> Unit) {
    val subCommandAnnotation = this::class.java.getAnnotation(NeptunSubCommand::class.java) ?: return
    val parts = subCommandAnnotation.parts.split(" ")

    val strippedList = parts.map { it.replace(Regex("[<>\\[\\]]"), "") }

    val placeholderIndex = strippedList.indexOf(key)
    val element = args.getOrNull(placeholderIndex)

    if (element == null) {
        result(null)
        return
    }

    DataTypeUtil.parseDataType(audience, dataType, element, result)
}
