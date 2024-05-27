package world.neptuns.core.base.api.language.color

import world.neptuns.core.base.api.language.LineKey
import world.neptuns.streamline.api.utils.Controller
import java.util.*

interface LanguageColorController : Controller<UUID, LanguageColor> {

    suspend fun getColor(uuid: UUID, name: LineKey): LanguageColor?
    suspend fun getColors(uuid: UUID): List<LanguageColor>

}