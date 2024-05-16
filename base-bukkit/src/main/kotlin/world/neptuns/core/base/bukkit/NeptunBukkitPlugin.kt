package world.neptuns.core.base.bukkit

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.common.CoreBaseApiImpl

class NeptunBukkitPlugin : SuspendingJavaPlugin() {

    override suspend fun onEnableAsync() {
        val coreBaseApi = CoreBaseApiImpl(minecraftDispatcher)
        coreBaseApi.getLanguageController().generateLanguages(this::class.java)

        NeptunCoreProvider.api = coreBaseApi
    }

}