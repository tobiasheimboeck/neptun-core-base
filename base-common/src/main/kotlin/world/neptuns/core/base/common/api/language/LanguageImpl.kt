package world.neptuns.core.base.common.api.language

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import world.neptuns.core.base.api.language.Language
import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.LineKey

class LanguageImpl(override val key: LangKey, override val messages: Map<LineKey, String>) : Language {

    override fun lineAsString(key: LineKey, toReplace: Any): String {
        TODO("Not yet implemented")
    }

    override fun line(key: LineKey, vararg toReplace: TagResolver): Component {
        TODO("Not yet implemented")
    }

    override fun lines(key: LineKey, vararg toReplace: TagResolver): List<Component> {
        TODO("Not yet implemented")
    }

    override fun displayName(key: LineKey, vararg toReplace: TagResolver): Component {
        TODO("Not yet implemented")
    }

    override fun lore(key: LineKey, vararg toReplace: TagResolver): Component {
        TODO("Not yet implemented")
    }

}