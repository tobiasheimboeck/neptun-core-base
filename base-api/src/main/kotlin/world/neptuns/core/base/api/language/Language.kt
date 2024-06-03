package world.neptuns.core.base.api.language

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import world.neptuns.core.base.api.language.properties.LanguageProperties

interface Language {

    val key: LangKey
    val messages: Map<LineKey, String>

    fun lineAsString(properties: LanguageProperties, lineKey: LineKey, vararg toReplace: Any): String

    fun line(properties: LanguageProperties, lineKey: LineKey, vararg toReplace: TagResolver): Component

    fun lines(properties: LanguageProperties, lineKey: LineKey, vararg toReplace: TagResolver): List<Component>

    fun displayName(properties: LanguageProperties, lineKey: LineKey, vararg toReplace: TagResolver): Component

    fun lore(properties: LanguageProperties, lineKey: LineKey, vararg toReplace: TagResolver): List<Component>

    fun hasMultipleLines(lineKey: LineKey): Boolean

}