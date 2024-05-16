package world.neptuns.core.base.common.api.player

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import world.neptuns.controller.api.service.NeptunService
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.player.NeptunOfflinePlayer
import world.neptuns.core.base.api.player.NeptunOnlinePlayer
import world.neptuns.core.base.api.player.NeptunPlayerController
import world.neptuns.core.base.common.api.skin.SkinProfileImpl
import world.neptuns.core.base.common.repository.OfflinePlayerRepository
import world.neptuns.core.base.common.repository.OnlinePlayerRepository
import java.util.*

class NeptunPlayerControllerImpl : NeptunPlayerController {

    private val onlinePlayerRepository = NeptunCoreProvider.api.repositoryLoader.get(OnlinePlayerRepository::class.java)!!

    override suspend fun isOnline(uuid: UUID): Deferred<Boolean> {
        return onlinePlayerRepository.contains(uuid)
    }

    override suspend fun getOnlinePlayerAsync(uuid: UUID): Deferred<NeptunOnlinePlayer?> {
        return onlinePlayerRepository.get(uuid)
    }

    override suspend fun getOnlinePlayersFromServiceAsync(neptunService: NeptunService): Deferred<List<NeptunOfflinePlayer>> {
        return this.onlinePlayerRepository.getAll { it.currentServiceName == neptunService.id }
    }

    override suspend fun getOnlinePlayersAsync(): Deferred<List<NeptunOnlinePlayer>> {
        return this.onlinePlayerRepository.getAll()
    }

    override suspend fun getOfflinePlayerAsync(uuid: UUID): Deferred<NeptunOfflinePlayer?> {
        return suspendedTransactionAsync(Dispatchers.IO) {
            constructOfflinePlayer(OfflinePlayerRepository.selectAll().where { OfflinePlayerRepository.uuid eq uuid }.limit(1).firstOrNull(), null)
        }
    }

    override suspend fun getOfflinePlayersAsync(): Deferred<List<NeptunOfflinePlayer>> {
        return suspendedTransactionAsync(Dispatchers.IO) {
            OfflinePlayerRepository.selectAll().toList().map { constructOfflinePlayer(it, null)!! }
        }
    }

    override suspend fun getGloballyRegisteredPlayersAmount(): Deferred<Int> {
        return suspendedTransactionAsync(Dispatchers.IO) {
            OfflinePlayerRepository.selectAll().toList().size
        }
    }

    override suspend fun handlePlayerCreation(uuid: UUID, name: String, skinValue: String, skinSignature: String, proxyServiceName: String, minecraftServiceName: String) {
        newSuspendedTransaction(Dispatchers.IO) {
            val resultRow = OfflinePlayerRepository.selectAll().where { OfflinePlayerRepository.uuid eq uuid }.limit(1).firstOrNull()
            val offlinePlayer: NeptunOfflinePlayer

            if (resultRow == null) {
                offlinePlayer = NeptunOfflinePlayerImpl.create(uuid, name, skinValue, skinValue)
                OfflinePlayerRepository.insert {
                    it[this.uuid] = offlinePlayer.uuid
                    it[this.name] = offlinePlayer.name
                    it[this.firstLoginTimestamp] = offlinePlayer.firstLoginTimestamp
                    it[this.lastLoginTimestamp] = offlinePlayer.lastLoginTimestamp
                    it[this.lastLogoutTimestamp] = offlinePlayer.lastLogoutTimestamp
                    it[this.onlineTime] = offlinePlayer.onlineTime
                    it[this.skinValue] = offlinePlayer.skinProfile.value
                    it[this.skinSignature] = offlinePlayer.skinProfile.signature
                    it[this.crystals] = offlinePlayer.crystals
                    it[this.shards] = offlinePlayer.shards
                }
            } else {
                val usernameChanged = resultRow[OfflinePlayerRepository.name] != name
                val username = if (usernameChanged) name else resultRow[OfflinePlayerRepository.name]

                offlinePlayer = constructOfflinePlayer(resultRow, username)!!
                if (usernameChanged) bulkUpdateEntry(uuid, NeptunOfflinePlayer.Update.NAME, username, false)
            }

            onlinePlayerRepository.insert(uuid, NeptunOnlinePlayerImpl.create(offlinePlayer, proxyServiceName, minecraftServiceName))
        }
    }

    override suspend fun handlePlayerDeletion(uuid: UUID) {
        withContext(Dispatchers.IO) {
            val offlinePlayer = getOfflinePlayerAsync(uuid).await() ?: return@withContext
            offlinePlayer.updateOnlineTime()

            transaction {
                OfflinePlayerRepository.update({ OfflinePlayerRepository.uuid eq uuid }) {
                    it[onlineTime] = offlinePlayer.onlineTime
                }
            }

            onlinePlayerRepository.delete(uuid)
        }
    }

    override suspend fun bulkUpdateEntry(key: UUID, updateType: NeptunOfflinePlayer.Update, newValue: Any, updateCache: Boolean, result: (Unit) -> Unit) {
        newSuspendedTransaction(Dispatchers.IO) {
            OfflinePlayerRepository.update({ OfflinePlayerRepository.uuid eq key }) {
                when (updateType) {
                    NeptunOfflinePlayer.Update.ALL -> {
                        if (newValue !is NeptunOnlinePlayer)
                            throw UnsupportedOperationException("Object has to be an NeptunOfflinePlayer instance!")

                        it[name] = newValue.name
                        it[lastLoginTimestamp] = newValue.lastLoginTimestamp
                        it[lastLogoutTimestamp] = newValue.lastLogoutTimestamp
                        it[onlineTime] = newValue.onlineTime
                        it[skinValue] = newValue.skinProfile.value
                        it[skinSignature] = newValue.skinProfile.signature
                        it[crystals] = newValue.crystals
                        it[shards] = newValue.shards
                    }

                    NeptunOfflinePlayer.Update.NAME -> it[name] = newValue as String
                    NeptunOfflinePlayer.Update.LAST_LOGIN_TIMESTAMP -> it[lastLoginTimestamp] = newValue as Long
                    NeptunOfflinePlayer.Update.LAST_LOGOUT_TIMESTAMP -> it[lastLogoutTimestamp] = newValue as Long
                    NeptunOfflinePlayer.Update.ONLINE_TIME -> it[onlineTime] = newValue as Long
                    NeptunOfflinePlayer.Update.SKIN_VALUE -> it[skinValue] = newValue as String
                    NeptunOfflinePlayer.Update.SKIN_SIGNATURE -> it[skinSignature] = newValue as String
                    NeptunOfflinePlayer.Update.CRYSTALS -> it[crystals] = newValue as Long
                    NeptunOfflinePlayer.Update.SHARDS -> it[shards] = newValue as Long
                }
            }
        }

        if (updateCache) updateCachedEntry(key, updateType, newValue, result)
    }

    override suspend fun updateCachedEntry(key: UUID, updateType: NeptunOfflinePlayer.Update, newValue: Any, result: (Unit) -> Unit) {
        withContext(Dispatchers.IO) {
            val onlinePlayer = getOnlinePlayerAsync(key).await() ?: return@withContext

            when (updateType) {
                NeptunOfflinePlayer.Update.ALL -> {
                    if (newValue !is NeptunOnlinePlayer)
                        throw UnsupportedOperationException("Object has to be an NeptunOnlinePlayer instance!")

                    onlinePlayer.name = newValue.name
                    onlinePlayer.lastLoginTimestamp = newValue.lastLoginTimestamp
                    onlinePlayer.lastLogoutTimestamp = newValue.lastLogoutTimestamp
                    onlinePlayer.onlineTime = newValue.onlineTime
                    onlinePlayer.skinProfile.value = newValue.skinProfile.value
                    onlinePlayer.skinProfile.signature = newValue.skinProfile.signature
                    onlinePlayer.crystals = newValue.crystals
                    onlinePlayer.shards = newValue.shards
                }

                NeptunOfflinePlayer.Update.NAME -> onlinePlayer.name = newValue as String
                NeptunOfflinePlayer.Update.LAST_LOGIN_TIMESTAMP -> onlinePlayer.lastLoginTimestamp = newValue as Long
                NeptunOfflinePlayer.Update.LAST_LOGOUT_TIMESTAMP -> onlinePlayer.lastLogoutTimestamp = newValue as Long
                NeptunOfflinePlayer.Update.ONLINE_TIME -> onlinePlayer.onlineTime = newValue as Long
                NeptunOfflinePlayer.Update.SKIN_VALUE -> onlinePlayer.skinProfile.value = newValue as String
                NeptunOfflinePlayer.Update.SKIN_SIGNATURE -> onlinePlayer.skinProfile.signature = newValue as String
                NeptunOfflinePlayer.Update.CRYSTALS -> onlinePlayer.crystals = newValue as Long
                NeptunOfflinePlayer.Update.SHARDS -> onlinePlayer.shards = newValue as Long
            }

            onlinePlayerRepository.update(key, onlinePlayer)
        }
    }

    private fun constructOfflinePlayer(resultRow: ResultRow?, name: String?): NeptunOfflinePlayer? {
        return if (resultRow != null) {
            NeptunOfflinePlayerImpl(
                resultRow[OfflinePlayerRepository.uuid],
                name ?: resultRow[OfflinePlayerRepository.name],
                resultRow[OfflinePlayerRepository.firstLoginTimestamp],
                resultRow[OfflinePlayerRepository.lastLoginTimestamp],
                resultRow[OfflinePlayerRepository.lastLogoutTimestamp],
                resultRow[OfflinePlayerRepository.onlineTime],
                SkinProfileImpl(resultRow[OfflinePlayerRepository.skinValue], resultRow[OfflinePlayerRepository.skinSignature]),
                resultRow[OfflinePlayerRepository.crystals],
                resultRow[OfflinePlayerRepository.shards],
            )
        } else {
            null
        }
    }

}