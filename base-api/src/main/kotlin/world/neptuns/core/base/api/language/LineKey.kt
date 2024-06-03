package world.neptuns.core.base.api.language

import world.neptuns.core.base.api.NeptunCoreProvider
import java.io.Serializable

interface LineKey : Serializable {

    val namespace: LangNamespace
    val value: String

    fun asString(): String = "${namespace.value.lowercase()}.${value.lowercase()}"

    companion object {
        fun key(namespace: LangNamespace, value: String): LineKey {
            return NeptunCoreProvider.api.newLineKey(namespace, value)
        }

        fun key(string: String): LineKey {

            // core.base.motd.normal
            // [core, base, motd, normal]
            //

            val keySplitted = string.split(".")
            val keyValue = keySplitted.subList(2, keySplitted.size).joinToString(".")
            return key(LangNamespace.create(keySplitted[0] + "." + keySplitted[1], null), keyValue)
        }

        fun coreKey(value: String): LineKey {
            return key("core.base.$value")
        }

        fun colorKey(value: String): LineKey {
            return key("core.base.color.$value")
        }

        fun colorDescriptionKey(value: String): LineKey {
            return key("core.base.color.$value.description")
        }

    }

}