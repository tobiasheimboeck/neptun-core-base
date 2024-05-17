package world.neptuns.core.base.api.language.properties

import kotlinx.coroutines.Deferred
import world.neptuns.controller.api.utils.Updatable
import java.util.*

interface LanguagePropertiesController : Updatable<UUID, LanguageProperties.Update, Any> {

    suspend fun getPropertiesAsync(uuid: UUID): Deferred<LanguageProperties?>

}