package world.neptuns.core.base.api.language.color

import world.neptuns.core.base.api.language.LineKey
import world.neptuns.streamline.api.cache.container.Cachable
import java.util.*

interface LanguageColorController : Cachable<UUID, List<LanguageColor>> {

    suspend fun getColor(uuid: UUID, name: LineKey): LanguageColor?
    suspend fun getColors(uuid: UUID): List<LanguageColor>

    suspend fun loadColors(uuid: UUID)
    suspend fun unloadColors(uuid: UUID)

}