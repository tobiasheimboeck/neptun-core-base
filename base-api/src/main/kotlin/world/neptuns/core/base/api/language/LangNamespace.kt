package world.neptuns.core.base.api.language

import world.neptuns.core.base.api.NeptunCoreProvider
import java.io.Serializable

interface LangNamespace : Serializable {

    val value: String
    val subPrefix: String?

    companion object {

        fun create(value: String, subPrefix: String?): LangNamespace {
            return NeptunCoreProvider.api.newNamespace(value, subPrefix)
        }

    }

}