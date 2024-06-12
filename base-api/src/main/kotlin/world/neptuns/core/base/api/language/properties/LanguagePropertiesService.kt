package world.neptuns.core.base.api.language.properties

import world.neptuns.streamline.api.utils.Controller
import world.neptuns.streamline.api.utils.Updatable
import java.util.*

interface LanguagePropertiesService : Controller<UUID, LanguageProperties>, Updatable<UUID, LanguageProperties.Update, Any> {

    suspend fun getProperties(uuid: UUID): LanguageProperties?

}