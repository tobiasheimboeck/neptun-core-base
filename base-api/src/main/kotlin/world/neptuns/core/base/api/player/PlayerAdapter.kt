package world.neptuns.core.base.api.player

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import world.neptuns.core.base.api.language.Language
import world.neptuns.core.base.api.player.model.NeptunOfflinePlayer
import world.neptuns.core.base.api.player.model.NeptunPlayer
import java.util.*


interface PlayerAdapter<T> {

    fun getMinecraftPlayer(uuid: UUID): T?
    fun getMinecraftPlayer(name: String): T?

    fun getOnlinePlayer(uuid: UUID): NeptunPlayer?
    fun getOnlinePlayer(name: String): NeptunPlayer?

    fun getOfflinePlayer(uuid: UUID): NeptunOfflinePlayer?
    fun getOfflinePlayer(name: String): NeptunOfflinePlayer?

    fun getLanguage(uuid: UUID): Language

    fun broadcastMessage(broadcastType: BroadcastType?, key: String, toReplace: List<Pair<String, String>>)
    fun sendGlobalMessage(unqiueId: UUID?, key: String, toReplace: List<Pair<String, String>>)
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