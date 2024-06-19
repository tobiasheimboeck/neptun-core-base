package world.neptuns.core.base.bukkit

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import world.neptuns.base.bukkit.api.NeptunCoreBukkitProvider
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.LangNamespace
import world.neptuns.core.base.api.utils.NeptunPlugin
import world.neptuns.core.base.bukkit.api.CoreBaseBukkitApiImpl
import world.neptuns.core.base.bukkit.command.BukkitCommandExecutorAsync
import world.neptuns.core.base.bukkit.listener.BukkitPlayerListener
import world.neptuns.core.base.bukkit.listener.PacketListener
import world.neptuns.core.base.bukkit.player.BukkitPlayerAdapter
import world.neptuns.core.base.common.CoreBaseApiImpl


class NeptunBukkitPlugin : SuspendingJavaPlugin(), NeptunPlugin {

    override lateinit var namespace: LangNamespace

    override suspend fun onEnableAsync() {
        val coreBaseApi = CoreBaseApiImpl(minecraftDispatcher, this.dataFolder.toPath())
        NeptunCoreProvider.api = coreBaseApi

        coreBaseApi.languageController.generateLanguages(this::class.java)
        coreBaseApi.registerPlayerAdapter(BukkitPlayerAdapter(this))
        coreBaseApi.registerCommandExecutorClass(BukkitCommandExecutorAsync::class.java)

        this.namespace = LangNamespace.create("core.lobby", null)

        val packetListener = PacketListener(this)
        packetListener.listen()

        val coreBaseBukkitApi = CoreBaseBukkitApiImpl(this)
        NeptunCoreBukkitProvider.api = coreBaseBukkitApi

        this.server.pluginManager.registerSuspendingEvents(BukkitPlayerListener(this, coreBaseApi.playerService, coreBaseApi.languagePropertiesService), this)
    }

}