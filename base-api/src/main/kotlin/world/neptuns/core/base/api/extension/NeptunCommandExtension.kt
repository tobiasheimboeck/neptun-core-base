package world.neptuns.core.base.api.extension

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import world.neptuns.core.base.api.command.NeptunCommandSender

inline fun <reified T> getArgument(sender: NeptunCommandSender, dataType: Class<T>, string: String, result: (T) -> Unit) {
    if (string is T) {
        result(string as T)
        return
    }

    val expectedDataTypeName = dataType.name
    sender.sendMessage(Component.text("Type $expectedDataTypeName required!", NamedTextColor.RED))
}