package world.neptuns.core.base.velocity

import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.github.shynixn.mccoroutine.velocity.registerSuspend
import com.github.shynixn.mccoroutine.velocity.velocityDispatcher
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.LangNamespace
import world.neptuns.core.base.api.util.NeptunPlugin
import world.neptuns.core.base.common.CoreBaseApiImpl
import world.neptuns.core.base.velocity.command.VelocityCommandExecutorAsync
import world.neptuns.core.base.velocity.command.impl.HubCommand
import world.neptuns.core.base.velocity.listener.PacketListener
import world.neptuns.core.base.velocity.listener.VelocityPlayerListener
import world.neptuns.core.base.velocity.player.VelocityPlayerAdapter
import java.nio.file.Path

@Plugin(
    id = "neptun_core_base",
    name = "neptun_core_base",
    version = "1.0.0",
    authors = ["TGamings"],
    dependencies = [Dependency(id = "neptun_network_controller")]
)
class NeptunVelocityPlugin @Inject constructor(
    suspendingPluginContainer: SuspendingPluginContainer,
    val proxyServer: ProxyServer,
    @DataDirectory val dataFolder: Path,
) : NeptunPlugin {

    override lateinit var namespace: LangNamespace

    init {
        suspendingPluginContainer.initialize(this)
    }

    @Subscribe
    suspend fun onProxyInitialization(event: ProxyInitializeEvent) {
        instance = this

        val pluginContainer = proxyServer.pluginManager.ensurePluginContainer(this)
        val coreBaseApi = CoreBaseApiImpl(pluginContainer.velocityDispatcher, this.dataFolder)

        NeptunCoreProvider.api = coreBaseApi

        coreBaseApi.languageController.generateLanguages(this::class.java)

        coreBaseApi.registerPlayerAdapter(VelocityPlayerAdapter(this.proxyServer, this))
        coreBaseApi.registerCommandExecutorClass(VelocityCommandExecutorAsync::class.java)

        this.namespace = LangNamespace.create("core.base.proxy", null)

        val packetListener = PacketListener(this.proxyServer)
        packetListener.listen()

        this.proxyServer.eventManager.registerSuspend(this, VelocityPlayerListener(coreBaseApi.playerService, coreBaseApi.languagePropertiesService))

        registerCommands(
            HubCommand(coreBaseApi.playerService),
            // LanguageCommand(coreBaseApi.languageColorService, coreBaseApi.languagePropertiesService, coreBaseApi.languageController)
        )
    }

    companion object {
        @JvmStatic
        lateinit var instance: NeptunVelocityPlugin
            private set
    }

}