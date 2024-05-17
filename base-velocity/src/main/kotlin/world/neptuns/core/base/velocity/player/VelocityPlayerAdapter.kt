package world.neptuns.core.base.velocity.player

import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import world.neptuns.core.base.api.player.PlayerAdapter
import java.util.*

class VelocityPlayerAdapter : PlayerAdapter<Player> {

    override fun broadcastMessage(broadcastType: PlayerAdapter.BroadcastType, key: String, toReplace: List<Pair<String, String>>) {
        TODO("Not yet implemented")
    }

    override fun sendGlobalMessage(unqiueId: UUID, key: String, toReplace: List<Pair<String, String>>) {
        TODO("Not yet implemented")
    }

    override fun transferPlayerToPlayersService(uuid: UUID, targetUuid: UUID) {
        TODO("Not yet implemented")
    }

    override fun transferPlayerToService(uuid: UUID, serviceName: String) {
        TODO("Not yet implemented")
    }

    override fun transferPlayerToLobby(uuid: UUID) {
        TODO("Not yet implemented")
    }

    override fun teleport(uniqueId: UUID, x: Double, y: Double, z: Double, yaw: Float, pitch: Float, vararg worldName: String) {
        TODO("Not yet implemented")
    }

    override fun teleport(uniqueId: UUID, x: Double, y: Double, z: Double, yaw: Float, vararg worldName: String) {
        TODO("Not yet implemented")
    }

    override fun teleportToPlayer(uniqueId: UUID, targetUniqueId: UUID) {
        TODO("Not yet implemented")
    }

    override fun sendPlayerListFooter(player: Player, key: String, vararg toReplace: TagResolver) {
        TODO("Not yet implemented")
    }

    override fun sendPlayerListHeader(player: Player, key: String, vararg toReplace: TagResolver) {
        TODO("Not yet implemented")
    }

    override fun sendTitle(player: Player, key: String, vararg toReplace: TagResolver) {
        TODO("Not yet implemented")
    }

    override fun sendActionBar(player: Player, key: String, vararg toReplace: TagResolver) {
        TODO("Not yet implemented")
    }

    override fun sendMessage(player: Player, key: String, vararg toReplace: TagResolver) {
        TODO("Not yet implemented")
    }

    override fun sendRawMessage(player: Player, message: String) {
        TODO("Not yet implemented")
    }

}