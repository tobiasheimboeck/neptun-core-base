package world.neptuns.core.base.common.api.language.color

import kotlinx.coroutines.Deferred
import org.jetbrains.exposed.sql.ResultRow
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.color.LanguageColor
import world.neptuns.core.base.api.language.color.LanguageColorController
import world.neptuns.core.base.api.language.color.LanguageColorRegistry
import world.neptuns.core.base.common.repository.color.LanguageColorCache
import world.neptuns.core.base.common.repository.color.LanguageColorRepository
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

    override suspend fun createOrLoadEntry(key: UUID, defaultValue: LanguageColor?, vararg data: Any): Deferred<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun unloadEntry(key: UUID) {
        TODO("Not yet implemented")
    }

    override fun constructEntry(resultRow: ResultRow): LanguageColor {
        TODO("Not yet implemented")
    }

    override fun cacheEntry(key: UUID, value: LanguageColor) {
        TODO("Not yet implemented")
    }

    override fun removeEntryFromCache(key: UUID) {
        TODO("Not yet implemented")
    }

    private fun getLanguageColor(lineKey: LineKey, hexFormat: String): LanguageColor {
        return LanguageColorRegistry.Default.elements.find { it.name.asString() == lineKey.asString() }
            ?: LanguageColorRegistry.Official.elements.find { it.name.asString() == lineKey.asString() }
            ?: LanguageColorRegistry.Custom.create(lineKey, null, hexFormat, 0L)
    }

}