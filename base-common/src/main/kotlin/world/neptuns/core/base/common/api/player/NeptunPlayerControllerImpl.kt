package world.neptuns.core.base.common.api.player

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.kyori.adventure.text.format.TextColor
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import world.neptuns.controller.api.service.NeptunService
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.core.base.api.packet.NeptunPlayerDataLoadedPacket
import world.neptuns.core.base.api.player.NeptunOfflinePlayer
import world.neptuns.core.base.api.player.NeptunOnlinePlayer
import world.neptuns.core.base.api.player.NeptunPlayerController
import world.neptuns.core.base.common.api.language.properties.LanguagePropertiesImpl
import world.neptuns.core.base.common.api.skin.SkinProfileImpl
import world.neptuns.core.base.common.repository.language.LanguagePropertiesRepository
import world.neptuns.core.base.common.repository.language.LanguagePropertiesTable
import world.neptuns.core.base.common.repository.player.OfflinePlayerTable
import world.neptuns.core.base.common.repository.player.OnlinePlayerCache
import world.neptuns.core.base.common.repository.player.OnlinePlayerRepository
import world.neptuns.streamline.api.NeptunStreamlineProvider
import java.util.*

class NeptunPlayerControllerImpl(override val updateChannel: String) : NeptunPlayerController {

    private val onlinePlayerRepository = NeptunStreamlineProvider.api.repositoryLoader.get(OnlinePlayerRepository::class.java)!!
    private val onlinePlayerCache = NeptunStreamlineProvider.api.cacheLoader.get(OnlinePlayerCache::class.java)!!

    private val languagePropertiesRepository = NeptunStreamlineProvider.api.repositoryLoader.get(LanguagePropertiesRepository::class.java)!!

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
            constructOfflinePlayer(OfflinePlayerTable.selectAll().where { OfflinePlayerTable.uuid eq uuid }.limit(1).firstOrNull(), null)
        }
    }

    override suspend fun getOfflinePlayersAsync(): Deferred<List<NeptunOfflinePlayer>> {
        return suspendedTransactionAsync(Dispatchers.IO) {
            OfflinePlayerTable.selectAll().toList().map { constructOfflinePlayer(it, null)!! }
        }
    }

    override suspend fun getGloballyRegisteredPlayersAmount(): Deferred<Int> {
        return suspendedTransactionAsync(Dispatchers.IO) {
            OfflinePlayerTable.selectAll().toList().size
        }
    }

    override suspend fun loadPlayer(uuid: UUID, name: String, skinValue: String, skinSignature: String, proxyServiceName: String, minecraftServiceName: String) {
        newSuspendedTransaction(Dispatchers.IO) {
            val resultRow = OfflinePlayerTable.selectAll().where { OfflinePlayerTable.uuid eq uuid }.limit(1).firstOrNull()

            val offlinePlayer: NeptunOfflinePlayer
            val languageProperties: LanguageProperties

            if (resultRow == null) {
                offlinePlayer = NeptunOfflinePlayerImpl.create(uuid, name, skinValue, skinValue)
                OfflinePlayerTable.insert {
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

                languageProperties = LanguagePropertiesImpl.createDefaultProperties(uuid)
                LanguagePropertiesTable.insert {
                    it[this.uuid] = offlinePlayer.uuid
                    it[this.languageKey] = languageProperties.langKey.asString()
                    it[this.primaryColor] = languageProperties.primaryColor.asHexString()
                    it[this.secondaryColor] = languageProperties.secondaryColor.asHexString()
                    it[this.separatorColor] = languageProperties.separatorColor.asHexString()
                }
            } else {
                val usernameChanged = resultRow[OfflinePlayerTable.name] != name
                val username = if (usernameChanged) name else resultRow[OfflinePlayerTable.name]

                offlinePlayer = constructOfflinePlayer(resultRow, username)!!
                if (usernameChanged) bulkUpdateEntry(NeptunOfflinePlayer.Update.NAME, uuid, username, false)

                val resultRowLanguageProperties = LanguagePropertiesTable.selectAll().where { LanguagePropertiesTable.uuid eq uuid }.limit(1).firstOrNull()
                languageProperties = constructLanguageProperties(resultRowLanguageProperties)!!
            }

            val neptunOnlinePlayer = NeptunOnlinePlayerImpl.create(offlinePlayer, proxyServiceName, minecraftServiceName)
            val playerResult = onlinePlayerRepository.insert(uuid, neptunOnlinePlayer)
            addToLocalCache(uuid, neptunOnlinePlayer)

            val propertiesResult = languagePropertiesRepository.insert(uuid, languageProperties)
            NeptunCoreProvider.api.languagePropertiesController.addToLocalCache(uuid, languageProperties)

            if (playerResult.await() && propertiesResult.await()) {
                NeptunStreamlineProvider.api.packetController.sendPacket(NeptunPlayerDataLoadedPacket(neptunOnlinePlayer.uuid))
            }
        }
    }

    override suspend fun unloadPlayer(uuid: UUID) {
        withContext(Dispatchers.IO) {
            val offlinePlayer = getOfflinePlayerAsync(uuid).await() ?: return@withContext
            offlinePlayer.updateOnlineTime()

            transaction {
                OfflinePlayerTable.update({ OfflinePlayerTable.uuid eq uuid }) {
                    it[onlineTime] = offlinePlayer.onlineTime
                }
            }

            onlinePlayerRepository.delete(uuid)
            removeFromLocalCache(uuid)

            languagePropertiesRepository.delete(uuid)
            NeptunCoreProvider.api.languagePropertiesController.removeFromLocalCache(uuid)
        }
    }

    override suspend fun addToLocalCache(key: UUID, value: NeptunOnlinePlayer) {
        this.onlinePlayerCache.insert(key, value)
    }

    override suspend fun removeFromLocalCache(key: UUID) {
        this.onlinePlayerCache.delete(key)
    }

    override suspend fun bulkUpdateEntry(updateType: NeptunOfflinePlayer.Update, key: UUID, newValue: Any, updateCache: Boolean, result: (Unit) -> Unit) {
        newSuspendedTransaction(Dispatchers.IO) {
            OfflinePlayerTable.update({ OfflinePlayerTable.uuid eq key }) {
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

        if (onlinePlayerRepository.update(key, onlinePlayer).await()) {
            sendUpdatePacket(updateType, key, newValue)
        }
    }

    private fun constructLanguageProperties(resultRow: ResultRow?): LanguageProperties? {
        return if (resultRow != null) {
            LanguagePropertiesImpl(
                resultRow[LanguagePropertiesTable.uuid],
                LangKey.fromString(resultRow[LanguagePropertiesTable.languageKey]),
                TextColor.fromHexString(resultRow[LanguagePropertiesTable.primaryColor])!!,
                TextColor.fromHexString(resultRow[LanguagePropertiesTable.secondaryColor])!!,
                TextColor.fromHexString(resultRow[LanguagePropertiesTable.separatorColor])!!
            )
        } else {
            null
        }
    }

    private fun constructOfflinePlayer(resultRow: ResultRow?, name: String?): NeptunOfflinePlayer? {
        return if (resultRow != null) {
            NeptunOfflinePlayerImpl(
                resultRow[OfflinePlayerTable.uuid],
                name ?: resultRow[OfflinePlayerTable.name],
                resultRow[OfflinePlayerTable.firstLoginTimestamp],
                resultRow[OfflinePlayerTable.lastLoginTimestamp],
                resultRow[OfflinePlayerTable.lastLogoutTimestamp],
                resultRow[OfflinePlayerTable.onlineTime],
                SkinProfileImpl(resultRow[OfflinePlayerTable.skinValue], resultRow[OfflinePlayerTable.skinSignature]),
                resultRow[OfflinePlayerTable.crystals],
                resultRow[OfflinePlayerTable.shards],
            )
        } else {
            null
        }
    }

}