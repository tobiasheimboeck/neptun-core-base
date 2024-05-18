package world.neptuns.core.base.api.language

import world.neptuns.core.base.api.NeptunCoreProvider

interface LineKey {

    val namespace: String
    val value: String

    fun asString(): String = "${namespace.lowercase()}.${value.lowercase()}"

    companion object {
        fun key(namespace: String, value: String): LineKey {
            return NeptunCoreProvider.api.newLineKey(namespace, value)
        }

        fun colorKey(value: String): LineKey {
            return key("core.color", value)
        }

        fun colorDescriptionKey(value: String): LineKey {
            return key("core.color", "$value.description")
        }

        fun fromString(string: String): LineKey {
            val keySplitted = string.split(".")
            val keyNamespace = "${keySplitted[0]}.${keySplitted[1]}"
            val keyValue = keySplitted.subList(2, keySplitted.size).joinToString(".")
            return key(keyNamespace, keyValue)
        }
    }

}