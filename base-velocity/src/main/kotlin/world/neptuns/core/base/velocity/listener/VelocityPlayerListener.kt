package world.neptuns.core.base.velocity.listener

import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.ResultedEvent
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.event.proxy.ProxyPingEvent
import com.velocitypowered.api.network.ProtocolVersion
import com.velocitypowered.api.proxy.server.ServerPing
import world.neptuns.controller.api.NeptunControllerProvider
import world.neptuns.core.base.api.CoreBaseApi
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.properties.LanguagePropertiesService
import world.neptuns.core.base.api.player.NeptunPlayerService
import world.neptuns.core.base.common.repository.player.OnlinePlayerNameRepository
import world.neptuns.streamline.api.NeptunStreamlineProvider


class VelocityPlayerListener(
    private val playerService: NeptunPlayerService,
    private val languagePropertiesService: LanguagePropertiesService,
) {

    private val onlineNameNameRepo = NeptunStreamlineProvider.api.repositoryLoader.get(OnlinePlayerNameRepository::class.java)!!

    @Subscribe
    suspend fun onServerListPing(event: ProxyPingEvent) {
        val language = NeptunCoreProvider.api.languageController.getLanguage(LangKey.key("en", "US")) ?: return
        val serviceGroup = NeptunControllerProvider.api.serviceGroupController.getServiceGroup("proxy") ?: return

        event.ping = event.ping.asBuilder()
            .onlinePlayers(NeptunControllerProvider.api.serviceController.getGlobalPlayerAmount())
            .maximumPlayers(serviceGroup.maxOnlineServiceCount)
            .description(language.line(CoreBaseApi.defaultLangProperties, LineKey.key("core.base.proxy.motd.normal")))
            .version(ServerPing.Version(0, "⚒"))
            .build()
    }

    @Subscribe(order = PostOrder.EARLY)
    suspend fun onPlayerLogin(event: LoginEvent) {
        val player = event.player
        val property = player.gameProfile.properties.first()
        val podName = NeptunControllerProvider.api.podName()

        val defaultLanguage = NeptunCoreProvider.api.languageController.getLanguage(LangKey.key("en", "US")) ?: return

        if (player.protocolVersion != ProtocolVersion.MINECRAFT_1_21) {
            player.disconnect(defaultLanguage.line(CoreBaseApi.defaultLangProperties, LineKey.key("core.base.proxy.protocol.wrong_version")))
            return
        }

        val serverGroup = NeptunControllerProvider.api.serviceGroupController.getServiceGroup("proxy") ?: return

        if (serverGroup.inMaintenance && !player.hasPermission("core.maintenance.bypass")) {
            event.result = ResultedEvent.ComponentResult.denied(defaultLanguage.line(CoreBaseApi.defaultLangProperties, LineKey.key("core.base.proxy.maintenance.forbidden")))
            return
        }

        if (!this.onlineNameNameRepo.contains(player.username).await()) {
            @Suppress("DeferredResultUnused")
            this.onlineNameNameRepo.insert(player.username)
        }

        val onlinePlayer = this.playerService.loadOnlinePlayer(player.uniqueId, player.username, property.value, property.signature, podName).await()

        if (onlinePlayer == null)
            this.playerService.createOfflinePlayer(player.uniqueId, player.username, property.value, property.signature, podName)

        val languageProperties = this.languagePropertiesService.loadLanguageProperties(player.uniqueId).await()

        if (languageProperties == null)
            this.languagePropertiesService.createLanguageProperties(player.uniqueId)
    }

    @Subscribe(order = PostOrder.LATE)
    suspend fun onPlayerDisconnect(event: DisconnectEvent) {
        val player = event.player
        val onlinePlayer = NeptunCoreProvider.api.playerService.getOnlinePlayer(player.uniqueId)

        if (onlinePlayer != null) {
            onlinePlayer.lastLogoutTimestamp = System.currentTimeMillis()
            onlinePlayer.updateOnlineTime()
        }

        this.onlineNameNameRepo.delete(player.username)
        this.playerService.unloadOnlinePlayer(player.uniqueId)
        this.languagePropertiesService.unloadLanguageProperties(player.uniqueId)
    }
}