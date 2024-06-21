package world.neptuns.core.base.bukkit.player

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.title.Title
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommandPlatform
import world.neptuns.core.base.api.language.Language
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.core.base.api.player.PlayerAdapter
import world.neptuns.core.base.api.player.extension.uuid
import world.neptuns.core.base.api.util.NeptunPlugin
import world.neptuns.core.base.common.packet.*
import world.neptuns.streamline.api.NeptunStreamlineProvider
import world.neptuns.streamline.api.packet.NetworkChannelRegistry
import java.util.*

class BukkitPlayerAdapter(override val pluginAdapter: NeptunPlugin) : PlayerAdapter {

    override suspend fun transferPlayerToPlayersService(uuid: UUID, targetUuid: UUID) {
        val targetPlayer = NeptunCoreProvider.api.playerService.getOnlinePlayer(targetUuid) ?: return
        NeptunStreamlineProvider.api.packetController.sendPacket(PlayerConnectToServicePacket(uuid, false, targetPlayer.currentServiceName))
    }

    override suspend fun transferPlayerToService(uuid: UUID, serviceName: String) {
        NeptunStreamlineProvider.api.packetController.sendPacket(PlayerConnectToServicePacket(uuid, false, serviceName))
    }

    override suspend fun transferPlayerToLobby(uuid: UUID) {
        NeptunStreamlineProvider.api.packetController.sendPacket(PlayerConnectToServicePacket(uuid, true, null))
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

    override suspend fun executeCommand(platform: NeptunCommandPlatform, audience: Audience, command: String) {
        val channel = if (platform == NeptunCommandPlatform.VELOCITY) NetworkChannelRegistry.PROXY else NetworkChannelRegistry.SERVICE
        NeptunStreamlineProvider.api.packetController.sendPacket(PlayerPerformCommandPacket(channel, audience.uuid, command))
    }

    override suspend fun sendPlayerListHeader(audience: Audience, key: LineKey, vararg toReplace: TagResolver) {
        validateLanguageProperties(audience.uuid) { properties, language ->
            audience.sendPlayerListHeader(language.line(properties, key, *toReplace))
        }
    }

    override suspend fun sendPlayerListFooter(audience: Audience, key: LineKey, vararg toReplace: TagResolver) {
        validateLanguageProperties(audience.uuid) { properties, language ->
            audience.sendPlayerListFooter(language.line(properties, key, *toReplace))
        }
    }

    override suspend fun sendTitle(audience: Audience, key: LineKey, vararg toReplace: TagResolver) {
        validateLanguageProperties(audience.uuid) { properties, language ->
            val components = if (language.hasMultipleLines(key)) language.lines(properties, key, *toReplace) else listOf(language.line(properties, key, *toReplace))
            repeat(components.size) { audience.showTitle(Title.title(components[0], components[1])) }
        }
    }

    override suspend fun sendActionBar(audience: Audience, key: LineKey, vararg toReplace: TagResolver) {
        validateLanguageProperties(audience.uuid) { properties, language ->
            val components = if (language.hasMultipleLines(key)) language.lines(properties, key, *toReplace) else listOf(language.line(properties, key, *toReplace))
            audience.showTitle(Title.title(components[0], components[1]))
        }
    }

    override suspend fun sendGlobalMessage(uuid: UUID, key: LineKey, toReplace: List<Pair<String, String>>) {
        NeptunStreamlineProvider.api.packetController.sendPacket(MessageToPlayerPacket(uuid, key.asString(), toReplace))
    }

    override suspend fun sendMessage(audience: Audience, key: LineKey, vararg toReplace: TagResolver) {
        validateLanguageProperties(audience.uuid) { properties, language ->
            val components = if (language.hasMultipleLines(key)) language.lines(properties, key, *toReplace) else listOf(language.line(properties, key, *toReplace))
            components.forEach(audience::sendMessage)
        }
    }

    private suspend fun validateLanguageProperties(uuid: UUID, result: (LanguageProperties, Language) -> Unit) {
        val properties = NeptunCoreProvider.api.languagePropertiesService.getProperties(uuid) ?: return
        val language = NeptunCoreProvider.api.languageController.getLanguage(properties.langKey) ?: return
        result.invoke(properties, language)
    }

}