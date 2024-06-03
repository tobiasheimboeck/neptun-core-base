package world.neptuns.core.base.api.utils

import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandExecutor
import world.neptuns.core.base.api.language.LangNamespace
import world.neptuns.core.base.api.language.LineKey

interface NeptunPluginAdapter {

    val namespace: LangNamespace

    fun registerCommands(vararg commandExecutors: NeptunCommandExecutor) {
        commandExecutors.forEach { registerCommand(it) }
    }

    fun registerCommand(commandExecutor: NeptunCommandExecutor) {
        NeptunCoreProvider.api.registerCommand(commandExecutor)
    }

    fun key(key: String): LineKey = LineKey.key(this.namespace, key)

}