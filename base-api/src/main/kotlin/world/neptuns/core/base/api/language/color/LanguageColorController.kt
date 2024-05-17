package world.neptuns.core.base.api.language.color

import kotlinx.coroutines.Deferred
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.player.NeptunOfflinePlayer
import java.util.*

interface LanguageColorController {

    suspend fun getColorAsync(uuid: UUID, name: LineKey): Deferred<LanguageColor?>
    suspend fun getColorsAsync(uuid: UUID): Deferred<List<LanguageColor>>

    suspend fun buyOrSelectColor(offlinePlayer: NeptunOfflinePlayer, languageColor: LanguageColor)

    suspend fun loadColors(uuid: UUID)
    suspend fun unloadColors(uuid: UUID)

}