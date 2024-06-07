package world.neptuns.base.bukkit.api.extension

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.entity.Player
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.player.NeptunOnlinePlayer
import java.util.*

suspend fun Player.toOnlinePlayer(): NeptunOnlinePlayer {
    return NeptunCoreProvider.api.playerController.getOnlinePlayer(uniqueId)!!
}

suspend fun Player.sendMessage(key: LineKey, vararg toReplace: TagResolver) {
    getPlayerAdapter().sendMessage(this, key, *toReplace)
}

suspend fun Player.sendMessage(key: String, vararg toReplace: TagResolver) {
    getPlayerAdapter().sendMessage(this, key, *toReplace)
}

suspend fun Player.sendActionBar(key: LineKey, vararg toReplace: TagResolver) {
    getPlayerAdapter().sendActionBar(this, key, *toReplace)
}

suspend fun Player.sendActionBar(key: String, vararg toReplace: TagResolver) {
    getPlayerAdapter().sendActionBar(this, key, *toReplace)
}

suspend fun Player.sendTitle(key: LineKey, vararg toReplace: TagResolver) {
    getPlayerAdapter().sendTitle(this, key, *toReplace)
}

suspend fun Player.sendTitle(key: String, vararg toReplace: TagResolver) {
    getPlayerAdapter().sendTitle(this, key, *toReplace)
}

suspend fun Player.transferToPlayersService(targetUuid: UUID) {
    getPlayerAdapter().transferPlayerToPlayersService(this.uniqueId, targetUuid)
}

suspend fun Player.transferToService(serviceName: String) {
    getPlayerAdapter().transferPlayerToService(this.uniqueId, serviceName)
}

suspend fun Player.transferToLobby() {
    getPlayerAdapter().transferPlayerToLobby(this.uniqueId)
}

private fun getPlayerAdapter() = NeptunCoreProvider.api.getPlayerAdapter(Player::class.java)