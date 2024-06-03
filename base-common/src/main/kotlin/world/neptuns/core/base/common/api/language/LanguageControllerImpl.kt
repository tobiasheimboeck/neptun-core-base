package world.neptuns.core.base.common.api.language

import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.Language
import world.neptuns.core.base.api.language.LanguageController
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.common.api.language.utils.LanguageFileUtils

class LanguageControllerImpl : LanguageController {

    override val languages: MutableList<Language> = mutableListOf()

    override fun getLanguage(key: LangKey): Language? {
        return this.languages.find { it.key.asString() == key.asString() }
    }

    override fun generateLanguages(loaderClass: Class<*>) {
        createLanguage("/lang", loaderClass)
        createLanguage("/lang/shared", loaderClass)
    }

    override fun addContentToLanguage(loaderClass: Class<*>) {
        createLanguage("/lang", loaderClass)
    }

    private fun createLanguage(rawPath: String, loaderClass: Class<*>) {
        for (langKey in LanguageFileUtils.getLanguageKeysFromJarFile(rawPath, loaderClass)) {
            val fileContent: MutableMap<LineKey, String> = LanguageFileUtils.getLanguageFileContent(rawPath, langKey, loaderClass)

            val language = this.languages.find { it.key.asString() == langKey.asString() }

            if (language == null) {
                this.languages.add(LanguageImpl(langKey, fileContent))
            } else {
                language.messages.putAll(fileContent)
            }
        }
    }

}