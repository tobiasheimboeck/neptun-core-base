package world.neptuns.core.base.common.api.language.properties

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import world.neptuns.core.base.api.language.LanguageKey
import world.neptuns.core.base.api.language.properties.LanguageProperties
import java.util.*

class LanguagePropertiesImpl(
    override val uuid: UUID,
    override var languageKey: LanguageKey,
    override var primaryColor: TextColor,
    override var secondaryColor: TextColor,
    override var separatorColor: TextColor,
) : LanguageProperties {

    companion object {
        fun createDefaultProperties(uuid: UUID): LanguageProperties {
            return LanguagePropertiesImpl(uuid, LanguageKey.defaultKey(), NamedTextColor.DARK_AQUA, NamedTextColor.WHITE, NamedTextColor.GRAY)
        }
    }

}