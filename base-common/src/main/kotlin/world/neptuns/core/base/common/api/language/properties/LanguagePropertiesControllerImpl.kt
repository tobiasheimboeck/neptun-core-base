package world.neptuns.core.base.common.api.language.properties

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import net.kyori.adventure.text.format.TextColor
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.update
import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.core.base.api.language.properties.LanguagePropertiesController
import world.neptuns.core.base.common.repository.language.LanguagePropertiesCache
import world.neptuns.core.base.common.repository.language.LanguagePropertiesRepository
import world.neptuns.core.base.common.repository.language.LanguagePropertiesTable
import world.neptuns.streamline.api.NeptunStreamlineProvider
import java.util.*

class LanguagePropertiesControllerImpl(override val updateChannel: String) : LanguagePropertiesController {

    private val languagePropertiesRepository = NeptunStreamlineProvider.api.repositoryLoader.get(LanguagePropertiesRepository::class.java)!!
    private val languagePropertiesCache = NeptunStreamlineProvider.api.cacheLoader.get(LanguagePropertiesCache::class.java)!!

    override suspend fun getProperties(uuid: UUID): LanguageProperties? {
        return this.languagePropertiesCache.get(uuid) ?: this.languagePropertiesRepository.get(uuid).await()
    }

    override suspend fun createOrLoadEntry(key: UUID, defaultValue: LanguageProperties?, vararg data: Any): Deferred<Boolean> {
        return suspendedTransactionAsync {
            val resultRow = LanguagePropertiesTable.selectAll().where { LanguagePropertiesTable.uuid eq key }.limit(1).firstOrNull()
            val loadedProperties: LanguageProperties

            if (resultRow == null && defaultValue != null ){
                LanguagePropertiesTable.insert {
                    it[this.uuid] = uuid
                    it[this.languageKey] = defaultValue.langKey.asString()
                    it[this.primaryColor] = defaultValue.primaryColor.asHexString()
                    it[this.secondaryColor] = defaultValue.secondaryColor.asHexString()
                    it[this.separatorColor] = defaultValue.separatorColor.asHexString()
                }

                loadedProperties = defaultValue
            } else {
                loadedProperties = constructEntry(resultRow!!)
            }

            cacheEntry(key, loadedProperties)
            languagePropertiesRepository.insert(key, loadedProperties).await()
        }
    }

    override suspend fun unloadEntry(key: UUID) {
        this.languagePropertiesRepository.delete(key)
        this.languagePropertiesCache.delete(key)
    }

    override fun constructEntry(resultRow: ResultRow): LanguageProperties {
        return LanguagePropertiesImpl(
            resultRow[LanguagePropertiesTable.uuid],
            LangKey.fromString(resultRow[LanguagePropertiesTable.languageKey]),
            TextColor.fromHexString(resultRow[LanguagePropertiesTable.primaryColor])!!,
            TextColor.fromHexString(resultRow[LanguagePropertiesTable.secondaryColor])!!,
            TextColor.fromHexString(resultRow[LanguagePropertiesTable.separatorColor])!!
        )
    }

    override fun cacheEntry(key: UUID, value: LanguageProperties) {
        this.languagePropertiesCache.insert(key, value)
    }

    override fun removeEntryFromCache(key: UUID) {
        this.languagePropertiesCache.delete(key)
    }

    override suspend fun bulkUpdateEntry(updateType: LanguageProperties.Update, key: UUID, newValue: Any, updateCache: Boolean, result: (Unit) -> Unit) {
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
            LanguageProperties.Update.PRIMARY_COLOR -> languageProperties.primaryColor = newValue as TextColor
            LanguageProperties.Update.SECONDARY_COLOR -> languageProperties.secondaryColor = newValue as TextColor
            LanguageProperties.Update.SEPARATOR_COLOR -> languageProperties.separatorColor = newValue as TextColor
        }

        if (languagePropertiesRepository.update(key, languageProperties).await()) {
            sendUpdatePacket(updateType, key, newValue)
        }
    }

}