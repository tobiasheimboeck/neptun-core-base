package world.neptuns.core.base.common.api.language.properties

import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.update
import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.color.LanguageColor
import world.neptuns.core.base.api.language.color.LanguageColorRegistry
import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.core.base.api.language.properties.LanguagePropertiesService
import world.neptuns.core.base.common.database.properties.LanguagePropertiesTable
import world.neptuns.core.base.common.database.properties.dao.LanguagePropertiesEntity
import world.neptuns.core.base.common.repository.language.LanguagePropertiesCache
import world.neptuns.core.base.common.repository.language.LanguagePropertiesRepository
import world.neptuns.streamline.api.NeptunStreamlineProvider
import java.util.*

@Suppress("OPT_IN_USAGE")
class LanguagePropertiesServiceImpl : LanguagePropertiesService {

    private val languagePropertiesRepository = NeptunStreamlineProvider.api.repositoryLoader.get(LanguagePropertiesRepository::class.java)!!
    private val languagePropertiesCache = NeptunStreamlineProvider.api.cacheLoader.get(LanguagePropertiesCache::class.java)!!

    init {
        GlobalScope.launch(Dispatchers.IO) {
            languagePropertiesRepository.onUpdate { uuid, languageProperties ->
                if (!languagePropertiesCache.contains(uuid)) return@onUpdate
                languagePropertiesCache.update(uuid, languageProperties)
            }
        }
    }

    override suspend fun getProperties(uuid: UUID): LanguageProperties? {
        return this.languagePropertiesCache.get(uuid) ?: this.languagePropertiesRepository.get(uuid).await()
    }

    override suspend fun createLanguageProperties(uuid: UUID) {
        val langKey = LangKey.defaultKey()
        val primaryColor = LanguageColorRegistry.Default.GOLD
        val secondaryColor = LanguageColorRegistry.Default.WHITE
        val separatorColor = LanguageColorRegistry.Default.GRAY

        newSuspendedTransaction {
            LanguagePropertiesEntity.new {
                this.uuid = uuid
                this.languageKey = langKey.asString()
                this.primaryColor = primaryColor.hexFormat
                this.secondaryColor = secondaryColor.hexFormat
                this.separatorColor = separatorColor.hexFormat
            }
        }

        val languageProperties = LanguagePropertiesImpl(
            uuid,
            langKey,
            primaryColor,
            secondaryColor,
            separatorColor
        )

        cacheLanguageProperties(uuid, languageProperties)
        this.languagePropertiesRepository.insert(uuid, languageProperties)
    }

    override suspend fun loadLanguageProperties(uuid: UUID): Deferred<LanguageProperties?> = withContext(Dispatchers.IO) {
        suspendedTransactionAsync {
            val existingLanguageProperties = LanguagePropertiesEntity.find { LanguagePropertiesTable.uuid eq uuid }.firstOrNull()

            if (existingLanguageProperties == null) {
                null
            } else {
                val languageProperties = LanguagePropertiesImpl(
                    uuid,
                    LangKey.fromString(existingLanguageProperties.languageKey),
                    LanguageColorRegistry.Default.of(existingLanguageProperties.primaryColor)!!,
                    LanguageColorRegistry.Default.of(existingLanguageProperties.secondaryColor)!!,
                    LanguageColorRegistry.Default.of(existingLanguageProperties.separatorColor)!!
                )

                cacheLanguageProperties(uuid, languageProperties)
                languagePropertiesRepository.insert(uuid, languageProperties)

                languageProperties
            }
        }
    }

    override suspend fun unloadLanguageProperties(uuid: UUID) {
        this.languagePropertiesRepository.delete(uuid)
        this.languagePropertiesCache.delete(uuid)
    }

    override suspend fun cacheLanguageProperties(uuid: UUID, languageProperties: LanguageProperties) {
        this.languagePropertiesCache.insert(uuid, languageProperties)
    }

    override suspend fun removeLanguageProperties(uuid: UUID) {
        this.languagePropertiesCache.delete(uuid)
    }

    override suspend fun bulkUpdateEntry(updateType: LanguageProperties.Update, key: UUID, newValue: Any, updateCache: Boolean, result: (Unit) -> Unit) {
        newSuspendedTransaction(Dispatchers.IO) {
            LanguagePropertiesTable.update({ LanguagePropertiesTable.uuid eq key }) {
                when (updateType) {
                    LanguageProperties.Update.ALL -> {
                        if (newValue !is LanguageProperties)
                            throw UnsupportedOperationException("Object has to be an LanguageProperties instance!")

                        it[languageKey] = newValue.langKey.asString()
                        it[primaryColor] = newValue.primaryColor.hexFormat
                        it[secondaryColor] = newValue.secondaryColor.hexFormat
                        it[separatorColor] = newValue.separatorColor.hexFormat
                    }

                    LanguageProperties.Update.LANGUAGE_KEY -> it[languageKey] = newValue as String
                    LanguageProperties.Update.PRIMARY_COLOR -> it[primaryColor] = newValue as String
                    LanguageProperties.Update.SECONDARY_COLOR -> it[secondaryColor] = newValue as String
                    LanguageProperties.Update.SEPARATOR_COLOR -> it[separatorColor] = newValue as String
                }
            }
        }

        if (updateCache) updateCachedEntry(updateType, key, newValue, result)
    }

    override suspend fun updateCachedEntry(updateType: LanguageProperties.Update, key: UUID, newValue: Any, result: (Unit) -> Unit) {
        val languageProperties = getProperties(key) ?: return

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
            LanguageProperties.Update.PRIMARY_COLOR -> languageProperties.primaryColor = newValue as LanguageColor
            LanguageProperties.Update.SECONDARY_COLOR -> languageProperties.secondaryColor = newValue as LanguageColor
            LanguageProperties.Update.SEPARATOR_COLOR -> languageProperties.separatorColor = newValue as LanguageColor
        }

        languagePropertiesRepository.update(key, languageProperties)
    }

}