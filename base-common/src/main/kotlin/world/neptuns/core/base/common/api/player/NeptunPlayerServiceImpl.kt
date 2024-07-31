package world.neptuns.core.base.common.api.player

import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import world.neptuns.controller.api.service.NeptunService
import world.neptuns.core.base.api.player.NeptunOfflinePlayer
import world.neptuns.core.base.api.player.NeptunOnlinePlayer
import world.neptuns.core.base.api.player.NeptunPlayerService
import world.neptuns.core.base.common.api.skin.SkinProfileImpl
import world.neptuns.core.base.common.database.player.OfflinePlayerTable
import world.neptuns.core.base.common.database.player.dao.OfflinePlayerEntity
import world.neptuns.core.base.common.repository.player.OnlinePlayerCache
import world.neptuns.core.base.common.repository.player.OnlinePlayerNameRepository
import world.neptuns.core.base.common.repository.player.OnlinePlayerRepository
import world.neptuns.streamline.api.NeptunStreamlineProvider
import java.util.*

@Suppress("OPT_IN_USAGE")
class NeptunPlayerServiceImpl : NeptunPlayerService {

    private val onlinePlayerNameRepository = NeptunStreamlineProvider.api.repositoryLoader.get(OnlinePlayerNameRepository::class.java)!!
    private val onlinePlayerRepository = NeptunStreamlineProvider.api.repositoryLoader.get(OnlinePlayerRepository::class.java)!!
    private val onlinePlayerCache = NeptunStreamlineProvider.api.cacheLoader.get(OnlinePlayerCache::class.java)!!

    init {
        GlobalScope.launch(Dispatchers.IO) {
            onlinePlayerRepository.onUpdate { uuid, onlinePlayer ->
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

    override suspend fun getOnlinePlayer(name: String): NeptunOnlinePlayer? {
        return this.onlinePlayerCache.getAll { it.name.equals(name, true) }.firstOrNull()
            ?: this.onlinePlayerRepository.getAll { it.name.equals(name, true) }.await().firstOrNull()
    }

    override suspend fun getOnlinePlayersFromService(neptunService: NeptunService): List<NeptunOfflinePlayer> {
        return this.onlinePlayerCache.getAll { it.currentServiceName == neptunService.id }
    }

    override suspend fun getOnlinePlayers(): List<NeptunOnlinePlayer> {
        return this.onlinePlayerCache.getAll().ifEmpty { this.onlinePlayerRepository.getAll().await() }
    }

    override suspend fun getOnlinePlayerNames(): List<String> {
        return this.onlinePlayerNameRepository.getAll().await()
    }

    override suspend fun getOfflinePlayerAsync(uuid: UUID): Deferred<NeptunOfflinePlayer?> {
        return suspendedTransactionAsync(Dispatchers.IO) {
            val playerEntity = OfflinePlayerEntity.find { OfflinePlayerTable.uuid eq uuid }.firstOrNull()
                ?: return@suspendedTransactionAsync null

            NeptunOfflinePlayerImpl(
                playerEntity.uuid,
                playerEntity.name,
                playerEntity.firstLoginTimestamp,
                playerEntity.lastLoginTimestamp,
                playerEntity.lastLogoutTimestamp,
                playerEntity.onlineTime,
                SkinProfileImpl(playerEntity.skinValue, playerEntity.skinSignature),
                playerEntity.crystals,
                playerEntity.shards
            )
        }
    }

    override suspend fun getOfflinePlayerAsync(name: String): Deferred<NeptunOfflinePlayer?> {
        return suspendedTransactionAsync(Dispatchers.IO) {
            val playerEntity = OfflinePlayerEntity.find { OfflinePlayerTable.name eq name }.firstOrNull()
                ?: return@suspendedTransactionAsync null

            NeptunOfflinePlayerImpl(
                playerEntity.uuid,
                playerEntity.name,
                playerEntity.firstLoginTimestamp,
                playerEntity.lastLoginTimestamp,
                playerEntity.lastLogoutTimestamp,
                playerEntity.onlineTime,
                SkinProfileImpl(playerEntity.skinValue, playerEntity.skinSignature),
                playerEntity.crystals,
                playerEntity.shards
            )
        }
    }

    override suspend fun getGloballyRegisteredPlayersAmount(): Deferred<Int> {
        return suspendedTransactionAsync(Dispatchers.IO) {
            OfflinePlayerTable.selectAll().toList().size
        }
    }

    override suspend fun createOfflinePlayer(uuid: UUID, name: String, skinValue: String, skinSignature: String, currentProxyName: String) {
        val currentTime = System.currentTimeMillis()

        newSuspendedTransaction {
            OfflinePlayerEntity.new {
                this.uuid = uuid
                this.name = name
                this.firstLoginTimestamp = currentTime
                this.lastLoginTimestamp = currentTime
                this.lastLogoutTimestamp = 0L
                this.onlineTime = 0L
                this.crystals = 0L
                this.shards = 0L
                this.skinValue = skinValue
                this.skinSignature = skinSignature
            }
        }

        val offlinePlayer = NeptunOfflinePlayerImpl(
            uuid,
            name,
            currentTime,
            currentTime,
            0L,
            0L,
            SkinProfileImpl(skinValue, skinSignature),
            0L,
            0L
        )

        val onlinePlayer = NeptunOnlinePlayerImpl.create(offlinePlayer, currentProxyName, "-")

        cacheOnlinePlayer(uuid, onlinePlayer)
        this.onlinePlayerRepository.insert(uuid, onlinePlayer)
    }

    override suspend fun loadOnlinePlayer(
        uuid: UUID,
        name: String,
        skinValue: String,
        skinSignature: String,
        currentProxyName: String
    ): Deferred<NeptunOnlinePlayer?> = withContext(Dispatchers.IO) {
        val currentTime = System.currentTimeMillis()

        suspendedTransactionAsync {
            val existingOfflinePlayer = OfflinePlayerEntity.find { OfflinePlayerTable.uuid eq uuid }.firstOrNull()

            if (existingOfflinePlayer == null) {
                null
            } else {
                val offlinePlayer = NeptunOfflinePlayerImpl(
                    uuid,
                    existingOfflinePlayer.name,
                    existingOfflinePlayer.firstLoginTimestamp,
                    existingOfflinePlayer.lastLoginTimestamp,
                    existingOfflinePlayer.lastLogoutTimestamp,
                    existingOfflinePlayer.onlineTime,
                    SkinProfileImpl(existingOfflinePlayer.skinValue, existingOfflinePlayer.skinSignature),
                    existingOfflinePlayer.crystals,
                    existingOfflinePlayer.shards,
                )

                val onlinePlayer = NeptunOnlinePlayerImpl.create(offlinePlayer, currentProxyName, "-")

                onlinePlayer.name = name
                onlinePlayer.lastLoginTimestamp = currentTime
                onlinePlayer.skinProfile.value = skinValue
                onlinePlayer.skinProfile.signature = skinSignature
                bulkUpdateEntry(NeptunOfflinePlayer.Update.ALL, uuid, onlinePlayer, false)

                cacheOnlinePlayer(uuid, onlinePlayer)
                onlinePlayerRepository.insert(uuid, onlinePlayer)

                onlinePlayer
            }
        }
    }

    override suspend fun unloadOnlinePlayer(uuid: UUID) {
        this.onlinePlayerRepository.delete(uuid)
        this.onlinePlayerCache.delete(uuid)
    }

    override suspend fun cacheOnlinePlayer(uuid: UUID, onlinePlayer: NeptunOnlinePlayer) {
        this.onlinePlayerCache.insert(uuid, onlinePlayer)
    }

    override suspend fun removeCachedOnlinePlayer(uuid: UUID) {
        this.onlinePlayerCache.delete(uuid)
    }

    override suspend fun bulkUpdateEntry(updateType: NeptunOfflinePlayer.Update, key: UUID, newValue: Any, updateCache: Boolean, result: (Unit) -> Unit) {
        newSuspendedTransaction(Dispatchers.IO) {
            val existingOfflinePlayer = OfflinePlayerEntity.find { OfflinePlayerTable.uuid eq key }.firstOrNull()

            when (updateType) {
                NeptunOfflinePlayer.Update.ALL -> {
                    if (newValue !is NeptunOfflinePlayer)
                        throw UnsupportedOperationException("'newValue' has to be an NeptunOfflinePlayer instance!")

                    existingOfflinePlayer?.apply {
                        this.name = newValue.name
                        this.firstLoginTimestamp = newValue.firstLoginTimestamp
                        this.lastLoginTimestamp = newValue.lastLoginTimestamp
                        this.lastLogoutTimestamp = newValue.lastLogoutTimestamp
                        this.onlineTime = newValue.onlineTime
                        this.skinValue = newValue.skinProfile.value
                        this.skinSignature = newValue.skinProfile.signature
                        this.crystals = newValue.crystals
                        this.shards = newValue.shards
                    }
                }

                NeptunOfflinePlayer.Update.NAME -> {
                    existingOfflinePlayer?.apply {
                        this.name = newValue as String
                    }
                }

                NeptunOfflinePlayer.Update.LAST_LOGIN_TIMESTAMP -> {
                    existingOfflinePlayer?.apply {
                        this.lastLoginTimestamp = newValue as Long
                    }
                }

                NeptunOfflinePlayer.Update.LAST_LOGOUT_TIMESTAMP -> {
                    existingOfflinePlayer?.apply {
                        this.lastLogoutTimestamp = newValue as Long
                    }
                }

                NeptunOfflinePlayer.Update.ONLINE_TIME -> {
                    existingOfflinePlayer?.apply {
                        this.onlineTime = newValue as Long
                    }
                }

                NeptunOfflinePlayer.Update.SKIN_VALUE -> {
                    existingOfflinePlayer?.apply {
                        this.skinValue = newValue as String
                    }
                }

                NeptunOfflinePlayer.Update.SKIN_SIGNATURE -> {
                    existingOfflinePlayer?.apply {
                        this.skinSignature = newValue as String
                    }
                }

                NeptunOfflinePlayer.Update.CRYSTALS -> {
                    existingOfflinePlayer?.apply {
                        this.crystals = newValue as Long
                    }
                }

                NeptunOfflinePlayer.Update.SHARDS -> {
                    existingOfflinePlayer?.apply {
                        this.shards = newValue as Long
                    }
                }

                NeptunOfflinePlayer.Update.CURRENT_SERVICE -> TODO("Unimplemented because offline players have no current service!")
            }

            commit()
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