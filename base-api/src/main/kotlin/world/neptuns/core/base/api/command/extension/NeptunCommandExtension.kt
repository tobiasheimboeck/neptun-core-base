package world.neptuns.core.base.api.command.extension

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.command.NeptunMainCommandExecutor
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommand
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommandExecutor
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.core.base.api.player.extension.sendMessage
import world.neptuns.core.base.api.util.DataTypeUtil

suspend fun NeptunMainCommandExecutor.sendUsageFormatted(sender: NeptunCommandSender) {
    val properties = NeptunCoreProvider.api.languagePropertiesService.getProperties(sender.uuid) ?: return
    sendUsageFormatted(sender, properties)
}

fun NeptunMainCommandExecutor.sendUsageFormatted(sender: NeptunCommandSender, properties: LanguageProperties) {
    val language = NeptunCoreProvider.api.languageController.getLanguage(properties.langKey) ?: return
    val subCommandParts: List<String> = findSubCommandParts()

    val mainCommandAnnotation = this::class.java.getAnnotation(NeptunCommand::class.java) ?: return

    val titlePlaceholder = Placeholder.parsed("title", mainCommandAnnotation.title)
    val usage = language.usage(properties, subCommandParts, titlePlaceholder)

    sender.sendMessage(usage.first)
    usage.second.forEach(sender::sendMessage)
}

suspend inline fun <reified T> NeptunSubCommandExecutor.findArgument(audience: Audience, key: String, args: List<String>, dataType: Class<T>): T? {
    val subCommandAnnotation = this::class.java.getAnnotation(NeptunSubCommand::class.java) ?: return null
    val parts = subCommandAnnotation.parts.split(" ")

    val strippedList = parts.map { it.replace(Regex("[<>\\[\\]]"), "") }

    val placeholderIndex = strippedList.indexOf(key)
    val element = args.getOrNull(placeholderIndex) ?: return null

    var resultData: Pair<T?, String?> = Pair(null, null)
    DataTypeUtil.parseDataTypeNullable(dataType, element) { resultData = it }

    if (resultData.first == null) {
        audience.sendMessage(LineKey.coreKey("argument_type.invalid"), Placeholder.parsed("name", key), Placeholder.parsed("type", resultData.second!!))
        return null
    }

    return resultData.first
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
