package world.neptuns.core.base.api.language

import world.neptuns.core.base.api.NeptunCoreProvider

interface LineKey {

    val namespace: String
    val value: String

    companion object {
        fun key(namespace: String, value: String): LineKey {
            return NeptunCoreProvider.api.newLineKey(namespace, value)
        }

        fun fromString(string: String): LanguageKey {
            val strings = string.split(".")
            return LanguageKey.key(strings[0], strings[1])
        }
    }

}