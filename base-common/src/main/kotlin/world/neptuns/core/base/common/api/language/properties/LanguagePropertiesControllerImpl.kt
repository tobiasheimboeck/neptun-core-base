package world.neptuns.core.base.common.api.language.properties

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.kyori.adventure.text.format.TextColor
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.core.base.api.language.properties.LanguagePropertiesController
import world.neptuns.core.base.common.repository.language.LanguagePropertiesCache
import world.neptuns.core.base.common.repository.language.LanguagePropertiesRepository
import world.neptuns.core.base.common.repository.language.LanguagePropertiesTable
import java.util.*

class LanguagePropertiesControllerImpl : LanguagePropertiesController {

    private val languagePropertiesRepository = NeptunCoreProvider.api.repositoryLoader.get(LanguagePropertiesRepository::class.java)!!
    private val languagePropertiesCache = NeptunCoreProvider.api.cacheLoader.get(LanguagePropertiesCache::class.java)!!

    override suspend fun getProperties(uuid: UUID): LanguageProperties? {
        return this.languagePropertiesCache.get(uuid) ?: this.languagePropertiesRepository.get(uuid).await()
    }

    override suspend fun load(key: UUID, value: LanguageProperties) {
        this.languagePropertiesCache.insert(key, value)
    }

    override suspend fun unload(key: UUID) {
        this.languagePropertiesCache.delete(key)
    }

    override suspend fun bulkUpdateEntry(key: UUID, updateType: LanguageProperties.Update, newValue: Any, updateCache: Boolean, result: (Unit) -> Unit) {
        newSuspendedTransaction(Dispatchers.IO) {
            LanguagePropertiesTable.update({ LanguagePropertiesTable.uuid eq key }) {
                when (updateType) {
                    LanguageProperties.Update.ALL -> {
                        if (newValue !is LanguageProperties)
                            throw UnsupportedOperationException("Object has to be an LanguageProperties instance!")

                        it[languageKey] = newValue.langKey.asString()
                        it[primaryColor] = newValue.primaryColor.asHexString()
                        it[secondaryColor] = newValue.secondaryColor.asHexString()
                        it[separatorColor] = newValue.separatorColor.asHexString()
                    }

                    LanguageProperties.Update.LANGUAGE_KEY -> it[languageKey] = newValue as String
                    LanguageProperties.Update.PRIMARY_COLOR -> it[primaryColor] = newValue as String
                    LanguageProperties.Update.SECONDARY_COLOR -> it[secondaryColor] = newValue as String
                    LanguageProperties.Update.SEPARATOR_COLOR -> it[separatorColor] = newValue as String
                }
            }
        }

        if (updateCache) updateCachedEntry(key, updateType, newValue, result)
    }

    override suspend fun updateCachedEntry(key: UUID, updateType: LanguageProperties.Update, newValue: Any, result: (Unit) -> Unit) {
        withContext(Dispatchers.IO) {
            val languageProperties = getProperties(key) ?: return@withContext

            when (updateType) {
                LanguageProperties.Update.ALL -> {
                    if (newValue !is LanguageProperties)
                        throw UnsupportedOperationException("Object has to be an LanguageProperties instance!")

                    languageProperties.langKey = newValue.langKey
                    languageProperties.primaryColor = newValue.primaryColor
                    languageProperties.secondaryColor = newValue.secondaryColor
                    languageProperties.separatorColor = newValue.separatorColor
                }

                LanguageProperties.Update.LANGUAGE_KEY -> languageProperties.langKey = newValue as LangKey
                LanguageProperties.Update.PRIMARY_COLOR -> languageProperties.primaryColor = newValue as TextColor
                LanguageProperties.Update.SECONDARY_COLOR -> languageProperties.secondaryColor = newValue as TextColor
                LanguageProperties.Update.SEPARATOR_COLOR -> languageProperties.separatorColor = newValue as TextColor
            }

            languagePropertiesRepository.update(key, languageProperties)
            TODO("Send update packet to service where an instance of LanguageProperties with this uuid is cached")
        }
    }

}