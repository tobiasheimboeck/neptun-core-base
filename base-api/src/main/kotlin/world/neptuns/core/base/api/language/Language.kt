package world.neptuns.core.base.api.language

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

interface Language {

    val name: LanguageKey
    val messages: Map<String, String>

    fun lineAsString(key: String, toReplace: Any): String

    fun line(key: String, vararg toReplace: TagResolver): Component

    fun lines(key: String, vararg toReplace: TagResolver): List<Component>

    fun displayName(key: String, vararg toReplace: TagResolver): Component

    fun lore(key: String, vararg toReplace: TagResolver): Component

}