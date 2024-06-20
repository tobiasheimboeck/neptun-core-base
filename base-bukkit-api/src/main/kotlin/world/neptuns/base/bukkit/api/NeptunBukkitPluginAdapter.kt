package world.neptuns.base.bukkit.api

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.LangNamespace
import world.neptuns.core.base.api.util.NeptunPlugin

abstract class NeptunBukkitPluginAdapter(val fallbackHotbarKey: String?, private val loadLanguageFile: Boolean) : SuspendingJavaPlugin(), NeptunPlugin {

    override lateinit var namespace: LangNamespace

    override fun onLoad() {
        if (!this.loadLanguageFile) return
        NeptunCoreProvider.api.languageController.addContentToLanguage(this::class.java)

        this.namespace = initNamespace()
    }

    abstract fun initNamespace(): LangNamespace

}