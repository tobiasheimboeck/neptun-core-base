package world.neptuns.core.base.api.language

import java.util.*

interface LanguageController {

    val cachedLanguages: MutableList<Language>

    fun getLanguage(name: String): Language?

    fun getLanguage(uuid: UUID): Language?

    fun generateLanguages(mainClass: Class<*>)

    fun addContentToLanguage(mainClass: Class<*>)

}