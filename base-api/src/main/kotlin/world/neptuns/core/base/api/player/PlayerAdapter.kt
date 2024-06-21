package world.neptuns.core.base.api.player

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import world.neptuns.core.base.api.command.NeptunCommandPlatform
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.util.NeptunPlugin
import java.util.*

interface PlayerAdapter {

    val pluginAdapter: NeptunPlugin

    suspend fun sendGlobalMessage(uuid: UUID, key: LineKey, toReplace: List<Pair<String, String>>)
    suspend fun sendGlobalMessage(uuid: UUID, key: String, toReplace: List<Pair<String, String>>) {
        sendGlobalMessage(uuid, LineKey.key(pluginAdapter.namespace, key), toReplace)
    }

    suspend fun sendMessage(audience: Audience, key: LineKey, vararg toReplace: TagResolver)
    suspend fun sendMessage(audience: Audience, key: String, vararg toReplace: TagResolver) {
        sendMessage(audience, LineKey.key(pluginAdapter.namespace, key), *toReplace)
    }

    suspend fun sendActionBar(audience: Audience, key: LineKey, vararg toReplace: TagResolver)
    suspend fun sendActionBar(audience: Audience, key: String, vararg toReplace: TagResolver) {
        sendActionBar(audience, LineKey.key(pluginAdapter.namespace, key), *toReplace)
    }

    suspend fun sendTitle(audience: Audience, key: LineKey, vararg toReplace: TagResolver)
    suspend fun sendTitle(audience: Audience, key: String, vararg toReplace: TagResolver) {
        sendTitle(audience, LineKey.key(pluginAdapter.namespace, key), *toReplace)
    }

    suspend fun sendPlayerListHeader(audience: Audience, key: LineKey, vararg toReplace: TagResolver)
    suspend fun sendPlayerListHeader(audience: Audience, key: String, vararg toReplace: TagResolver) {
        sendPlayerListHeader(audience, LineKey.key(pluginAdapter.namespace, key), *toReplace)
    }

    suspend fun sendPlayerListFooter(audience: Audience, key: LineKey, vararg toReplace: TagResolver)
    suspend fun sendPlayerListFooter(audience: Audience, key: String, vararg toReplace: TagResolver) {
        sendPlayerListFooter(audience, LineKey.key(pluginAdapter.namespace, key), *toReplace)
    }

    suspend fun executeCommand(platform: NeptunCommandPlatform, audience: Audience, command: String)

    suspend fun transferPlayerToPlayersService(uuid: UUID, targetUuid: UUID)
    suspend fun transferPlayerToService(uuid: UUID, serviceName: String)
    suspend fun transferPlayerToLobby(uuid: UUID)

    suspend fun teleport(uuid: UUID, x: Double, y: Double, z: Double, yaw: Float, pitch: Float, worldName: String?)
    suspend fun teleport(uuid: UUID, x: Double, y: Double, z: Double, worldName: String?)
    suspend fun teleportToPlayer(uuid: UUID, targetUuid: UUID)

    enum class BroadcastType {
        PROXIES, CURRENT_SERVICE
    }

}