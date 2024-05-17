package world.neptuns.core.base.api.language.properties

import net.kyori.adventure.text.format.TextColor
import world.neptuns.core.base.api.language.LanguageKey
import java.util.*

interface LanguageProperties {

    val uuid: UUID
    var languageKey: LanguageKey

    var primaryColor: TextColor
    var secondaryColor: TextColor
    var separatorColor: TextColor

    enum class Update {
        ALL,
        LANGUAGE_KEY,
        PRIMARY_COLOR,
        SECONDARY_COLOR,
        SEPARATOR_COLOR;
    }

}