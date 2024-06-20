package world.neptuns.core.base.api.util

import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommandInitializer
import world.neptuns.core.base.api.language.LangNamespace
import world.neptuns.core.base.api.language.LineKey

interface NeptunPlugin {

    val namespace: LangNamespace

    fun registerCommands(vararg initializers: NeptunCommandInitializer) {
        initializers.forEach { registerCommand(it) }
    }

    fun registerCommand(initializer: NeptunCommandInitializer) {
        NeptunCoreProvider.api.registerInitializer(initializer)
    }

    fun key(key: String): LineKey = LineKey.key(this.namespace, key)

}