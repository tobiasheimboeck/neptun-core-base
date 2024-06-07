package world.neptuns.base.bukkit.api

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.LangNamespace
import world.neptuns.core.base.api.utils.NeptunPlugin

abstract class NeptunBukkitPlugin(override val namespace: LangNamespace, val fallbackHotbarKey: String?, private val loadLanguageFile: Boolean) : SuspendingJavaPlugin(), NeptunPlugin {

    override fun onLoad() {
        if (!this.loadLanguageFile) return
        NeptunCoreProvider.api.languageController.addContentToLanguage(this::class.java)
    }

}