package world.neptuns.core.base.common.api.language.color

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.color.LanguageColor
import world.neptuns.core.base.api.language.color.LanguageColorController
import world.neptuns.core.base.api.language.color.LanguageColorRegistry
import world.neptuns.core.base.common.repository.color.LanguageColorCache
import world.neptuns.core.base.common.repository.color.LanguageColorRepository
import world.neptuns.core.base.common.repository.color.LanguageColorTable
import world.neptuns.streamline.api.NeptunStreamlineProvider
import java.util.*

class LanguageColorControllerImpl : LanguageColorController {

    private val languageColorRepository = NeptunStreamlineProvider.api.repositoryLoader.get(LanguageColorRepository::class.java)!!
    private val languageColorCache = NeptunStreamlineProvider.api.cacheLoader.get(LanguageColorCache::class.java)!!

    override suspend fun getColor(uuid: UUID, name: LineKey): LanguageColor? {
        return this.languageColorCache.getValue(uuid) { it.name == name }
            ?: this.languageColorRepository.getValue(uuid) { it.name == name }.await()
    }

    override suspend fun getColors(uuid: UUID): List<LanguageColor> {
        return if (this.languageColorCache.contains(uuid)) this.languageColorCache.getValues(uuid)
        else this.languageColorRepository.getValues(uuid).await()
    }

    override suspend fun loadColors(uuid: UUID) {
        newSuspendedTransaction(Dispatchers.IO) {
            for (resultRow in LanguageColorTable.selectAll().where { LanguageColorTable.uuid eq uuid }.toList()) {
                val languageColor = getLanguageColor(
                    LineKey.Companion.key(resultRow[LanguageColorTable.name]),
                    resultRow[LanguageColorTable.hexFormat]
                )

                languageColorRepository.insert(uuid, languageColor)
                languageColorCache.insert(uuid, languageColor)
            }
        }
    }

    override suspend fun unloadColors(uuid: UUID) {

    }

    override suspend fun addToLocalCache(key: UUID, value: List<LanguageColor>) {
        for (color in value) {
            this.languageColorCache.insert(key, color)
        }
    }

    override suspend fun removeFromLocalCache(key: UUID) {
        this.languageColorCache.deleteAll(key)
    }

    private fun getLanguageColor(lineKey: LineKey, hexFormat: String): LanguageColor {
        return LanguageColorRegistry.Default.elements.find { it.name.asString() == lineKey.asString() }
            ?: LanguageColorRegistry.Official.elements.find { it.name.asString() == lineKey.asString() }
            ?: LanguageColorRegistry.Custom.create(lineKey, null, hexFormat, 0L)
    }

}