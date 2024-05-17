package world.neptuns.core.base.api.language

import world.neptuns.core.base.api.NeptunCoreProvider

interface LanguageKey {

    val countryCode: String
    val languageCode: String

    fun asString(): String = "${countryCode.lowercase()}_${languageCode.uppercase()}"

    companion object {
        fun key(countryCode: String, languageCode: String): LanguageKey {
            return NeptunCoreProvider.api.newLanguageKey(countryCode, languageCode)
        }

        fun defaultKey(): LanguageKey {
            return key("en", "US")
        }

        fun fromString(string: String): LanguageKey {
            val strings = string.split("-")
            return key(strings[0], strings[1])
        }
    }

}