package world.neptuns.core.base.api.language

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

interface Language {

    val key: LangKey
    val messages: Map<LineKey, String>

    fun lineAsString(key: LineKey, toReplace: Any): String

    fun line(key: LineKey, vararg toReplace: TagResolver): Component

    fun lines(key: LineKey, vararg toReplace: TagResolver): List<Component>

    fun displayName(key: LineKey, vararg toReplace: TagResolver): Component

    fun lore(key: LineKey, vararg toReplace: TagResolver): Component

}