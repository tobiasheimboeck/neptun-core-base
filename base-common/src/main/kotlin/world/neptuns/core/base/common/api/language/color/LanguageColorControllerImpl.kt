package world.neptuns.core.base.common.api.language.color

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.color.LanguageColor
import world.neptuns.core.base.api.language.color.LanguageColorController
import world.neptuns.core.base.api.player.NeptunOfflinePlayer
import world.neptuns.core.base.common.repository.color.LanguageColorCache
import world.neptuns.core.base.common.repository.color.LanguageColorRepository
import world.neptuns.core.base.common.repository.color.LanguageColorTable
import java.util.*

class LanguageColorControllerImpl : LanguageColorController {

    //TODO: Class impl rework
    private val languageColorRepository = NeptunCoreProvider.api.repositoryLoader.get(LanguageColorRepository::class.java)!!
    private val languageColorCache = NeptunCoreProvider.api.cacheLoader.get(LanguageColorCache::class.java)!!

    override suspend fun getColor(uuid: UUID, name: LineKey): LanguageColor? {
        return this.languageColorCache.getValue(uuid) { it.name == name }
            ?: this.languageColorRepository.getValue(uuid) { it.name == name }.await()
    }

    override suspend fun getColors(uuid: UUID): List<LanguageColor> {
        return if (this.languageColorCache.contains(uuid))
            this.languageColorCache.getValues(uuid)
        else
            this.languageColorRepository.getValues(uuid).await()
    }

    override suspend fun buyOrSelectColor(offlinePlayer: NeptunOfflinePlayer, languageColor: LanguageColor) {
        val transactionStatus = languageColor.buy(offlinePlayer)
        if (!transactionStatus) return

        newSuspendedTransaction {
            LanguageColorTable.insert {
                it[this.uuid] = uuid
                it[this.name] = name
                it[this.hexFormat] = hexFormat
            }
        }

        this.languageColorRepository.insert(offlinePlayer.uuid, languageColor)
    }

    override suspend fun loadColors(uuid: UUID) {

    }

    override suspend fun unloadColors(uuid: UUID) {

    }

    override suspend fun load(key: UUID, value: List<LanguageColor>) {
        for (color in value) {
            this.languageColorCache.insert(key, color)
        }
    }

    override suspend fun unload(key: UUID) {
        this.languageColorCache.deleteAll(key)
    }

}