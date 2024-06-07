package world.neptuns.base.bukkit.api.plugin

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import world.neptuns.core.base.api.language.LangNamespace
import world.neptuns.core.base.api.utils.NeptunPlugin

abstract class NeptunBukkitPlugin(override val namespace: LangNamespace, val fallbackHotbarKey: String?) : SuspendingJavaPlugin(), NeptunPlugin