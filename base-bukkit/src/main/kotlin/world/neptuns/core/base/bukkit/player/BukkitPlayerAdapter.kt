package world.neptuns.core.base.bukkit.player

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import world.neptuns.controller.api.NeptunControllerProvider
import world.neptuns.controller.api.packet.NetworkChannelRegistry
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommandPlatform
import world.neptuns.core.base.api.language.Language
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.core.base.api.player.PlayerAdapter
import world.neptuns.core.base.common.packet.*
import java.util.*

class BukkitPlayerAdapter : PlayerAdapter<Player> {

    override fun getMinecraftPlayer(uuid: UUID): Player? {
        return Bukkit.getPlayer(uuid)
    }

    override suspend fun transferPlayerToPlayersService(uuid: UUID, targetUuid: UUID) {
        val targetPlayer = NeptunCoreProvider.api.playerController.getOnlinePlayer(targetUuid) ?: return
        NeptunControllerProvider.api.packetController.sendPacket(PlayerConnectToServicePacket(uuid, false, targetPlayer.currentServiceName))
    }

    override fun transferPlayerToService(uuid: UUID, serviceName: String) {
        NeptunControllerProvider.api.packetController.sendPacket(PlayerConnectToServicePacket(uuid, false, serviceName))
    }

    override fun transferPlayerToLobby(uuid: UUID) {
        NeptunControllerProvider.api.packetController.sendPacket(PlayerConnectToServicePacket(uuid, true, null))
    }

    override fun teleport(uuid: UUID, x: Double, y: Double, z: Double, yaw: Float, pitch: Float, worldName: String?) {
        NeptunControllerProvider.api.packetController.sendPacket(PlayerTeleportPacket(uuid, null, x, y, z, yaw, pitch))
    }

    override fun teleport(uuid: UUID, x: Double, y: Double, z: Double, worldName: String?) {
        teleport(uuid, x, y, z, 0f, 0f, worldName)
    }

    override fun teleportToPlayer(uuid: UUID, targetUuid: UUID) {
        NeptunControllerProvider.api.packetController.sendPacket(PlayerTeleportToPlayerPacket(uuid, targetUuid))
    }

    override fun executeCommand(platform: NeptunCommandPlatform, player: Player, command: String) {
        val channel = if (platform == NeptunCommandPlatform.VELOCITY) NetworkChannelRegistry.PROXY else NetworkChannelRegistry.SERVICE
        NeptunControllerProvider.api.packetController.sendPacket(PlayerPerformCommandPacket(channel, player.uniqueId, command))
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
        NeptunControllerProvider.api.packetController.sendPacket(MessageToPlayerPacket(uuid, key.asString(), toReplace))
    }

    override suspend fun sendMessage(player: Player, key: LineKey, vararg toReplace: TagResolver) {
        validateLanguageProperties(player.uniqueId) { properties, language ->
            val components = if (language.hasMultipleLines(key)) language.lines(properties, key, *toReplace) else listOf(language.line(properties, key, *toReplace))
            components.forEach(player::sendMessage)
        }
    }

    private suspend fun validateLanguageProperties(uuid: UUID, result: (LanguageProperties, Language) -> Unit) {
        val properties = NeptunCoreProvider.api.languagePropertiesController.getProperties(uuid) ?: return
        result.invoke(properties, NeptunCoreProvider.api.languageController.getLanguage(properties.langKey))
    }

}