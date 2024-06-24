package world.neptuns.core.base.api.language.properties

import kotlinx.coroutines.Deferred
import world.neptuns.streamline.api.utils.Updatable
import java.util.*

interface LanguagePropertiesService : Updatable<UUID, LanguageProperties.Update, Any> {

    suspend fun getProperties(uuid: UUID): LanguageProperties?

    suspend fun createLanguageProperties(uuid: UUID)

    suspend fun loadLanguageProperties(uuid: UUID): Deferred<LanguageProperties?>

    suspend fun unloadLanguageProperties(uuid: UUID)

    suspend fun cacheLanguageProperties(uuid: UUID, languageProperties: LanguageProperties)

    suspend fun removeLanguageProperties(uuid: UUID)

}