package world.neptuns.core.base.api.language.properties

import world.neptuns.controller.api.utils.Updatable
import world.neptuns.core.base.api.cache.LocalCacheFunctions
import java.util.*

interface LanguagePropertiesController : LocalCacheFunctions<UUID, LanguageProperties>, Updatable<UUID, LanguageProperties.Update, Any> {

    suspend fun getProperties(uuid: UUID): LanguageProperties?

}