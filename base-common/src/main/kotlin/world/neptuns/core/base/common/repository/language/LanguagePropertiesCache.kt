package world.neptuns.core.base.common.repository.language

import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.streamline.api.cache.MapCache
import java.util.*

class LanguagePropertiesCache : MapCache<UUID, LanguageProperties>()