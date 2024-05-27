package world.neptuns.core.base.api.player

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import world.neptuns.core.base.api.command.NeptunCommandPlatform
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.utils.NeptunPluginAdapter
import java.util.*


interface PlayerAdapter<T> {

    val pluginAdapter: NeptunPluginAdapter

    fun getMinecraftPlayer(uuid: UUID): T?

    suspend fun sendGlobalMessage(uuid: UUID, key: LineKey, toReplace: List<Pair<String, String>>)
    suspend fun sendGlobalMessage(uuid: UUID, key: String, toReplace: List<Pair<String, String>>) {
        sendGlobalMessage(uuid, LineKey.key(pluginAdapter.namespace, key), toReplace)
    }

    suspend fun sendMessage(player: T, key: LineKey, vararg toReplace: TagResolver)
    suspend fun sendMessage(player: T, key: String, vararg toReplace: TagResolver) {
        sendMessage(player, LineKey.key(pluginAdapter.namespace, key), *toReplace)
    }

    suspend fun sendActionBar(player: T, key: LineKey, vararg toReplace: TagResolver)
    suspend fun sendActionBar(player: T, key: String, vararg toReplace: TagResolver) {
        sendActionBar(player, LineKey.key(pluginAdapter.namespace, key), *toReplace)
    }

    suspend fun sendTitle(player: T, key: LineKey, vararg toReplace: TagResolver)
    suspend fun sendTitle(player: T, key: String, vararg toReplace: TagResolver) {
        sendTitle(player, LineKey.key(pluginAdapter.namespace, key), *toReplace)
    }

    suspend fun sendPlayerListHeader(player: T, key: LineKey, vararg toReplace: TagResolver)
    suspend fun sendPlayerListHeader(player: T, key: String, vararg toReplace: TagResolver) {
        sendPlayerListHeader(player, LineKey.key(pluginAdapter.namespace, key), *toReplace)
    }

    suspend fun sendPlayerListFooter(player: T, key: LineKey, vararg toReplace: TagResolver)
    suspend fun sendPlayerListFooter(player: T, key: String, vararg toReplace: TagResolver) {
        sendPlayerListFooter(player, LineKey.key(pluginAdapter.namespace, key), *toReplace)
    }

    suspend fun executeCommand(platform: NeptunCommandPlatform, player: T, command: String)

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