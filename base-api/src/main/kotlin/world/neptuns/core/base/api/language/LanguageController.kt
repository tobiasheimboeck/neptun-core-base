package world.neptuns.core.base.api.language

interface LanguageController {

    val languages: MutableList<Language>

    fun getLanguage(key: LangKey): Language?

    fun generateLanguages(loaderClass: Class<*>)

    fun addContentToLanguage(loaderClass: Class<*>)

}