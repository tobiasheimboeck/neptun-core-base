package world.neptuns.core.base.velocity.player

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.title.Title
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommandPlatform
import world.neptuns.core.base.api.language.Language
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.core.base.api.player.PlayerAdapter
import world.neptuns.core.base.api.utils.NeptunPlugin
import world.neptuns.core.base.common.packet.MessageToPlayerPacket
import world.neptuns.core.base.common.packet.PlayerPerformCommandPacket
import world.neptuns.core.base.common.packet.PlayerTeleportPacket
import world.neptuns.core.base.common.packet.PlayerTeleportToPlayerPacket
import world.neptuns.streamline.api.NeptunStreamlineProvider
import world.neptuns.streamline.api.packet.NetworkChannelRegistry
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class VelocityPlayerAdapter(private val proxyServer: ProxyServer, override val pluginAdapter: NeptunPlugin) : PlayerAdapter<Player> {

    override fun getMinecraftPlayer(uuid: UUID): Player? {
        return this.proxyServer.getPlayer(uuid).orElse(null)
    }

    override suspend fun transferPlayerToPlayersService(uuid: UUID, targetUuid: UUID) {
        val neptunPlayer = NeptunCoreProvider.api.playerController.getOnlinePlayer(targetUuid) ?: return
        transferPlayerToService(uuid, neptunPlayer.currentServiceName)
    }

    override suspend fun transferPlayerToService(uuid: UUID, serviceName: String) {
        this.proxyServer.getServer(serviceName).ifPresent {
            val player = getMinecraftPlayer(uuid) ?: return@ifPresent
            player.createConnectionRequest(it).fireAndForget()
        }
    }

    override suspend fun transferPlayerToLobby(uuid: UUID) {
        val player = getMinecraftPlayer(uuid) ?: return

        val lobbyServers = this.proxyServer.allServers.filter { it.serverInfo.name.startsWith("Lobby") }
        val lobbyServer = lobbyServers[if (lobbyServers.size == 1) 0 else ThreadLocalRandom.current().nextInt(lobbyServers.size - 1)]

        player.createConnectionRequest(lobbyServer).fireAndForget()
    }

    override suspend fun teleport(uuid: UUID, x: Double, y: Double, z: Double, yaw: Float, pitch: Float, worldName: String?) {
        NeptunStreamlineProvider.api.packetController.sendPacket(PlayerTeleportPacket(uuid, null, x, y, z, yaw, pitch))
    }

    override suspend fun teleport(uuid: UUID, x: Double, y: Double, z: Double, worldName: String?) {
        teleport(uuid, x, y, z, 0f, 0f, worldName)
    }

    override suspend fun teleportToPlayer(uuid: UUID, targetUuid: UUID) {
        NeptunStreamlineProvider.api.packetController.sendPacket(PlayerTeleportToPlayerPacket(uuid, targetUuid))
    }

    override suspend fun executeCommand(platform: NeptunCommandPlatform, player: Player, command: String) {
        val channel = if (platform == NeptunCommandPlatform.VELOCITY) NetworkChannelRegistry.PROXY else NetworkChannelRegistry.SERVICE
        NeptunStreamlineProvider.api.packetController.sendPacket(PlayerPerformCommandPacket(channel, player.uniqueId, command))
    }

    override suspend fun sendPlayerListHeader(player: Player, key: LineKey, vararg toReplace: TagResolver) {
        validateLanguageProperties(player.uniqueId) { properties, language ->
            player.sendPlayerListHeader(language.line(properties, key, *toReplace))
        }
    }

    override suspend fun sendPlayerListFooter(player: Player, key: LineKey, vararg toReplace: TagResolver) {
        validateLanguageProperties(player.uniqueId) { properties, language ->
            player.sendPlayerListFooter(language.line(properties, key, *toReplace))
        }
    }

    override suspend fun sendTitle(player: Player, key: LineKey, vararg toReplace: TagResolver) {
        validateLanguageProperties(player.uniqueId) { properties, language ->
            val components = if (language.hasMultipleLines(key)) language.lines(properties, key, *toReplace) else listOf(language.line(properties, key, *toReplace))
            repeat(components.size) { player.showTitle(Title.title(components[0], components[1])) }
        }
    }

    override suspend fun sendActionBar(player: Player, key: LineKey, vararg toReplace: TagResolver) {
        validateLanguageProperties(player.uniqueId) { properties, language ->
            val components = if (language.hasMultipleLines(key)) language.lines(properties, key, *toReplace) else listOf(language.line(properties, key, *toReplace))
            player.showTitle(Title.title(components[0], components[1]))
        }
    }

    override suspend fun sendGlobalMessage(uuid: UUID, key: LineKey, toReplace: List<Pair<String, String>>) {
        NeptunStreamlineProvider.api.packetController.sendPacket(MessageToPlayerPacket(uuid, key.asString(), toReplace))
    }

    override suspend fun sendMessage(player: Player, key: LineKey, vararg toReplace: TagResolver) {
        validateLanguageProperties(player.uniqueId) { properties, language ->
            val components = if (language.hasMultipleLines(key)) language.lines(properties, key, *toReplace) else listOf(language.line(properties, key, *toReplace))
            components.forEach(player::sendMessage)
        }
    }

    private suspend fun validateLanguageProperties(uuid: UUID, result: (LanguageProperties, Language) -> Unit) {
        val properties = NeptunCoreProvider.api.languagePropertiesController.getProperties(uuid) ?: return
        val language = NeptunCoreProvider.api.languageController.getLanguage(properties.langKey) ?: return
        result.invoke(properties, language)
    }

}