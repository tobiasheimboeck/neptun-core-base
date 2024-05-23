package world.neptuns.core.base.api.language.color

import world.neptuns.core.base.api.cache.LocalCacheFunctions
import world.neptuns.core.base.api.language.LineKey
import java.util.*

interface LanguageColorController : LocalCacheFunctions<UUID, List<LanguageColor>> {

    suspend fun getColor(uuid: UUID, name: LineKey): LanguageColor?
    suspend fun getColors(uuid: UUID): List<LanguageColor>

    suspend fun loadColors(uuid: UUID)
    suspend fun unloadColors(uuid: UUID)

}