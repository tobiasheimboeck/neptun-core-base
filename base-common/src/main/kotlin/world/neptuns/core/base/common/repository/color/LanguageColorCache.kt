package world.neptuns.core.base.common.repository.color

import world.neptuns.core.base.api.language.color.LanguageColor
import world.neptuns.streamline.api.cache.MultimapCache
import java.util.*

class LanguageColorCache : MultimapCache<UUID, LanguageColor>()