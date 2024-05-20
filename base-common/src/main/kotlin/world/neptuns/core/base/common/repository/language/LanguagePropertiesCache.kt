package world.neptuns.core.base.common.repository.language

import world.neptuns.core.base.api.cache.impl.MapCache
import world.neptuns.core.base.api.language.properties.LanguageProperties
import java.util.*

class LanguagePropertiesCache : MapCache<UUID, LanguageProperties>()