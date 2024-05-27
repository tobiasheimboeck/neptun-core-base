package world.neptuns.core.base.api.language.properties

import world.neptuns.streamline.api.cache.container.Cachable
import world.neptuns.streamline.api.utils.Updatable
import java.util.*

interface LanguagePropertiesController : Cachable<UUID, LanguageProperties>, Updatable<UUID, LanguageProperties.Update, Any> {

    suspend fun getProperties(uuid: UUID): LanguageProperties?

}