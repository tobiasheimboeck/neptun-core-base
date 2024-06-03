package world.neptuns.core.base.api.language.properties.default

import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.color.LanguageColor
import world.neptuns.core.base.api.language.color.LanguageColorRegistry
import world.neptuns.core.base.api.language.properties.LanguageProperties
import java.util.*

class DefaultLanguageProperties : LanguageProperties {

    override val uuid: UUID = UUID.randomUUID()
    override var langKey: LangKey = LangKey.key("en", "US")

    override var primaryColor: LanguageColor = LanguageColorRegistry.Default.DARK_AQUA
    override var secondaryColor: LanguageColor = LanguageColorRegistry.Default.GRAY
    override var separatorColor: LanguageColor = LanguageColorRegistry.Default.GRAY

}