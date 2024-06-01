package world.neptuns.core.base.common.api.language.properties

import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.color.LanguageColor
import world.neptuns.core.base.api.language.color.LanguageColorRegistry
import world.neptuns.core.base.api.language.properties.LanguageProperties
import java.util.*

class LanguagePropertiesImpl(
    override val uuid: UUID,
    override var langKey: LangKey,
    override var primaryColor: LanguageColor,
    override var secondaryColor: LanguageColor,
    override var separatorColor: LanguageColor,
) : LanguageProperties {

    companion object {
        fun create(uuid: UUID): LanguageProperties {
            return LanguagePropertiesImpl(uuid, LangKey.defaultKey(), LanguageColorRegistry.Default.DARK_AQUA, LanguageColorRegistry.Default.WHITE, LanguageColorRegistry.Default.GRAY)
        }
    }

}