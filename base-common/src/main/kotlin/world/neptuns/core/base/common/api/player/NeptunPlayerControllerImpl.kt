package world.neptuns.core.base.common.api.player

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.update
import world.neptuns.controller.api.service.NeptunService
import world.neptuns.core.base.api.player.NeptunOfflinePlayer
import world.neptuns.core.base.api.player.NeptunOnlinePlayer
import world.neptuns.core.base.api.player.NeptunPlayerController
import world.neptuns.core.base.common.api.skin.SkinProfileImpl
import world.neptuns.core.base.common.repository.player.OfflinePlayerTable
import world.neptuns.core.base.common.repository.player.OnlinePlayerCache
import world.neptuns.core.base.common.repository.player.OnlinePlayerRepository
import world.neptuns.streamline.api.NeptunStreamlineProvider
import java.util.*

@Suppress("OPT_IN_USAGE")
class NeptunPlayerControllerImpl(override val updateChannel: String) : NeptunPlayerController {

    private val onlinePlayerRepository = NeptunStreamlineProvider.api.repositoryLoader.get(OnlinePlayerRepository::class.java)!!
    private val onlinePlayerCache = NeptunStreamlineProvider.api.cacheLoader.get(OnlinePlayerCache::class.java)!!

    init {
        GlobalScope.launch(Dispatchers.IO) {
            onlinePlayerRepository.onUpdate() { uuid, onlinePlayer ->
                if (!onlinePlayerCache.contains(uuid)) return@onUpdate
                onlinePlayerCache.update(uuid, onlinePlayer)
            }
        }
    }

    override suspend fun isOnline(uuid: UUID): Boolean {
        return this.onlinePlayerCache.contains(uuid) || this.onlinePlayerRepository.contains(uuid).await()
    }

    override suspend fun getOnlinePlayer(uuid: UUID): NeptunOnlinePlayer? {
        return this.onlinePlayerCache.get(uuid) ?: this.onlinePlayerRepository.get(uuid).await()
    }

    override suspend fun getOnlinePlayersFromService(neptunService: NeptunService): List<NeptunOfflinePlayer> {
        return this.onlinePlayerCache.getAll { it.currentServiceName == neptunService.id }
    }

    override suspend fun getOnlinePlayers(): List<NeptunOnlinePlayer> {
        return this.onlinePlayerCache.getAll().ifEmpty { this.onlinePlayerRepository.getAll().await() }
    }

    override suspend fun getOfflinePlayerAsync(uuid: UUID): Deferred<NeptunOfflinePlayer?> {
        return suspendedTransactionAsync(Dispatchers.IO) {
            val resultRow = OfflinePlayerTable.selectAll().where { OfflinePlayerTable.uuid eq uuid }.limit(1).firstOrNull()
            resultRow?.let { constructEntry(it) }
        }
    }

    override suspend fun getOfflinePlayersAsync(): Deferred<List<NeptunOfflinePlayer>> {
        return suspendedTransactionAsync(Dispatchers.IO) {
            OfflinePlayerTable.selectAll().toList().map { constructEntry(it) }
        }
    }

    override suspend fun getGloballyRegisteredPlayersAmount(): Deferred<Int> {
        return suspendedTransactionAsync(Dispatchers.IO) {
            OfflinePlayerTable.selectAll().toList().size
        }
    }

    override suspend fun createOrLoadEntry(key: UUID, defaultValue: NeptunOfflinePlayer?, vararg data: Any) {
        newSuspendedTransaction {
            val resultRow = OfflinePlayerTable.selectAll().where { OfflinePlayerTable.uuid eq key }.limit(1).firstOrNull()
            val loadedOfflinePlayer: NeptunOfflinePlayer

            if (resultRow == null && defaultValue != null) {
                OfflinePlayerTable.insert {
                    it[this.uuid] = key
                    it[this.name] = defaultValue.name
                    it[this.firstLoginTimestamp] = defaultValue.firstLoginTimestamp
                    it[this.lastLoginTimestamp] = defaultValue.lastLoginTimestamp
                    it[this.lastLogoutTimestamp] = defaultValue.lastLogoutTimestamp
                    it[this.onlineTime] = defaultValue.onlineTime
                    it[this.crystals] = defaultValue.crystals
                    it[this.shards] = defaultValue.shards
                    it[this.skinValue] = defaultValue.skinProfile.value
                    it[this.skinSignature] = defaultValue.skinProfile.signature
                }

                loadedOfflinePlayer = defaultValue
            } else {
                loadedOfflinePlayer = constructEntry(resultRow!!)
            }

            val onlinePlayer = NeptunOnlinePlayerImpl.create(loadedOfflinePlayer, data[0] as String, "-")

            if (defaultValue != null) {
                onlinePlayer.name = defaultValue.name
                onlinePlayer.lastLoginTimestamp = defaultValue.lastLoginTimestamp
                onlinePlayer.skinProfile.value = defaultValue.skinProfile.value
                onlinePlayer.skinProfile.signature = defaultValue.skinProfile.signature
                bulkUpdateEntry(NeptunOfflinePlayer.Update.ALL, key, onlinePlayer, false)
            }

            cacheEntry(onlinePlayer.uuid, onlinePlayer)
            onlinePlayerRepository.insert(onlinePlayer.uuid, onlinePlayer)
        }
    }

    override suspend fun unloadEntry(key: UUID) {
        this.onlinePlayerRepository.delete(key)
        this.onlinePlayerCache.delete(key)
    }

    override fun constructEntry(resultRow: ResultRow): NeptunOfflinePlayer {
        return NeptunOfflinePlayerImpl(
            resultRow[OfflinePlayerTable.uuid],
            resultRow[OfflinePlayerTable.name],
            resultRow[OfflinePlayerTable.firstLoginTimestamp],
            resultRow[OfflinePlayerTable.lastLoginTimestamp],
            resultRow[OfflinePlayerTable.lastLogoutTimestamp],
            resultRow[OfflinePlayerTable.onlineTime],
            SkinProfileImpl(resultRow[OfflinePlayerTable.skinValue], resultRow[OfflinePlayerTable.skinSignature]),
            resultRow[OfflinePlayerTable.crystals],
            resultRow[OfflinePlayerTable.shards],
        )
    }

    override fun cacheEntry(key: UUID, value: NeptunOfflinePlayer) {
        this.onlinePlayerCache.insert(key, value as NeptunOnlinePlayer)

    }

    override fun removeEntryFromCache(key: UUID) {
        this.onlinePlayerCache.delete(key)
    }

    override suspend fun bulkUpdateEntry(updateType: NeptunOfflinePlayer.Update, key: UUID, newValue: Any, updateCache: Boolean, result: (Unit) -> Unit) {
        newSuspendedTransaction(Dispatchers.IO) {
            OfflinePlayerTable.update({ OfflinePlayerTable.uuid eq key }) {
                when (updateType) {
                    NeptunOfflinePlayer.Update.ALL -> {
                        if (newValue !is NeptunOfflinePlayer)
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
                    NeptunOfflinePlayer.Update.CURRENT_SERVICE -> {}
                }
            }
        }

        if (updateCache) updateCachedEntry(updateType, key, newValue, result)
    }

    override suspend fun updateCachedEntry(updateType: NeptunOfflinePlayer.Update, key: UUID, newValue: Any, result: (Unit) -> Unit) {
        val onlinePlayer = getOnlinePlayer(key) ?: return

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
                onlinePlayer.currentServiceName = newValue.currentServiceName
            }

            NeptunOfflinePlayer.Update.NAME -> onlinePlayer.name = newValue as String
            NeptunOfflinePlayer.Update.LAST_LOGIN_TIMESTAMP -> onlinePlayer.lastLoginTimestamp = newValue as Long
            NeptunOfflinePlayer.Update.LAST_LOGOUT_TIMESTAMP -> onlinePlayer.lastLogoutTimestamp = newValue as Long
            NeptunOfflinePlayer.Update.ONLINE_TIME -> onlinePlayer.onlineTime = newValue as Long
            NeptunOfflinePlayer.Update.SKIN_VALUE -> onlinePlayer.skinProfile.value = newValue as String
            NeptunOfflinePlayer.Update.SKIN_SIGNATURE -> onlinePlayer.skinProfile.signature = newValue as String
            NeptunOfflinePlayer.Update.CRYSTALS -> onlinePlayer.crystals = newValue as Long
            NeptunOfflinePlayer.Update.SHARDS -> onlinePlayer.shards = newValue as Long
            NeptunOfflinePlayer.Update.CURRENT_SERVICE -> onlinePlayer.currentServiceName = newValue as String
        }

        onlinePlayerRepository.update(key, onlinePlayer)
    }

}