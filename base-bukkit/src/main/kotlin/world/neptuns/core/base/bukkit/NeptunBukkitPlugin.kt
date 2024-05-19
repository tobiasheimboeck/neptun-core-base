package world.neptuns.core.base.bukkit

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.bukkit.command.BukkitCommandExecutorAsync
import world.neptuns.core.base.bukkit.player.BukkitPlayerAdapter
import world.neptuns.core.base.common.CoreBaseApiImpl

class NeptunBukkitPlugin : SuspendingJavaPlugin() {

    override suspend fun onEnableAsync() {
        val coreBaseApi = CoreBaseApiImpl(minecraftDispatcher, this.dataFolder.toPath())
        coreBaseApi.languageController.generateLanguages(this::class.java)
        coreBaseApi.registerPlayerAdapter(BukkitPlayerAdapter())
        coreBaseApi.registerCommandExecutorClass(BukkitCommandExecutorAsync::class.java)

        NeptunCoreProvider.api = coreBaseApi
    }

}