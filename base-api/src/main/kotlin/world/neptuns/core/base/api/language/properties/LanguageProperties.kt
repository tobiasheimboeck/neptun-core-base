package world.neptuns.core.base.api.language.properties

import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.color.LanguageColor
import java.io.Serializable
import java.util.*

interface LanguageProperties : Serializable {

    val uuid: UUID
    var langKey: LangKey

    var primaryColor: LanguageColor
    var secondaryColor: LanguageColor
    var separatorColor: LanguageColor

    enum class Update {
        ALL,
        LANGUAGE_KEY,
        PRIMARY_COLOR,
        SECONDARY_COLOR,
        SEPARATOR_COLOR;
    }

}