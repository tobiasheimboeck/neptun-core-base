package world.neptuns.core.base.common.api.language

import world.neptuns.core.base.api.language.Language
import world.neptuns.core.base.api.language.LanguageController
import java.util.*

class LanguageControllerImpl : LanguageController {

    override val cachedLanguages: MutableList<Language> = mutableListOf()

    override fun getLanguage(name: String): Language? {
        TODO("Not yet implemented")
    }

    override fun getLanguage(uuid: UUID): Language? {
        TODO("Not yet implemented")
    }

    override fun generateLanguages(mainClass: Class<*>) {
        TODO("Not yet implemented")
    }

    override fun addContentToLanguage(mainClass: Class<*>) {
        TODO("Not yet implemented")
    }

}