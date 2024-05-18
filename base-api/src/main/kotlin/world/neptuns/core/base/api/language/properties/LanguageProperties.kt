package world.neptuns.core.base.api.language.properties

import net.kyori.adventure.text.format.TextColor
import world.neptuns.core.base.api.language.LangKey
import java.io.Serializable
import java.util.*

interface LanguageProperties : Serializable {

    val uuid: UUID
    var langKey: LangKey

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