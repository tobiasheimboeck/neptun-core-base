package world.neptuns.core.base.api.utils

import world.neptuns.core.base.api.language.LangNamespace
import world.neptuns.core.base.api.language.LineKey

interface NeptunPluginAdapter {

    val namespace: LangNamespace

    fun key(key: String): LineKey = LineKey.key(this.namespace, key)

}