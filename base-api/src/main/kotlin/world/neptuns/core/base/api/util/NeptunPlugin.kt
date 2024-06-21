package world.neptuns.core.base.api.util

import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunMainCommandExecutor
import world.neptuns.core.base.api.language.LangNamespace
import world.neptuns.core.base.api.language.LineKey

interface NeptunPlugin {

    val namespace: LangNamespace

    fun registerCommands(vararg initializers: NeptunMainCommandExecutor) {
        initializers.forEach { registerCommand(it) }
    }

    fun registerCommand(initializer: NeptunMainCommandExecutor) {
        NeptunCoreProvider.api.registerInitializer(initializer)
    }

    fun key(key: String): LineKey = LineKey.key(this.namespace, key)

}