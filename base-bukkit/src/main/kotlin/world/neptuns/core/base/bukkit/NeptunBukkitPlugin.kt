package world.neptuns.core.base.bukkit

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import kotlinx.coroutines.withContext
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import world.neptuns.base.bukkit.api.NeptunCoreBukkitProvider
import world.neptuns.controller.api.NeptunControllerProvider
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.LangNamespace
import world.neptuns.core.base.api.util.NeptunPlugin
import world.neptuns.core.base.bukkit.api.CoreBaseBukkitApiImpl
import world.neptuns.core.base.bukkit.command.BukkitCommandExecutorAsync
import world.neptuns.core.base.bukkit.database.location.WorldLocationTable
import world.neptuns.core.base.bukkit.listener.BukkitPlayerListener
import world.neptuns.core.base.bukkit.listener.PacketListener
import world.neptuns.core.base.bukkit.player.BukkitPlayerAdapter
import world.neptuns.core.base.bukkit.repository.location.WorldLocationCache
import world.neptuns.core.base.bukkit.repository.location.WorldLocationRepository
import world.neptuns.core.base.common.CoreBaseApiImpl
import world.neptuns.streamline.api.NeptunStreamlineProvider


class NeptunBukkitPlugin : SuspendingJavaPlugin(), NeptunPlugin {

    override lateinit var namespace: LangNamespace

    override suspend fun onEnableAsync() {
        instance = this

        val coreBaseApi = CoreBaseApiImpl(minecraftDispatcher, this.dataFolder.toPath(), WorldLocationTable)
        NeptunCoreProvider.api = coreBaseApi

        coreBaseApi.languageController.generateLanguages(this::class.java)
        coreBaseApi.registerPlayerAdapter(BukkitPlayerAdapter(this))
        coreBaseApi.registerCommandExecutorClass(BukkitCommandExecutorAsync::class.java)

        NeptunStreamlineProvider.api.repositoryLoader.register(WorldLocationRepository(NeptunControllerProvider.api.redissonClient))
        NeptunStreamlineProvider.api.cacheLoader.register(WorldLocationCache())

        this.namespace = LangNamespace.create("core.lobby", null)

        val packetListener = PacketListener(this)
        packetListener.listen()

        val coreBaseBukkitApi = CoreBaseBukkitApiImpl(this)
        NeptunCoreBukkitProvider.api = coreBaseBukkitApi

        withContext(asyncDispatcher) {
            val locations = coreBaseBukkitApi.locationService.loadLocations().await()
            Bukkit.getConsoleSender().sendMessage(Component.text("Loaded ${locations.size} locations..."))
        }

        this.server.pluginManager.registerSuspendingEvents(BukkitPlayerListener(this, coreBaseApi.playerService, coreBaseApi.languagePropertiesService), this)
    }

    companion object {
        @JvmStatic
        lateinit var instance: NeptunBukkitPlugin
            private set
    }

}