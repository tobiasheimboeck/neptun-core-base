package world.neptuns.core.base.bukkit

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.LangNamespace
import world.neptuns.core.base.api.utils.NeptunPluginAdapter
import world.neptuns.core.base.bukkit.command.BukkitCommandExecutorAsync
import world.neptuns.core.base.bukkit.player.BukkitPlayerAdapter
import world.neptuns.core.base.common.CoreBaseApiImpl

class NeptunBukkitPlugin : SuspendingJavaPlugin(), NeptunPluginAdapter {

    override val namespace: LangNamespace = LangNamespace.create("core.lobby", "Lobby")

    override suspend fun onEnableAsync() {
        val coreBaseApi = CoreBaseApiImpl(minecraftDispatcher, this.dataFolder.toPath())
        coreBaseApi.languageController.generateLanguages(this::class.java)
        coreBaseApi.registerPlayerAdapter(BukkitPlayerAdapter())
        coreBaseApi.registerCommandExecutorClass(BukkitCommandExecutorAsync::class.java)

        NeptunCoreProvider.api = coreBaseApi
    }

}