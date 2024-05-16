package world.neptuns.core.base.api.language

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

interface Language {

    val name: String
    val cachedMessages: Map<String, String>

    fun validateLineAsString(key: String, toReplace: Any): String

    fun validateLine(key: String, vararg placeholders: TagResolver): Component

    fun validateLines(key: String, vararg placeholders: TagResolver): List<Component>

    fun validateItemName(key: String, vararg placeholders: TagResolver): Component

    fun validateItemLore(key: String, vararg placeholders: TagResolver): Component

}