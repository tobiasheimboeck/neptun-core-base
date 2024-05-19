package world.neptuns.core.base.velocity

import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.github.shynixn.mccoroutine.velocity.velocityDispatcher
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import world.neptuns.core.base.common.CoreBaseApiImpl
import world.neptuns.core.base.velocity.command.VelocityCommandExecutorAsync
import world.neptuns.core.base.velocity.player.VelocityPlayerAdapter
import java.nio.file.Path

@Plugin(
    id = "neptun_core_base",
    name = "neptun_core_base"
)
class NeptunVelocityPlugin @Inject constructor(
    private val suspendingPluginContainer: SuspendingPluginContainer,
    private val proxyServer: ProxyServer,
    @DataDirectory private val dataFolder: Path
) {

    init {
        suspendingPluginContainer.initialize(this)
    }

    @Subscribe
    suspend fun onProxyInitialization(event: ProxyInitializeEvent) {
        instance = this

        val coreBaseApi = CoreBaseApiImpl(suspendingPluginContainer.pluginContainer.velocityDispatcher, this.dataFolder)
        coreBaseApi.languageController.generateLanguages(this::class.java)
        coreBaseApi.registerPlayerAdapter(VelocityPlayerAdapter())
        coreBaseApi.registerCommandExecutorClass(VelocityCommandExecutorAsync::class.java)
    }

    companion object {
        @JvmStatic
        lateinit var instance: NeptunVelocityPlugin
            private set
    }

}