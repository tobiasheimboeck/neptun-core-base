package world.neptuns.core.base.api.language

import world.neptuns.core.base.api.NeptunCoreProvider
import java.io.Serializable

interface LangKey : Serializable {

    val countryCode: String
    val languageCode: String

    fun asString(): String = "${countryCode.lowercase()}_${languageCode.uppercase()}"

    companion object {
        fun key(countryCode: String, languageCode: String): LangKey {
            return NeptunCoreProvider.api.newLanguageKey(countryCode, languageCode)
        }

        fun defaultKey(): LangKey {
            return key("en", "US")
        }

        fun fromString(string: String): LangKey {
            val strings = string.split("_")
            return key(strings[0], strings[1])
        }
    }

}