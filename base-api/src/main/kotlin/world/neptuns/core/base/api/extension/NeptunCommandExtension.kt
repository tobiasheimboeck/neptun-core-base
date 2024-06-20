package world.neptuns.core.base.api.extension

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommand
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommandExecutor

inline fun <reified T> getArgument(sender: NeptunCommandSender, dataType: Class<T>, string: String, result: (T) -> Unit) {
    if (string is T) {
        result(string as T)
        return
    }

    val expectedDataTypeName = dataType.name
    sender.sendMessage(Component.text("Type $expectedDataTypeName required!", NamedTextColor.RED))
}

inline fun <reified T> NeptunSubCommandExecutor.findArgument(sender: NeptunCommandSender, key: String, dataType: Class<T>, result: (T) -> Unit) {
    val subCommandAnnotation = this::class.java.getAnnotation(NeptunSubCommand::class.java) ?: return
    val parts = subCommandAnnotation.parts

    // world info <woldName>

    val placeholderIndex = parts.indexOf("{$key}")
    getArgument(sender, dataType, parts.split(" ")[placeholderIndex], result)
}