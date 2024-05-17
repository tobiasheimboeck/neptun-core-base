package world.neptuns.core.base.api.player

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import java.util.*


interface PlayerAdapter<T> {

    fun broadcastMessage(broadcastType: BroadcastType, key: String, toReplace: List<Pair<String, String>>)
    fun sendGlobalMessage(unqiueId: UUID, key: String, toReplace: List<Pair<String, String>>)
    fun sendRawMessage(player: T, message: String)
    fun sendMessage(player: T, key: String, vararg toReplace: TagResolver)

    fun sendActionBar(player: T, key: String, vararg toReplace: TagResolver)
    fun sendTitle(player: T, key: String, vararg toReplace: TagResolver)

    fun sendPlayerListHeader(player: T, key: String, vararg toReplace: TagResolver)
    fun sendPlayerListFooter(player: T, key: String, vararg toReplace: TagResolver)

    // fun executeCommand(type: ElytraCommandType, player: T, command: String)

    fun transferPlayerToPlayersService(uuid: UUID, targetUuid: UUID)
    fun transferPlayerToService(uuid: UUID, serviceName: String)
    fun transferPlayerToLobby(uuid: UUID)

    fun teleport(uniqueId: UUID, x: Double, y: Double, z: Double, yaw: Float, pitch: Float, vararg worldName: String)
    fun teleport(uniqueId: UUID, x: Double, y: Double, z: Double, yaw: Float, vararg worldName: String)
    fun teleportToPlayer(uniqueId: UUID, targetUniqueId: UUID)

    enum class BroadcastType {
        PROXIES, CURRENT_SERVICE
    }

}