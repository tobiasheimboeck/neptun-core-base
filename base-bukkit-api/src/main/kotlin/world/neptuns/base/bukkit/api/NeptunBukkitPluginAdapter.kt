package world.neptuns.base.bukkit.api

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunMainCommandExecutor
import world.neptuns.core.base.api.language.LangNamespace
import world.neptuns.core.base.api.util.NeptunPlugin

abstract class NeptunBukkitPluginAdapter(val fallbackHotbarKey: String?, private val loadLanguageFile: Boolean) : SuspendingJavaPlugin(), NeptunPlugin {

    override lateinit var namespace: LangNamespace

    override fun onLoad() {
        if (!this.loadLanguageFile) return
        NeptunCoreProvider.api.languageController.addContentToLanguage(this::class.java)

        this.namespace = initNamespace()
    }

    protected fun registerCommand(initializer: NeptunMainCommandExecutor, plugin: SuspendingJavaPlugin) {
        val neptunCommand = NeptunCoreProvider.api.commandController.registerCommand(initializer)
        NeptunCoreProvider.api.commandExecutorClass.getDeclaredConstructor(NeptunCommand::class.java, SuspendingJavaPlugin::class.java).newInstance(neptunCommand, plugin)
    }

    abstract fun initNamespace(): LangNamespace

}