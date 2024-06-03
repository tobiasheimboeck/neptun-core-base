package world.neptuns.core.base.velocity.listener

import com.velocitypowered.api.event.PostOrder
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
import world.neptuns.core.base.api.language.properties.LanguagePropertiesController
import world.neptuns.core.base.api.player.NeptunPlayerController
import world.neptuns.core.base.common.api.language.properties.LanguagePropertiesImpl
import world.neptuns.core.base.common.api.player.NeptunOfflinePlayerImpl


class VelocityPlayerListener(
    private val playerController: NeptunPlayerController,
    private val languagePropertiesController: LanguagePropertiesController,
) {

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

    @Subscribe(order = PostOrder.EARLY) // PermissionSystem: FIRST, CoreSystem: EARLY
    suspend fun onPlayerLogin(event: LoginEvent) {
        val player = event.player
        val property = player.gameProfile.properties.first()
        val podName = NeptunControllerProvider.api.podName()

        val defaultLanguage = NeptunCoreProvider.api.languageController.getLanguage(LangKey.key("en", "US")) ?: return

        if (player.protocolVersion != ProtocolVersion.MINECRAFT_1_20_3) {
            player.disconnect(defaultLanguage.line(CoreBaseApi.defaultLangProperties, LineKey.key("core.base.proxy.protocol.wrong_version")))
            return
        }

        val serverGroup = NeptunControllerProvider.api.serviceGroupController.getServiceGroup("proxy") ?: return

        if (serverGroup.inMaintenance && !player.hasPermission("core.maintenance.bypass")) {
            player.disconnect(defaultLanguage.line(CoreBaseApi.defaultLangProperties, LineKey.key("core.base.proxy.maintenance.forbidden")))
            return
        }

        this.playerController.createOrLoadEntry(player.uniqueId, NeptunOfflinePlayerImpl.create(player.uniqueId, player.username, property.value, property.signature), podName)
        this.languagePropertiesController.createOrLoadEntry(player.uniqueId, LanguagePropertiesImpl.create(player.uniqueId))
    }

    @Subscribe(order = PostOrder.LATE)
    suspend fun onPlayerDisconnect(event: DisconnectEvent) {
        val player = event.player

        val onlinePlayer = NeptunCoreProvider.api.playerController.getOnlinePlayer(player.uniqueId)

        if (onlinePlayer != null) {
            onlinePlayer.lastLogoutTimestamp = System.currentTimeMillis()
            onlinePlayer.updateOnlineTime()
        }

        this.playerController.unloadEntry(player.uniqueId)
        this.languagePropertiesController.unloadEntry(player.uniqueId)
    }
}