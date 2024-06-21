package world.neptuns.core.base.api.player.extension

import kotlinx.coroutines.Deferred
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.player.NeptunOfflinePlayer
import world.neptuns.core.base.api.player.NeptunOnlinePlayer
import java.util.*

val Audience.uuid: UUID
    get() = this.pointers().get(Identity.UUID).get()

val Audience.name: String
    get() = this.pointers().get(Identity.NAME).get()

val Audience.displayName: Component
    get() = this.pointers().get(Identity.DISPLAY_NAME).get()

suspend fun Audience.toOnlinePlayer(): NeptunOnlinePlayer {
    return NeptunCoreProvider.api.playerService.getOnlinePlayer(uuid)!!
}

suspend fun Audience.toOfflinePlayer(): Deferred<NeptunOfflinePlayer?> {
    return NeptunCoreProvider.api.playerService.getOfflinePlayerAsync(uuid)
}

suspend fun Audience.sendMessage(key: LineKey, vararg toReplace: TagResolver) {
    getPlayerAdapter().sendMessage(this, key, *toReplace)
}

suspend fun Audience.sendMessage(key: String, vararg toReplace: TagResolver) {
    getPlayerAdapter().sendMessage(this, key, *toReplace)
}

suspend fun Audience.sendActionBar(key: LineKey, vararg toReplace: TagResolver) {
    getPlayerAdapter().sendActionBar(this, key, *toReplace)
}

suspend fun Audience.sendActionBar(key: String, vararg toReplace: TagResolver) {
    getPlayerAdapter().sendActionBar(this, key, *toReplace)
}

suspend fun Audience.sendTitle(key: LineKey, vararg toReplace: TagResolver) {
    getPlayerAdapter().sendTitle(this, key, *toReplace)
}

suspend fun Audience.sendTitle(key: String, vararg toReplace: TagResolver) {
    getPlayerAdapter().sendTitle(this, key, *toReplace)
}

suspend fun Audience.transferToAudiencesService(targetUuid: UUID) {
    getPlayerAdapter().transferPlayerToPlayersService(uuid, targetUuid)
}

suspend fun Audience.transferToService(serviceName: String) {
    getPlayerAdapter().transferPlayerToService(uuid, serviceName)
}

suspend fun Audience.transferToLobby() {
    getPlayerAdapter().transferPlayerToLobby(uuid)
}

private fun getPlayerAdapter() = NeptunCoreProvider.api.getPlayerAdapter()
